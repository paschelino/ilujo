package de.cosmicsand.webtools.path;

import junitx.extensions.ComparabilityTestCase;

public class PathComparabilityTest extends ComparabilityTestCase {

    public PathComparabilityTest(String name) {
        super(name);
    }

    @Override
    protected Comparable<Path> createEqualInstance() throws Exception {
        return new Path("/b");
    }

    @Override
    protected Comparable<Path> createGreaterInstance() throws Exception {
        return new Path("/c");
    }

    @Override
    protected Comparable<Path> createLessInstance() throws Exception {
        return new Path("/a");
    }

}
