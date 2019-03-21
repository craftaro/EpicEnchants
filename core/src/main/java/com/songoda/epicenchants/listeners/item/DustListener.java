package com.songoda.epicenchants.listeners.item;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.objects.Group;
import de.tr7zw.itemnbtapi.NBTItem;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.ThreadLocalRandom;

public class DustListener extends ItemListener {
    public DustListener(EpicEnchants instance) {
        super(instance);
    }

    @Override
    void onApply(InventoryClickEvent event, NBTItem cursor, NBTItem current) {
        if (!cursor.hasKey("dust") || !cursor.getBoolean("dust")) {
            return;
        }

        if (!current.hasKey("book-item") || !current.getBoolean("book-item")) {
            return;
        }

        Enchant enchant = instance.getEnchantManager().getValue(current.getString("enchant")).orElseThrow(() -> new IllegalStateException("Book without enchant!"));

        if (!enchant.getGroup().equals(instance.getGroupManager().getValue(cursor.getString("group")).orElseThrow(() -> new IllegalStateException("Dust without group!")))) {
            return;
        }

        int successRate = current.getInteger("success-rate");

        if (successRate == 100) {
            return;
        }

        successRate = successRate + cursor.getInteger("percentage") > 100 ? 100 : successRate + cursor.getInteger("percentage");
        event.setCurrentItem(enchant.getBook().get(enchant, current.getInteger("level"), successRate, current.getInteger("destroy-rate")));

        event.setCancelled(true);
        useItem(event);
    }

    @Override
    void onClick(PlayerInteractEvent event, NBTItem clicked) {
        if (!clicked.hasKey("secret-dust") || !clicked.getBoolean("secret-dust")) {
            return;
        }

        event.setCancelled(true);

        if (event.getItem().getAmount() != 1 && event.getPlayer().getInventory().firstEmpty() == -1) {
            return;
        }

        Group group = instance.getGroupManager().getValueUnsafe(clicked.getString("group"));
        int rate = ThreadLocalRandom.current().nextInt(clicked.getInteger("min-rate"), clicked.getInteger("max-rate"));

        useItem(event);
        event.getPlayer().getInventory().addItem(instance.getSpecialItems().getDust(group, null, rate, false));
    }
}
