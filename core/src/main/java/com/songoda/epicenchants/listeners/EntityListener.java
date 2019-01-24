package com.songoda.epicenchants.listeners;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.enums.EffectType;
import org.bukkit.entity.Explosive;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.projectiles.ProjectileSource;

import static com.songoda.epicenchants.enums.EffectType.*;
import static org.bukkit.entity.EntityType.PLAYER;
import static org.bukkit.event.entity.EntityDamageEvent.DamageCause.*;

public class EntityListener implements Listener {
    private final EpicEnchants instance;

    public EntityListener(EpicEnchants instance) {
        this.instance = instance;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Mob && event.getEntity().getKiller() != null) {
            instance.getEnchantUtils().handlePlayer(event.getEntity().getKiller(), event, KILLED_MOB);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Projectile) {
            ProjectileSource source = ((Projectile) event.getDamager()).getShooter();

            if (event.getEntity() instanceof Player) {
                instance.getEnchantUtils().handlePlayer(((Player) event.getEntity()), event, source instanceof Player ? DEFENSE_PLAYER_RANGE : DEFENSE_MOB_RANGE);
            }

            if (!(source instanceof Player)) {
                return;
            }

            instance.getEnchantUtils().handlePlayer(((Player) source), event, event.getEntity() instanceof Player ? ATTACK_PLAYER_RANGE : ATTACK_MOB_RANGE);
        }


        if (event.getEntity() instanceof Player) {
            Player defender = (Player) event.getEntity();
            EffectType effectType = null;

            if (event.getDamager() instanceof Player) {
                effectType = DEFENSE_PLAYER_MELEE;
            } else if (event.getDamager() instanceof Mob) {
                effectType = DEFENSE_MOB_MELEE;
            } else if (event.getDamager() instanceof Explosive) {
                effectType = EXPLOSION_DAMAGE;
            }

            if (effectType != null) {
                instance.getEnchantUtils().handlePlayer(defender, event, effectType);
            }
        }

        if (event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            EffectType effectType = null;

            if (event.getEntity() instanceof Player) {
                effectType = ATTACK_PLAYER_MELEE;
            } else if (event.getEntity() instanceof Mob) {
                effectType = ATTACK_MOB_MELEE;
            }

            if (effectType != null) {
                instance.getEnchantUtils().handlePlayer(attacker, event, effectType);
            }

            instance.getEnchantUtils().handlePlayer(attacker, event, event.getEntityType() == PLAYER ? ATTACK_PLAYER_MELEE : ATTACK_MOB_MELEE);
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntityDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        if (event.getCause() == FALL) {
            instance.getEnchantUtils().handlePlayer(((Player) event.getEntity()), event, FALL_DAMAGE);
            return;
        }

        if (event.getCause() == FIRE || event.getCause() == FIRE_TICK) {
            instance.getEnchantUtils().handlePlayer(((Player) event.getEntity()), event, FIRE_DAMAGE);
        }
    }


}
