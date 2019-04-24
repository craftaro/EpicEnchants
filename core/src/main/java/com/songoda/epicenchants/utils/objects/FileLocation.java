package com.songoda.epicenchants.utils.objects;

import lombok.Getter;

public class FileLocation {
    @Getter private final boolean required, versionDependent;
    @Getter private final String path;

    private FileLocation(String path, boolean required, boolean versionDependent) {
        this.required = required;
        this.versionDependent = versionDependent;
        this.path = path;
    }

    public static FileLocation of(String path, boolean required) {
        return new FileLocation(path, required, false);
    }

    public static FileLocation of(String path, boolean required, boolean versionDependent) {
        return new FileLocation(path, required, versionDependent);
    }

    public String getResourcePath(String dir) {
        return (versionDependent ? "version-dependent/" + dir + "/" : "") + path;
    }

    public boolean isDirectory() {
        return path.endsWith("/");
    }
}
