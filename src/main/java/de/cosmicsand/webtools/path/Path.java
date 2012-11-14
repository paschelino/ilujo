package de.cosmicsand.webtools.path;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang.StringUtils.isEmpty;
import static org.apache.commons.lang.StringUtils.isNotEmpty;

import java.util.regex.Pattern;

public class Path implements Comparable<Path> {

    public static final Pattern SYNTAX_PATTERN = compile("(/[^/]*)*");
    public static final Path EMPTY_PATH = new Path();

    private static final String ERR_MESS_SYNTAX_PATTERN = "If not empty the given path is required to follow the pattern '/your/path'. Current value: '%s'";

    private final String rawPath;

    public Path(String rawPath) {
        this.rawPath = isEmpty(rawPath) ? "" : rawPath;
        if (!SYNTAX_PATTERN.matcher(this.rawPath).matches())
            throw new URLPathException(format(ERR_MESS_SYNTAX_PATTERN, rawPath));
    }

    public Path() {
        this("");
    }

    @Override
    public String toString() {
        return rawPath;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Path)
            return rawPath.equals(((Path) obj).rawPath);
        return false;
    }

    @Override
    public int hashCode() {
        return rawPath.hashCode();
    }

    @Override
    public int compareTo(Path other) {
        if (isEmpty(rawPath) && isEmpty(other.rawPath))
            return 0;
        else if (isNotEmpty(rawPath) && isEmpty(other.rawPath))
            return 1;
        else if (isEmpty(rawPath) && isNotEmpty(other.rawPath))
            return -1;
        return rawPath.compareTo(other.rawPath);
    }
}
