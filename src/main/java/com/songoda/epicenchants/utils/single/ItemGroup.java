package com.songoda.epicenchants.utils.single;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.songoda.core.compatibility.CompatibleMaterial;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.songoda.core.compatibility.CompatibleMaterial.*;
import static com.songoda.epicenchants.utils.single.ItemGroup.Group.*;

public class ItemGroup {

    private final Multimap<Group, CompatibleMaterial> groupMap;

    public ItemGroup() {
        groupMap = HashMultimap.create();

        groupMap.putAll(AXES, Arrays.asList(NETHERITE_AXE, DIAMOND_AXE, GOLDEN_AXE, IRON_AXE, STONE_AXE, WOODEN_AXE));

        groupMap.putAll(PICKAXES, Arrays.asList(NETHERITE_PICKAXE, DIAMOND_PICKAXE, GOLDEN_PICKAXE, IRON_PICKAXE, STONE_PICKAXE, WOODEN_PICKAXE));

        groupMap.putAll(HOES, Arrays.asList(NETHERITE_HOE, DIAMOND_HOE, GOLDEN_HOE, IRON_HOE, STONE_HOE, WOODEN_HOE));

        groupMap.putAll(SHOVELS, Arrays.asList(NETHERITE_SHOVEL, DIAMOND_SHOVEL, GOLDEN_SHOVEL, IRON_SHOVEL, STONE_SHOVEL, WOODEN_SHOVEL));

        groupMap.putAll(SWORDS, Arrays.asList(NETHERITE_SWORD, DIAMOND_SWORD, GOLDEN_SWORD, IRON_SWORD, STONE_SWORD, WOODEN_SWORD));

        groupMap.put(BOWS, BOW);

        groupMap.putAll(BOOTS, Arrays.asList(NETHERITE_BOOTS, DIAMOND_BOOTS, GOLDEN_BOOTS, IRON_BOOTS, LEATHER_BOOTS));

        groupMap.putAll(LEGGINGS, Arrays.asList(NETHERITE_LEGGINGS, DIAMOND_LEGGINGS, GOLDEN_LEGGINGS, IRON_LEGGINGS, LEATHER_LEGGINGS));

        groupMap.putAll(CHESTPLATES, Arrays.asList(NETHERITE_CHESTPLATE, DIAMOND_CHESTPLATE, GOLDEN_CHESTPLATE, IRON_CHESTPLATE, LEATHER_CHESTPLATE));

        groupMap.putAll(HELMETS, Arrays.asList(NETHERITE_HELMET, DIAMOND_HELMET, GOLDEN_HELMET, IRON_HELMET, LEATHER_HELMET));

        groupMap.put(TRIDENTS, TRIDENT);
    }

    public Set<CompatibleMaterial> get(String key) {
        Optional<Group> optionalGroup = Group.from(key);
        Set<CompatibleMaterial> output = new HashSet<>();

        optionalGroup.ifPresent(group -> output.addAll(getMaterials(group)));

        if (CompatibleMaterial.getMaterial(key) != null) {
            output.add(CompatibleMaterial.getMaterial(key));
        }

        return output;
    }

    public boolean isValid(CompatibleMaterial material) {
        for (Group group : groupMap.keys())
            if (getMaterials(group).contains(material))
                return true;
        return false;
    }

    public Set<String> getGroups(Set<CompatibleMaterial> materials) {
        Set<String> groups = new HashSet<>();

        for (int i = 0; i < 5; i++) {
            getGroup(materials).ifPresent(group -> {
                groups.add(group.getName());
                materials.removeAll(getMaterials(group).stream().collect(Collectors.toList()));
            });
        }

        groups.addAll(materials.stream().map(CompatibleMaterial::toString).collect(Collectors.toSet()));
        return groups;
    }

    public Optional<Group> getGroup(Set<CompatibleMaterial> materials) {
        Optional<Group> group = Arrays.stream(Group.values())
                .filter(s -> !s.getChildren().isEmpty() && s.getChildren().stream().allMatch(child -> materials.containsAll(groupMap.get(child))))
                .findFirst();

        if (group.isPresent()) {
            return group;
        }

        return groupMap.asMap().entrySet().stream().filter(s -> materials.containsAll(s.getValue())).map(Map.Entry::getKey).findFirst();
    }

    public Set<CompatibleMaterial> getMaterials(Group group) {
        Set<CompatibleMaterial> out = new HashSet<>();

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
        HOES,
        PICKAXES,
        SHOVELS,
        TOOLS(AXES, PICKAXES, HOES, SHOVELS),

        SWORDS,
        BOWS,
        TRIDENTS,
        WEAPONS(SWORDS, BOWS, AXES, TRIDENTS),

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
