package de.cosmicsand.webtools.path;

import junitx.extensions.ComparabilityTestCase;

public class PathAtomComparabilityTest extends ComparabilityTestCase {

    public PathAtomComparabilityTest(String name) {
        super(name);
    }

    @Override
    protected Comparable<PathAtom> createEqualInstance() throws Exception {
        return new PathAtom("equal1");
    }

    @Override
    protected Comparable<PathAtom> createGreaterInstance() throws Exception {
        return new PathAtom("equal2");
    }

    @Override
    protected Comparable<PathAtom> createLessInstance() throws Exception {
        return new PathAtom("equal0");
    }

}
