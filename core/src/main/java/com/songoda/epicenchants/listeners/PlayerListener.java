package com.songoda.epicenchants.listeners;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.events.ArmorEquipEvent;
import com.songoda.epicenchants.objects.Enchant;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.util.Map;

import static com.songoda.epicenchants.enums.EnchantType.*;
import static com.songoda.epicenchants.enums.EventType.OFF;
import static com.songoda.epicenchants.enums.EventType.ON;

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
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntityType() == EntityType.PLAYER) {
            Player defender = (Player) event.getEntity();
            instance.getEnchantUtils().handlePlayer(defender, event, event.getDamager() instanceof Player ? DEFENSE_PLAYER : DEFENSE_MOB);
        }

        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            instance.getEnchantUtils().handlePlayer(attacker, event, ATTACK_PLAYER);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        instance.getEnchantUtils().getEnchants(event.getPlayer().getInventory().getItem(event.getNewSlot()))
                .forEach((enchant, level) -> enchant.onAction(event.getPlayer(), null, event, level, HELD_ITEM, ON));

        instance.getEnchantUtils().getEnchants(event.getPlayer().getInventory().getItem(event.getPreviousSlot()))
                .forEach((enchant, level) -> enchant.onAction(event.getPlayer(), null, event, level, HELD_ITEM, OFF));

    }
}
