package de.cosmicsand.webtools.path;

import static java.lang.Integer.valueOf;
import static java.lang.String.format;
import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathAtom {

    protected static final Pattern PATH_ATOM_PATTERN = compile("\\s*/?([^/\\s]+)/?\\s*");

    private static final String SYNTAX_ERR_MESS = "A path atom may not be null or empty and needs to be of the form '/innerName', 'innerName' or 'innerName/'!";

    private final String innerName;

    public PathAtom(String rawPathAtom) {
        final Matcher matcher = PATH_ATOM_PATTERN.matcher(rawPathAtom == null ? "" : rawPathAtom);
        if (!matcher.matches())
            throw new URLPathException(SYNTAX_ERR_MESS);
        this.innerName = matcher.group(1);
    }

    public String getInnerName() {
        return innerName;
    }

    @Override
    public String toString() {
        return getOuterName();
    }

    public Integer outerLength() {
        return valueOf(getOuterName().length());
    }

    public Integer innerLength() {
        return valueOf(innerName.length());
    }

    public String getOuterName() {
        return format("/%s", innerName);
    }

}
