package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.enums.MaterialType;
import com.songoda.epicenchants.wrappers.PotionEffectWrapper;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Set;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.enums.EnchantProcType.*;

@Builder
@Getter
public class Enchant {
    private String identifier;
    private int tier;
    private int maxLevel;
    private MaterialType materialType;
    private Set<PotionEffectWrapper> potionEffects;
    private Set<Material> itemWhitelist;
    private String format;
    private ActionClass action;
    private BookItem bookItem;

    public void onEquip(Player player, int level) {
        potionEffects.stream().map(p -> p.get(level)).forEach(player::addPotionEffect);
    }

    public void onUnEquip(Player player, int level) {
        Set<PotionEffectType> effects = potionEffects.stream().map(p -> p.get(level)).map(PotionEffect::getType).collect(Collectors.toSet());
        player.getActivePotionEffects().stream().map(PotionEffect::getType).filter(effects::contains).forEach(player::removePotionEffect);
    }

    public void onReceiveDamage(EntityDamageByEntityEvent event, int level) {
        event.setDamage(action.run(((Player) event.getEntity()), ((Player) event.getDamager()), level, event.getDamage(), DAMAGED, materialType));
    }

    public void onDealDamage(EntityDamageByEntityEvent event, int level) {
        event.setDamage(action.run(((Player) event.getEntity()), ((Player) event.getDamager()), level, event.getDamage(), DEALT_DAMAGE, materialType));
    }

    public void onMine(BlockBreakEvent event, int level) {
    }
}
