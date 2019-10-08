package com.songoda.epicenchants.objects;

public class Placeholder {
    private String placeholder;
    private Object toReplace;

    private Placeholder(String placeholder, Object toReplace) {
        this.placeholder = "{" + placeholder + "}";
        this.toReplace = toReplace;
    }

    public static Placeholder of(String placeholder, Object toReplace) {
        return new Placeholder(placeholder, toReplace);
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public Object getToReplace() {
        return toReplace;
    }
}
