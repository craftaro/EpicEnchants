package com.songoda.epicenchants.utils.single;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.songoda.epicenchants.EpicEnchants;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;

import java.util.*;
import java.util.stream.Collectors;

import static com.songoda.epicenchants.utils.single.ItemGroup.Group.*;
import static org.bukkit.Material.*;

public class ItemGroup {

    private Multimap<Group, Material> groupMap;

    public ItemGroup(EpicEnchants instance) {
        groupMap = HashMultimap.create();
        if (instance.getVersion() > 12) setupMaster();
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

        optionalGroup.ifPresent(group -> output.addAll(getMaterials(group)));

        if (Material.matchMaterial(key) != null) {
            output.add(Material.matchMaterial(key));
        }

        return output;
    }

    public Set<String> getGroups(Set<Material> materials) {
        Set<String> groups = new HashSet<>();

        for (int i = 0; i < 5; i++) {
            getGroup(materials).ifPresent(group -> {
                groups.add(group.getName());
                materials.removeAll(getMaterials(group));
            });
        }

        groups.addAll(materials.stream().map(Material::toString).collect(Collectors.toSet()));
        return groups;
    }

    public Optional<Group> getGroup(Set<Material> materials) {
        Optional<Group> group = Arrays.stream(Group.values())
                .filter(s -> !s.getChildren().isEmpty() && s.getChildren().stream().allMatch(child -> materials.containsAll(groupMap.get(child))))
                .findFirst();

        if (group.isPresent()) {
            return group;
        }

        return groupMap.asMap().entrySet().stream().filter(s -> materials.containsAll(s.getValue())).map(Map.Entry::getKey).findFirst();
    }

    public Set<Material> getMaterials(Group group) {
        Set<Material> out = new HashSet<>();

        for (int i = 0; i < 5; i++) {
            if (group.getChildren().isEmpty())
                out.addAll(groupMap.get(group));
            else
                out.addAll(group.getChildren().stream().map(this::getMaterials).flatMap(Collection::stream).collect(Collectors.toSet()));
        }

        return out;
    }


    public enum Group {
        AXES,
        PICKAXES,
        SHOVELS,
        TOOLS(AXES, PICKAXES, SHOVELS),

        SWORDS,
        BOWS,
        WEAPONS(SWORDS, BOWS, AXES),

        BOOTS,
        LEGGINGS,
        CHESTPLATES,
        HELMETS,
        ARMOR(BOOTS, LEGGINGS, CHESTPLATES, HELMETS);

        private final Set<Group> children;

        Group(Group... child) {
            children = child == null ? new HashSet<>() : new HashSet<>(Arrays.asList(child));
        }

        public static Optional<Group> from(String key) {
            return Arrays.stream(values()).filter(s -> s.toString().equalsIgnoreCase(key)).findFirst();
        }

        public String getName() {
            return StringUtils.capitalize(toString().toLowerCase());
        }

        public Set<Group> getChildren() {
            return this.children;
        }
    }
}
