package de.cosmicsand.webtools.path;

import static java.lang.Integer.valueOf;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.regex.Pattern.compile;
import static org.apache.commons.lang.StringUtils.isEmpty;

import java.util.*;
import java.util.regex.Pattern;

public class Path implements Comparable<Path> {

    // needs to be declared and initialized before the very first constructor invocation; see Path#ROOT
    static final Pattern SYNTAX_PATTERN = compile("(/[^/]*)*");

    public static final Path ROOT = new Path(); // very first constructor invocation; see Path#SYNTAX_PATTERN

    private static final String ERR_MESS_SYNTAX_TEMPLATE = "If not empty the given path is required to follow the pattern '/your/path'. Current value: '%s'";

    public final String rawPath;

    private List<PathAtom> atoms;

    static final String ERR_MESS_PATH_ATOMS_INDEX_OUT_OF_BOUNDS = "The requested path atom %s index %s exceeds the atom list's index boundaries (min 0, max %s).";

    public Path(String rawPath) {
        this.rawPath = isEmpty(rawPath) ? "/" : rawPath;
        if (!SYNTAX_PATTERN.matcher(this.rawPath).matches())
            throw new URLPathException(format(ERR_MESS_SYNTAX_TEMPLATE, rawPath));
    }

    public Path() {
        this("");
    }

    public Path(PathAtom... pathAtoms) {
        rawPath = buildRawPath(pathAtoms);
        atoms = asList(pathAtoms);
    }

    private String buildRawPath(PathAtom... pathAtoms) {
        return buildRawPath(asList(pathAtoms));
    }

    private String buildRawPath(Collection<PathAtom> pathAtoms) {
        StringBuilder pathBuilder = new StringBuilder();
        for (PathAtom atom : pathAtoms)
            pathBuilder.append(atom.getOuterName());
        if (pathBuilder.length() == 0)
            return ROOT.rawPath;
        return pathBuilder.toString();
    }

    public Path(Path path) {
        if (path == null)
            rawPath = ROOT.rawPath;
        else
            rawPath = path.rawPath;
    }


    public Path(Collection<PathAtom> emptyList) {
        rawPath = buildRawPath(emptyList);
        atoms = new ArrayList<>(emptyList);
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
        List<PathAtom> atomsHelper = new ArrayList<>();
        String[] split = rawPath.substring(1).split("/");
        for (String rawAtom : split)
            atomsHelper.add(atomsHelper.size(), new PathAtom(rawAtom));
        atoms = Collections.unmodifiableList(atomsHelper);
    }

    public Path add(Path... paths) {
        if(paths == null)
            return new Path(this);
        StringBuilder resultPathBuilder = new StringBuilder(this.rawPath);
        for(Path path: paths){
            resultPathBuilder.append(new Path(path).rawPath);
            cleanupObsoleteSlashes(resultPathBuilder);
        }
        return new Path(resultPathBuilder.toString());
    }

    public Path add(String rawPath) {
        return add(new Path(rawPath));
    }

    public Path add(PathAtom... pathAtoms) {
        return add(new Path(pathAtoms));
    }

    public Path add(Collection<PathAtom> pathAtoms) {
        return add(new Path(pathAtoms));
    }

    private void cleanupObsoleteSlashes(StringBuilder dirtyPathBuilder) {
        if (dirtyPathBuilder.length() < 2)
            return;
        if (dirtyPathBuilder.charAt(1) == '/')
            deleteCharAt(dirtyPathBuilder, 0);
        int lastCharIndex = dirtyPathBuilder.length() - 1;
        if (dirtyPathBuilder.charAt(lastCharIndex) == '/')
            deleteCharAt(dirtyPathBuilder, lastCharIndex);
    }

    private void deleteCharAt(StringBuilder dirtyPathBuilder, int charPosition) {
        dirtyPathBuilder.replace(charPosition, charPosition + 1, "");
    }

    public Path merge(Path path) {
        Path resultPath = this;
        if (!this.equals(path))
            resultPath = this.add(path);
        return resultPath;
    }

    public Path intersection(Path otherPath) {
        if (ROOT.equals(otherPath))
            return ROOT;
        if (!this.equals(otherPath))
            return buildIntersection(otherPath);
        return this;
    }

    private Path buildIntersection(Path otherPath) {
        PathAtom firstAtom = otherPath.getAtoms().get(0);
        Path subpath = otherPath.getSubpath(0, this.getPathAtomCount() - this.indexOf(firstAtom));
        if (this.contains(subpath))
            return subpath;
        return ROOT;
    }

    public Boolean contains(Path other) {
        List<PathAtom> othersAtoms = new Path(other).getAtoms();
        if (othersAtoms.size() > 0)
            return checkContainmentForNotNullPath(othersAtoms, this.indexOf(othersAtoms.get(0)));
        return Boolean.TRUE;
    }

    private Boolean checkContainmentForNotNullPath(List<PathAtom> othersAtoms, Integer othersFirstAtomIndex) {
        Boolean contained = isProbablyInside(othersAtoms, othersFirstAtomIndex);
        for (
                int thisIndex = othersFirstAtomIndex + 1, othersIndex = 1;
                contained && thisIndex < this.getAtoms().size() && othersIndex < othersAtoms.size();
                thisIndex++, othersIndex++
            )
            contained = this.getAtoms().get(thisIndex).equals(othersAtoms.get(othersIndex));
        return contained;
    }

    private Boolean isProbablyInside(List<PathAtom> othersAtoms, Integer othersFirstAtomIndex) {
        return !(othersFirstAtomIndex.equals(valueOf(-1))
                || (this.getAtoms().size() - othersFirstAtomIndex < othersAtoms.size()));
    }

    public Boolean containsAtom(PathAtom pathAtom) {
        return !this.indexOf(pathAtom).equals(valueOf(-1));
    }

    public Integer indexOf(PathAtom pathAtom) {
        return valueOf(this.getAtoms().indexOf(pathAtom));
    }

    public Integer getPathAtomCount() {
        return this.getAtoms().size();
    }

    public Path getSubpath(Integer beginIndex, Integer endIndex) {
        checkIndexBounds(beginIndex, endIndex);
        return buildSubpath(beginIndex, endIndex);
    }

    private void checkIndexBounds(Integer beginIndex, Integer endIndex) {
        if (beginIndex < 0 || beginIndex >= getPathAtomCount())
            throwIndexError(beginIndex, "begin");
        if (endIndex < 0 || endIndex > getPathAtomCount())
            throwIndexError(endIndex, "end");
    }

    private void throwIndexError(Integer index, String errorType) {
        throw new PathAtomIndexOutOfBoundsException(format(ERR_MESS_PATH_ATOMS_INDEX_OUT_OF_BOUNDS, errorType,
                index, this.getPathAtomCount() - 1));
    }

    private Path buildSubpath(Integer beginIndex, Integer endIndex) {
        if (beginIndex.equals(endIndex))
            return ROOT;
        ArrayList<PathAtom> subpath = new ArrayList<>();
        for (int indexCursor = beginIndex; indexCursor < endIndex; indexCursor++)
            subpath.add(this.getAtoms().get(indexCursor));
        return new Path(subpath);
    }

    public Path remove(Path path) {
        if (this.contains(path) && !this.equals(path) && !ROOT.equals(path)) {
            int beginIndex = this.indexOf(path.getAtoms().get(0));
            int endIndex = beginIndex + path.getPathAtomCount();
            return pathFromAtomsAroundIndexes(beginIndex, endIndex);
        }
        return this;
    }

    private Path pathFromAtomsAroundIndexes(int beginIndex, int endIndex) {
        ArrayList<PathAtom> newPath = new ArrayList<>();
        int index;
        for (index = 0; index < beginIndex; index++)
            newPath.add(this.getAtoms().get(index));
        for (index = endIndex; index < this.getPathAtomCount(); index++)
            newPath.add(this.getAtoms().get(index));
        return new Path(newPath);
    }

}
