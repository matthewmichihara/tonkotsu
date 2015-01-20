package com.fourpool.tonkotsu.data;

public class Photo {
    private final String prefix;
    private final String suffix;
    private final int width;
    private final int height;

    public Photo(String prefix, String suffix, int width, int height) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.width = width;
        this.height = height;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
