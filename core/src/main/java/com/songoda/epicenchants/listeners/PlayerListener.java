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

import java.util.Map;

import static com.songoda.epicenchants.enums.EnchantProcType.DAMAGED;
import static com.songoda.epicenchants.enums.EnchantProcType.DEALT_DAMAGE;

public class PlayerListener implements Listener {
    private final EpicEnchants instance;

    public PlayerListener(EpicEnchants instance) {
        this.instance = instance;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onArmorEquip(ArmorEquipEvent event) {
        Map<Enchant, Integer> oldArmorMap = instance.getEnchantUtils().getEnchants(event.getOldArmorPiece());
        Map<Enchant, Integer> newArmorMap = instance.getEnchantUtils().getEnchants(event.getNewArmorPiece());

        oldArmorMap.forEach((enchant, level) -> enchant.onUnEquip(event.getPlayer(), level));
        newArmorMap.forEach((enchant, level) -> enchant.onEquip(event.getPlayer(), level));
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntityType() != EntityType.PLAYER || !(event.getDamager() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        Player damager = (Player) event.getDamager();

        instance.getEnchantUtils().handlePlayer(player, event, DAMAGED);
        instance.getEnchantUtils().handlePlayer(damager, event, DEALT_DAMAGE);
    }
}
