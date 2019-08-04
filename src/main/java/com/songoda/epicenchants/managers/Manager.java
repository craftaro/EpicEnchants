package com.songoda.epicenchants.managers;

import com.songoda.epicenchants.EpicEnchants;

import java.util.*;

public abstract class Manager<K, V> {

    final EpicEnchants instance;
    private final Map<K, V> map;

    public Manager(EpicEnchants instance) {
        this.instance = instance;
        this.map = new HashMap<>();
    }

    public Optional<V> getValue(K key) {
        return Optional.ofNullable(map.get(key));
    }

    public void add(K key, V value) {
        map.put(key, value);
    }

    public V getValueUnsafe(K key) {
        return getValue(key).orElse(null);
    }

    public Collection<V> getValues() {
        return Collections.unmodifiableCollection(map.values());
    }

    public Collection<K> getKeys() {
        return Collections.unmodifiableCollection(map.keySet());
    }

    public void clear() {
        map.clear();
    }

}
