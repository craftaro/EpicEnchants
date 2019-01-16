package com.songoda.epicenchants.managers;

import com.songoda.epicenchants.objects.Enchant;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class EnchantManager {
    private Map<String, Enchant> enchantMap;

    public EnchantManager() {
        this.enchantMap = new HashMap<>();
    }

    public Optional<Enchant> getEnchant(String identifier) {
        return Optional.ofNullable(enchantMap.get(identifier));
    }

    public void addEnchant(Enchant enchant) {
        enchantMap.put(enchant.getIdentifier(), enchant);
    }
}
