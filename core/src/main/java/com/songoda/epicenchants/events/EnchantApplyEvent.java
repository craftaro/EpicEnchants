package com.songoda.epicenchants.events;

import com.songoda.epicenchants.objects.Enchant;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

@Getter
public class EnchantApplyEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final ItemStack toEnchant;
    private final Enchant enchant;
    private final int level;
    private final int successRate;
    private final int destroyRate;
    private boolean cancelled = false;

    public EnchantApplyEvent(ItemStack toEnchant, Enchant enchant, int level, int successRate, int destroyRate) {
        this.toEnchant = toEnchant;
        this.enchant = enchant;
        this.level = level;
        this.successRate = successRate;
        this.destroyRate = destroyRate;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
