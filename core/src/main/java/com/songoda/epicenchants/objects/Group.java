package com.songoda.epicenchants.objects;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Group {
    private String identifier;
    private String name;
    private String format;
    private String color;
    private String descriptionColor;
    private int slotsUsed;
    private BookItem bookItem;
    private int destroyRateMin, destroyRateMax, successRateMin, successRateMax;
    private int tinkererExp;
    private int order;
}
