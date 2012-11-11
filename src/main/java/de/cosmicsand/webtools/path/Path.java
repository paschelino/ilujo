package de.cosmicsand.webtools.path;

import static org.apache.commons.lang.StringUtils.isEmpty;

public class Path {
    public static final Path EMPTY_PATH = new Path();
    private final String rawPath;

    public Path(String rawPath) {
        this.rawPath = isEmpty(rawPath) ? "" : rawPath;
    }

    public Path() {
        this("");
    }

    @Override
    public String toString() {
        return rawPath;
    }
}
