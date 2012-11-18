package de.cosmicsand.webtools.path;

import static java.lang.String.format;
import static java.util.Collections.emptyList;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang.StringUtils.isEmpty;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Path implements Comparable<Path> {

    // needs to be defined before the very first constructor invocation
    protected static final Pattern SYNTAX_PATTERN = compile("(/[^/]*)*");

    public static final Path ROOT = new Path();

    private static final String ERR_MESS_SYNTAX_PATTERN = "If not empty the given path is required to follow the pattern '/your/path'. Current value: '%s'";

    private final String rawPath;

    private List<PathAtom> atoms;

    public Path(String rawPath) {
        this.rawPath = isEmpty(rawPath) ? "/" : rawPath;
        if (!SYNTAX_PATTERN.matcher(this.rawPath).matches())
            throw new URLPathException(format(ERR_MESS_SYNTAX_PATTERN, rawPath));
    }

    public Path() {
        this("");
    }

    public Path(PathAtom pathAtom) {
        rawPath = pathAtom.getOuterName();
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
        return rawPath.compareTo(other.rawPath);
    }

    public List<PathAtom> getAtoms() {
        if (ROOT.equals(this))
            atoms = emptyList();
        if (atoms == null)
            extractAtoms();
        return atoms;
    }

    private void extractAtoms() {
        atoms = new ArrayList<PathAtom>();
        String[] split = rawPath.substring(1).split("/");
        for (String rawAtom : split)
            atoms.add(atoms.size(), new PathAtom(rawAtom));
    }
}
