package de.cosmicsand.webtools.path;

import junitx.extensions.EqualsHashCodeTestCase;

public class PathAtomEqualityTest extends EqualsHashCodeTestCase {

    public PathAtomEqualityTest(String name) {
        super(name);
    }

    @Override
    protected PathAtom createInstance() throws Exception {
        return new PathAtom("/equal");
    }

    @Override
    protected PathAtom createNotEqualInstance() throws Exception {
        return new PathAtom("/different");
    }

}
