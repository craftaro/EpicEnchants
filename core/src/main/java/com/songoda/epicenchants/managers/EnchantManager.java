package com.songoda.epicenchants.managers;

import com.songoda.epicenchants.objects.Enchant;

import java.util.*;
import java.util.stream.Collectors;

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

    public Collection<Enchant> getEnchants(int tier) {
        return Collections.unmodifiableCollection(enchantMap.values().stream().filter(s -> s.getTier() == tier).collect(Collectors.toList()));
    }

    public Optional<Enchant> getRandomEnchant(int tier) {
        Collection<Enchant> tierList = getEnchants(tier);
        return tierList.stream().skip((int) (tierList.size() * Math.random())).findFirst();
    }

    public Collection<Enchant> getEnchants() {
        return Collections.unmodifiableCollection(enchantMap.values());
    }
}
