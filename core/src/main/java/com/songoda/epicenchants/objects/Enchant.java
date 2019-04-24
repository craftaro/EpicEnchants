package com.songoda.epicenchants.objects;

import com.songoda.epicenchants.effect.EffectExecutor;
import com.songoda.epicenchants.enums.EventType;
import com.songoda.epicenchants.enums.TriggerType;
import com.songoda.epicenchants.utils.single.RomanNumber;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

import static com.songoda.epicenchants.utils.single.GeneralUtils.color;

@Builder
@Getter
public class Enchant {
    private String identifier;
    private Group group;
    private int maxLevel;
    private Set<String> conflict;
    //TODO: ISSUES
    private Set<Material> itemWhitelist;
    private Set<EffectExecutor> effectExecutors;
    private List<String> description;
    private String format;
    @Nullable private BookItem bookItem;

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
}
