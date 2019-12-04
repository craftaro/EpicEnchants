package com.songoda.epicenchants.effect.effects;

import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class DropHead extends EffectExecutor {
    public DropHead(ConfigurationSection section) {
        super(section);
    }

    @Override
    public void execute(@NotNull Player user, LivingEntity opponent, int level, EventType eventType) {
        consume(entity -> getHead(entity).ifPresent(head -> entity.getWorld().dropItemNaturally(entity.getLocation(), head)), user, opponent);
    }

    private Optional<ItemStack> getHead(Entity entity) {
        ItemStack out = CompatibleMaterial.PLAYER_HEAD.getItem();
        String skin = "";

        switch (entity.getType()) {
            case CHICKEN:
                skin = "MHF_Chicken";
                break;
            case PIG_ZOMBIE:
                skin = "MHF_PigZombie";
                break;
            case PIG:
                skin = "MHF_Pig";
                break;
            case COW:
                skin = "MHF_Cow";
                break;
            case SHEEP:
                skin = "MHF_Sheep";
                break;
            case MUSHROOM_COW:
                skin = "MHF_MushroomCow";
                break;
            case SPIDER:
                skin = "MHF_Spider";
                break;
            case ZOMBIE:
                out = CompatibleMaterial.ZOMBIE_HEAD.getItem();
                break;
            case SKELETON:
                out = CompatibleMaterial.SKELETON_SKULL.getItem();
                break;
            case VILLAGER:
                skin = "MHF_Villager";
                break;
            case MAGMA_CUBE:
                skin = "MHF_LavaSlime";
                break;
            case BLAZE:
                skin = "MHF_Blaze";
                break;
            case CREEPER:
                out = CompatibleMaterial.CREEPER_HEAD.getItem();
            case ENDERMAN:
                skin = "MHF_Enderman";
                break;
            case RABBIT:
                skin = "MHF_Rabbit";
                break;
            case IRON_GOLEM:
                skin = "MHF_Golem";
            case PLAYER:
                break;
            default:
                return Optional.empty();
        }

        SkullMeta skullMeta = (SkullMeta) out.getItemMeta();

        skullMeta.setOwner(entity instanceof Player ? entity.getName() : skin);
        out.setItemMeta(skullMeta);

        return Optional.of(out);
    }
}
