package com.songoda.epicenchants.listeners.item;

import com.songoda.core.nms.nbt.NBTItem;
import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.objects.Enchant;
import com.songoda.epicenchants.objects.Group;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.concurrent.ThreadLocalRandom;

public class DustListener extends ItemListener {
    public DustListener(EpicEnchants instance) {
        super(instance);
    }

    @Override
    void onApply(InventoryClickEvent event, NBTItem cursor, NBTItem current) {
        if (!cursor.has("dust") || !cursor.getNBTObject("dust").asBoolean()) {
            return;
        }

        if (!current.has("book-item") || !current.getNBTObject("book-item").asBoolean()) {
            return;
        }

        Enchant enchant = instance.getEnchantManager().getValue(current.getNBTObject("enchant").asString()).orElseThrow(() -> new IllegalStateException("Book without enchant!"));

        if (!enchant.getGroup().equals(instance.getGroupManager().getValue(cursor.getNBTObject("group").asString()).orElseThrow(() -> new IllegalStateException("Dust without group!")))) {
            return;
        }

        int successRate = current.getNBTObject("success-rate").asInt();

        if (successRate == 100) {
            return;
        }

        successRate = Math.min(successRate + cursor.getNBTObject("percentage").asInt(), 100);
        event.setCurrentItem(enchant.getBook().get(enchant, current.getNBTObject("level").asInt(), successRate, current.getNBTObject("destroy-rate").asInt()));

        event.setCancelled(true);
        useItem(event);
    }

    @Override
    void onClick(PlayerInteractEvent event, NBTItem clicked) {
        if (!clicked.has("secret-dust") || !clicked.getNBTObject("secret-dust").asBoolean()) {
            return;
        }

        event.setCancelled(true);

        if (event.getItem().getAmount() != 1 && event.getPlayer().getInventory().firstEmpty() == -1) {
            return;
        }

        Group group = instance.getGroupManager().getValueUnsafe(clicked.getNBTObject("group").asString());
        int rate = ThreadLocalRandom.current().nextInt(clicked.getNBTObject("min-rate").asInt(), clicked.getNBTObject("max-rate").asInt());

        useItem(event);
        event.getPlayer().getInventory().addItem(instance.getSpecialItems().getDust(group, null, rate, false));
    }
}
