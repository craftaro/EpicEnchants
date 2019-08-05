package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.EpicEnchants;
import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.enums.TriggerType;
import com.songoda.epicenchants.utils.single.RomanNumber;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.songoda.epicenchants.utils.single.GeneralUtils.color;

public class Enchant {
    private String identifier;
    private Group group;
    private int maxLevel;
    private Set<String> conflict;
    private Set<Material> itemWhitelist;
    private Set<EffectExecutor> effectExecutors;
    private List<String> description;
    private String format;
    @Nullable private BookItem bookItem;

    Enchant(String identifier, Group group, int maxLevel, Set<String> conflict, Set<Material> itemWhitelist, Set<EffectExecutor> effectExecutors, List<String> description, String format, BookItem bookItem) {
        this.identifier = identifier;
        this.group = group;
        this.maxLevel = maxLevel;
        this.conflict = conflict;
        this.itemWhitelist = itemWhitelist;
        this.effectExecutors = effectExecutors;
        this.description = description;
        this.format = format;
        this.bookItem = bookItem;
    }

    public static EnchantBuilder builder() {
        return new EnchantBuilder();
    }

    public void onAction(@NotNull Player user, @Nullable LivingEntity opponent, Event event, int level, TriggerType triggerType, EventType eventType) {
        effectExecutors.forEach(effect -> effect.testAndRun(user, opponent, level, triggerType, event, eventType));
    }

    public BookItem getBook() {
        return bookItem != null ? bookItem : group.getBookItem();
    }

    public String getFormat(int level, boolean roman) {
        String output = format.isEmpty() ? group.getFormat() : format;

        output = output
                .replace("{level}", "" + (roman ? RomanNumber.toRoman(level) : level))
                .replace("{enchant}", "" + identifier)
                .replace("{group_color}", "" + group.getColor());

        return color(output);
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Group getGroup() {
        return this.group;
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public Set<String> getConflict() {
        return this.conflict;
    }

    public Set<Material> getItemWhitelist() {
        return new HashSet<>(this.itemWhitelist);
    }

    public Set<EffectExecutor> getEffectExecutors() {
        return new HashSet<>(this.effectExecutors);
    }

    public List<String> getDescription() {
        return this.description;
    }

    public String getFormat() {
        return this.format;
    }

    @Nullable
    public BookItem getBookItem() {
        return this.bookItem;
    }

    public static class EnchantBuilder {
        private String identifier;
        private Group group;
        private int maxLevel;
        private Set<String> conflict;
        private Set<Material> itemWhitelist;
        private Set<EffectExecutor> effectExecutors;
        private List<String> description;
        private String format;
        private BookItem bookItem;

        EnchantBuilder() {
        }

        public Enchant.EnchantBuilder identifier(String identifier) {
            this.identifier = identifier;
            return this;
        }

        public Enchant.EnchantBuilder group(Group group) {
            this.group = group;
            return this;
        }

        public Enchant.EnchantBuilder maxLevel(int maxLevel) {
            this.maxLevel = maxLevel;
            return this;
        }

        public Enchant.EnchantBuilder conflict(Set<String> conflict) {
            this.conflict = conflict;
            return this;
        }

        public Enchant.EnchantBuilder itemWhitelist(Set<Material> itemWhitelist) {
            this.itemWhitelist = itemWhitelist;
            return this;
        }

        public Enchant.EnchantBuilder effectExecutors(Set<EffectExecutor> effectExecutors) {
            this.effectExecutors = effectExecutors;
            return this;
        }

        public Enchant.EnchantBuilder description(List<String> description) {
            this.description = description;
            return this;
        }

        public Enchant.EnchantBuilder format(String format) {
            this.format = format;
            return this;
        }

        public Enchant.EnchantBuilder bookItem(BookItem bookItem) {
            this.bookItem = bookItem;
            return this;
        }

        public Enchant build() {
            return new Enchant(identifier, group, maxLevel, conflict, itemWhitelist, effectExecutors, description, format, bookItem);
        }

        public String toString() {
            return "Enchant.EnchantBuilder(identifier=" + this.identifier + ", group=" + this.group + ", maxLevel=" + this.maxLevel + ", conflict=" + this.conflict + ", itemWhitelist=" + this.itemWhitelist + ", effectExecutors=" + this.effectExecutors + ", description=" + this.description + ", format=" + this.format + ", bookItem=" + this.bookItem + ")";
        }
    }
}
