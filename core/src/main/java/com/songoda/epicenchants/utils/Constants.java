package com.songoda.epicenchants.utils;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.UUID;

public interface Constants {
    Multimap<UUID, UUID> MONSTER_MAP = HashMultimap.create();
}
