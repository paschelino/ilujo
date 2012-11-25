package de.cosmicsand.webtools.path;

import static java.lang.String.format;
import static java.util.Arrays.asList;
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

    public final String rawPath;

    private List<PathAtom> atoms;

    public Path(String rawPath) {
        this.rawPath = isEmpty(rawPath) ? "/" : rawPath;
        if (!SYNTAX_PATTERN.matcher(this.rawPath).matches())
            throw new URLPathException(format(ERR_MESS_SYNTAX_PATTERN, rawPath));
    }

    public Path() {
        this("");
    }

    public Path(PathAtom... pathAtoms) {
        rawPath = buildRawPath(pathAtoms);
        atoms = asList(pathAtoms);
    }

    private String buildRawPath(PathAtom... pathAtoms) {
        StringBuilder pathBuilder = new StringBuilder();
        for (PathAtom atom : pathAtoms)
            pathBuilder.append(atom.getOuterName());
        return pathBuilder.toString();
    }

    public Path(Path path) {
        if (path == null)
            rawPath = ROOT.rawPath;
        else
            rawPath = path.rawPath;
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

    public Path add(Path path) {
        Path helperPath = new Path(path);
        StringBuilder resultPath = new StringBuilder(this.rawPath);
        resultPath.append(helperPath.rawPath);
        cleanupObsoleteSlashes(resultPath);
        return new Path(resultPath.toString());
    }

    private void cleanupObsoleteSlashes(StringBuilder dirtyPath) {
        if (dirtyPath.length() > 1 && dirtyPath.charAt(1) == '/')
            dirtyPath.replace(0, 1, "");
    }

    public Path merge(Path path) {
        if (!this.equals(path))
            this.add(path);
        return this;
    }
}
