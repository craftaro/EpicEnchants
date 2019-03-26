package com.songoda.epicenchants.utils.single;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Placeholders {

    private static final Map<String, Function<Event, String>> EVENT_FUNCTIONS = new HashMap<String, Function<Event, String>>() {{
        put("{block_type}", event -> {
            if (event instanceof BlockBreakEvent) {
                return ((BlockBreakEvent) event).getBlock().getType().toString();
            } else if (event instanceof BlockPlaceEvent) {
                return ((BlockPlaceEvent) event).getBlockPlaced().getType().toString();
            } else return "N/A";
        });

        put("{clicked_type}", event -> {
            if (event instanceof PlayerInteractEvent && ((PlayerInteractEvent) event).hasBlock()) {
                return ((PlayerInteractEvent) event).getClickedBlock().getType().toString();
            } else if (event instanceof PlayerInteractEntityEvent) {
                return ((PlayerInteractEntityEvent) event).getRightClicked().getType().toString();
            } else return "N/A";
        });
    }};

    private static final Map<String, BiFunction<Player, LivingEntity, Object>> PLAYER_FUNCTIONS = new HashMap<String, BiFunction<Player, LivingEntity, Object>>() {{
        put("{user_health}", (user, opponent) -> user.getHealth());
        put("{attacker_health}", (user, opponent) -> opponent == null ? -1 : opponent.getHealth());
        put("{user_food}", (user, opponent) -> user.getFoodLevel());
        put("{attacker_food}", (user, opponent) -> opponent instanceof Player ? ((Player) opponent).getFoodLevel() : 0);
        put("{user_is_sneaking}", (user, opponent) -> user.isSneaking());
        put("{attacker_is_sneaking}", (user, opponent) -> opponent instanceof Player && ((Player) opponent).isSneaking());
        put("{world}", (user, opponent) -> user.getWorld().getName());
        put("{players_near}", (user, opponent) -> user.getNearbyEntities(4, 4, 4).size());
        put("{user_on_fire}", (user, opponent) -> user.getFireTicks() != 0);
        put("{attacker_on_fire}", (user, opponent) -> opponent != null && opponent.getFireTicks() != 0);
    }};

    public static String setPlaceholders(String input, Player user, LivingEntity opponent, int level) {
        return setPlaceholders(input, user, opponent, level, null);
    }

    public static String setPlaceholders(String input, Player user, LivingEntity opponent, int level, Event event) {
        AtomicReference<String> output = new AtomicReference<>(input.replace("{level}", "" + level));

        PLAYER_FUNCTIONS.forEach((toReplace, function) -> output.updateAndGet(string -> string.replace(toReplace, function.apply(user, opponent).toString())));
        Optional.ofNullable(event).ifPresent(e -> EVENT_FUNCTIONS.forEach((toReplace, function) -> output.updateAndGet(string -> "'" + string.replace(toReplace, function.apply(e)) + "'")));

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            output.updateAndGet(string -> PlaceholderAPI.setPlaceholders(user, string));

            if (opponent instanceof Player) {
                output.updateAndGet(string -> PlaceholderAPI.setRelationalPlaceholders(user, (Player) opponent, input));
            }
        }

        return output.get();
    }
}
