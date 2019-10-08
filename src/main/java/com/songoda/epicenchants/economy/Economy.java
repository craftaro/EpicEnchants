package com.songoda.epicenchants.economy;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public interface Economy {

    boolean hasBalance(OfflinePlayer player, double cost);

    double getBalance(Player player);

    boolean withdrawBalance(OfflinePlayer player, double cost);

    boolean deposit(OfflinePlayer player, double amount);
}
