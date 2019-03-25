package com.songoda.epicenchants.utils.objects;

import lombok.Getter;

public class FileLocationObject {
    @Getter private final boolean required, versionDependent;
    @Getter private final String path;

    private FileLocationObject(String path, boolean required, boolean versionDependent) {
        this.required = required;
        this.versionDependent = versionDependent;
        this.path = path;
    }

    public static FileLocationObject of(String path, boolean required) {
        return new FileLocationObject(path, required, false);
    }

    public static FileLocationObject of(String path, boolean required, boolean versionDependent) {
        return new FileLocationObject(path, required, versionDependent);
    }

    public String getResourcePath(String dir) {
        return (versionDependent ? "version-dependent/" + dir + "/" : "") + path;
    }
}
