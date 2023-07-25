package com.craftaro.epicenchants.managers;

import com.craftaro.epicenchants.objects.Group;
import com.craftaro.epicenchants.EpicEnchants;
import com.craftaro.epicenchants.utils.single.ConfigParser;
import org.bukkit.configuration.ConfigurationSection;

public class GroupManager extends Manager<String, Group> {
    public GroupManager(EpicEnchants instance) {
        super(instance);
    }

    public void loadGroups() {
        ConfigurationSection config = this.instance.getFileManager().getConfiguration("groups").getConfigurationSection("groups");
        config.getKeys(false).forEach(key -> {
            Group group = ConfigParser.parseGroup(this.instance, config.getConfigurationSection(key));
            add(group.getIdentifier().toUpperCase(), group);
        });
    }
}
