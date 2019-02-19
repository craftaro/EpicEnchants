package com.songoda.epicenchants.hooks;

import org.bukkit.entity.Player;

public interface FactionsHook {
    String getRelationToLocation(Player player);

    boolean isFriendly(Player player, Player other);
}
