package com.songoda.epicenchants.utils.single;

import com.google.common.collect.Multimap;
import com.songoda.epicenchants.EpicEnchants;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;

import java.util.*;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.utils.single.ItemGroup.Group.*;
import static org.bukkit.Material.*;

public class ItemGroup {

    private Multimap<Group, Material> groupMap;

    public ItemGroup(EpicEnchants instance) {
        if (instance.getVersion() > 1.12) setupMaster();
        else setupLegacy();
    }

    private void setupMaster() {
        groupMap.putAll(AXES, Arrays.asList(DIAMOND_AXE, GOLDEN_AXE, IRON_AXE, STONE_AXE, WOODEN_AXE));

        groupMap.putAll(PICKAXES, Arrays.asList(DIAMOND_PICKAXE, GOLDEN_PICKAXE, IRON_PICKAXE, STONE_PICKAXE, WOODEN_PICKAXE));

        groupMap.putAll(SWORDS, Arrays.asList(DIAMOND_SWORD, GOLDEN_SWORD, IRON_SWORD, STONE_SWORD, WOODEN_SWORD));

        groupMap.put(BOWS, BOW);

        groupMap.putAll(BOOTS, Arrays.asList(DIAMOND_BOOTS, GOLDEN_BOOTS, IRON_BOOTS, LEATHER_BOOTS));

        groupMap.putAll(LEGGINGS, Arrays.asList(DIAMOND_LEGGINGS, GOLDEN_LEGGINGS, IRON_LEGGINGS, LEATHER_LEGGINGS));

        groupMap.putAll(CHESTPLATES, Arrays.asList(DIAMOND_CHESTPLATE, GOLDEN_CHESTPLATE, IRON_CHESTPLATE, LEATHER_CHESTPLATE));

        groupMap.putAll(HELMETS, Arrays.asList(DIAMOND_HELMET, GOLDEN_HELMET, IRON_HELMET, LEATHER_HELMET));
    }

    private void setupLegacy() {
        groupMap.putAll(AXES, Arrays.asList(DIAMOND_AXE, Material.valueOf("GOLD_AXE"), IRON_AXE, STONE_AXE, Material.valueOf("WOOD_AXE")));

        groupMap.putAll(PICKAXES, Arrays.asList(DIAMOND_PICKAXE, Material.valueOf("GOLD_PICKAXE"), IRON_PICKAXE, STONE_PICKAXE, Material.valueOf("WOOD_PICKAXE")));

        groupMap.putAll(SWORDS, Arrays.asList(DIAMOND_SWORD, Material.valueOf("GOLD_SWORD"), IRON_SWORD, STONE_SWORD, Material.valueOf("WOOD_SWORD")));

        groupMap.put(BOWS, BOW);

        groupMap.putAll(BOOTS, Arrays.asList(DIAMOND_BOOTS, Material.valueOf("GOLD_BOOTS"), IRON_BOOTS, LEATHER_BOOTS));

        groupMap.putAll(LEGGINGS, Arrays.asList(DIAMOND_LEGGINGS, Material.valueOf("GOLD_LEGGINGS"), IRON_LEGGINGS, LEATHER_LEGGINGS));

        groupMap.putAll(CHESTPLATES, Arrays.asList(DIAMOND_CHESTPLATE, Material.valueOf("GOLD_CHESTPLATE"), IRON_CHESTPLATE, LEATHER_CHESTPLATE));

        groupMap.putAll(HELMETS, Arrays.asList(DIAMOND_HELMET, Material.valueOf("GOLD_HELMET"), IRON_HELMET, LEATHER_HELMET));
    }

    public Set<Material> get(String key) {
        Optional<Group> optionalGroup = Group.from(key);
        Set<Material> output = new HashSet<>();

        optionalGroup.ifPresent(group -> {
            output.addAll(groupMap.get(group));
            output.addAll(group.getChildren().stream().map(groupMap::get).flatMap(Collection::stream).collect(Collectors.toSet()));
        });

        if (Material.matchMaterial(key) != null) {
            output.add(Material.matchMaterial(key));
        }

        return output;
    }


    public enum Group {
        AXES,
        PICKAXES,
        SHOVELS,
        TOOLS(AXES, PICKAXES, SHOVELS),

        SWORDS,
        BOWS,
        WEAPONS(SWORDS, BOWS),

        BOOTS,
        LEGGINGS,
        CHESTPLATES,
        HELMETS,
        ARMOR(BOOTS, LEGGINGS, CHESTPLATES, HELMETS);

        @Getter private final Set<Group> children;

        Group(Group... child) {
            children = child == null ? new HashSet<>() : new HashSet<>(Arrays.asList(child));
        }

        public static Optional<Group> from(String key) {
            return Arrays.stream(values()).filter(s -> s.toString().equalsIgnoreCase(key)).findFirst();
        }

        public String getName() {
            return StringUtils.capitalize(toString().toLowerCase());
        }
    }
}
