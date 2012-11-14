package de.cosmicsand.webtools.path;

import junitx.extensions.EqualsHashCodeTestCase;

public class PathEqualityTest extends EqualsHashCodeTestCase {

    public PathEqualityTest(String name) {
        super(name);
    }

    @Override
    protected Path createInstance() throws Exception {
        return new Path("/an/equal/path");
    }

    @Override
    protected Path createNotEqualInstance() throws Exception {
        return new Path("/a/different/path");
    }

}
