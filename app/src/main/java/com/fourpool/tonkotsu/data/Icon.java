package com.fourpool.tonkotsu.data;

public class Icon {
    private final String prefix;
    private final String suffix;

    public Icon(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}
