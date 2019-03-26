package com.songoda.epicenchants.managers;

import com.songoda.epicenchants.objects.Placeholder;
import com.songoda.ultimatebottles.UltimateBottles;
import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Optional;

@Getter
public class HookManager {
    private UltimateBottles ultimateBottles;

    public void setup() {
        ultimateBottles = Bukkit.getPluginManager().isPluginEnabled("UltimateBottles") ? (UltimateBottles) Bukkit.getPluginManager().getPlugin("UltimateBottles") : null;
    }

    public Optional<UltimateBottles> getUltimateBottles() {
        return Optional.ofNullable(ultimateBottles);
    }

    public static String setPAPIPlaceholders(String input, Player user, LivingEntity opponent, int level) {
        for (Placeholder pair : Arrays.asList(
                Placeholder.of("level", level),
                Placeholder.of("user_health", user.getHealth()),
                Placeholder.of("attacker_health", opponent == null ? -1 : opponent.getHealth()),
                Placeholder.of("user_food", user.getFoodLevel()),
                Placeholder.of("attacker_food", opponent instanceof Player ? ((Player) opponent).getFoodLevel() : 0),
                Placeholder.of("user_is_sneaking", user.isSneaking()),
                Placeholder.of("attacker_is_sneaking", opponent instanceof Player && ((Player) opponent).isSneaking()),
                Placeholder.of("world", user.getWorld().getName()),
                Placeholder.of("players_near", user.getNearbyEntities(4, 4, 4).size()),
                Placeholder.of("user_on_fire", user.getFireTicks() != 0),
                Placeholder.of("attacker_on_fire", opponent != null && opponent.getFireTicks() != 0)
        )) {
            input = input.replace(pair.getPlaceholder(), pair.getToReplace().toString());
        }

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            input = PlaceholderAPI.setPlaceholders(user, input);

            if (opponent instanceof Player) {
                input = PlaceholderAPI.setRelationalPlaceholders(user, (Player) opponent, input);
            }
        }

        return input;
    }
}
