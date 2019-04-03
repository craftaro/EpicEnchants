package com.songoda.epicenchants.listeners;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.events.ArmorEquipEvent;
import com.songoda.epicenchants.objects.Enchant;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Arrays;
import java.util.Map;

import static com.songoda.epicenchants.enums.EventType.*;
import static com.songoda.epicenchants.enums.TriggerType.*;

public class PlayerListener implements Listener {
    private final EpicEnchants instance;

    public PlayerListener(EpicEnchants instance) {
        this.instance = instance;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onArmorEquip(ArmorEquipEvent event) {
        Map<Enchant, Integer> oldArmorMap = instance.getEnchantUtils().getEnchants(event.getOldArmorPiece());
        Map<Enchant, Integer> newArmorMap = instance.getEnchantUtils().getEnchants(event.getNewArmorPiece());

        oldArmorMap.forEach((enchant, level) -> enchant.onAction(event.getPlayer(), null, event, level, STATIC_EFFECT, OFF));
        newArmorMap.forEach((enchant, level) -> enchant.onAction(event.getPlayer(), null, event, level, STATIC_EFFECT, ON));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        instance.getEnchantUtils().getEnchants(event.getPlayer().getInventory().getItem(event.getNewSlot()))
                .forEach((enchant, level) -> enchant.onAction(event.getPlayer(), null, event, level, HELD_ITEM, ON));

        instance.getEnchantUtils().getEnchants(event.getPlayer().getInventory().getItem(event.getPreviousSlot()))
                .forEach((enchant, level) -> enchant.onAction(event.getPlayer(), null, event, level, HELD_ITEM, OFF));

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        instance.getEnchantUtils().handlePlayer(event.getEntity(), event.getEntity().getKiller(), event, DEATH);

        if (event.getEntity().getKiller() != null) {
            instance.getEnchantUtils().handlePlayer(event.getEntity().getKiller(), event.getEntity(), event, KILLED_PLAYER);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            instance.getEnchantUtils().handlePlayer(event.getPlayer(), null, event, RIGHT_CLICK);
        } else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            instance.getEnchantUtils().handlePlayer(event.getPlayer(), null, event, LEFT_CLICK);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent event) {
        instance.getEnchantUtils().handlePlayer(event.getPlayer(), null, event, BLOCK_BREAK);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().getActivePotionEffects().stream().filter(potion -> potion.getDuration() >= 32760)
                .forEach(potionEffect -> event.getPlayer().removePotionEffect(potionEffect.getType()));

        Arrays.stream(event.getPlayer().getInventory().getArmorContents()).forEach(itemStack -> {
            instance.getEnchantUtils().getEnchants(itemStack).forEach((enchant, level) ->
                    enchant.onAction(event.getPlayer(), null, event, level, STATIC_EFFECT, ON));
        });
    }
}
