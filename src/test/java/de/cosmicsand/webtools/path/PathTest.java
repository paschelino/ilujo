package de.cosmicsand.webtools.path;

import static java.lang.Math.random;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PathTest {

    private static final String WRONG_PATH_START_WITH_RANDOM = "wrongpathstartwithrandompart" + random();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void whenCreatedWithEmptyConstructorInformation_then_itTransformsToRoot() {
        assertThat(new Path().toString(), is("/"));
        assertThat(new Path((String) null).toString(), is("/"));
        assertThat(new Path("").toString(), is("/"));
    }

    @Test
    public void itProvidesARootPathObject() {
        assertThat(Path.ROOT, is(new Path()));
    }

    @Test
    public void whenCreatedWithASlash_then_itTransformsToASlash() {
        assertThat(new Path("/").toString(), is("/"));
    }

    @Test
    public void whenNotRootAndNotStartingWithASlash_then_throwAURLPathException() {
        thrown.expect(URLPathException.class);
        thrown.expectMessage("If not empty the given path is required to follow the pattern '/your/path'. Current value: '"
                + WRONG_PATH_START_WITH_RANDOM + "'");
        new Path(WRONG_PATH_START_WITH_RANDOM);
    }

    @Test
    public void whenItIsTheRoot_then_thereAreNoPathAtoms() {
        assertThat(new Path().getAtoms(), is(Collections.<PathAtom> emptyList()));
    }

    @Test
    public void whenCreatedWithOnePathAtom_then_itKnowsIt() {
        String rawPathAtom = "/one" + random();
        assertThat(new Path(rawPathAtom).getAtoms(), is(asList(new PathAtom(rawPathAtom))));
    }

    @Test
    public void whenCreatedWithSomePathAtoms_then_itKnowsThem() {
        String rawAtom1 = "/one", rawAtom2 = "/two", rawAtom3 = "/three";
        assertThat(new Path(rawAtom1 + rawAtom2 + rawAtom3).getAtoms(), is(asList(
                new PathAtom(rawAtom1),
                new PathAtom(rawAtom2),
                new PathAtom(rawAtom3)
                )));
    }

}
