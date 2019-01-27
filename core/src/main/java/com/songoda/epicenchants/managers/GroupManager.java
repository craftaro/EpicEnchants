package com.songoda.epicenchants.managers;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Group;
import com.songoda.epicenchants.utils.ConfigParser;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class GroupManager {
    private final Map<String, Group> groupMap;
    private final EpicEnchants instance;

    public GroupManager(EpicEnchants instance) {
        this.instance = instance;
        this.groupMap = new HashMap<>();
    }

    public Optional<Group> getGroup(String identifier) {
        return Optional.ofNullable(groupMap.get(identifier));
    }

    public void addGroup(Group group) {
        groupMap.put(group.getIdentifier(), group);
    }

    public void loadGroups() {
        ConfigurationSection config = instance.getFileManager().getConfiguration("groups").getConfigurationSection("groups");
        config.getKeys(false).forEach(key -> addGroup(ConfigParser.parseGroup(config.getConfigurationSection(key))));
    }

    public Collection<Group> getGroups() {
        return Collections.unmodifiableCollection(groupMap.values());
    }
}
