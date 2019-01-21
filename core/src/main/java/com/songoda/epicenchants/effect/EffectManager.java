package com.songoda.epicenchants.effect;

import org.bukkit.configuration.ConfigurationSection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static com.google.common.base.CaseFormat.UPPER_CAMEL;
import static com.google.common.base.CaseFormat.UPPER_UNDERSCORE;

public class EffectManager {

    public Optional<EffectExecutor> getEffect(ConfigurationSection section) {
        try {
            Class<?> clazz = Class.forName(UPPER_UNDERSCORE.to(UPPER_CAMEL, section.getName().toLowerCase()));
            Constructor<?> constructor = clazz.getConstructor(ConfigurationSection.class);
            Object object = constructor.newInstance(section);
            return Optional.of((EffectExecutor) object);
        } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | ClassCastException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
