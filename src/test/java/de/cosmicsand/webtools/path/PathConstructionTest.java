package de.cosmicsand.webtools.path;

import static de.cosmicsand.webtools.path.test.PathMatcher.root;
import static de.cosmicsand.webtools.path.test.PathMatcher.theIdenticalPathAs;
import static java.lang.Math.random;
import static java.lang.reflect.Modifier.isFinal;
import static java.lang.reflect.Modifier.isPublic;
import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Collections;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PathConstructionTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private static final String WRONG_PATH_START_WITH_RANDOM = "wrongpathstartwithrandompart" + random();

    @Test
    public void whenCreatedWithASlash_then_itTransformsToASlash() {
        assertThat(new Path("/").toString(), is("/"));
    }

    @Test
    public void whenNotRootAndNotStartingWithASlash_then_throwAURLPathException() {
        thrown.expect(URLPathException.class);
        thrown.expectMessage("If not empty the given path is required to follow the pattern '/your/path'. Current value: '"
                + PathConstructionTest.WRONG_PATH_START_WITH_RANDOM + "'");
        new Path(PathConstructionTest.WRONG_PATH_START_WITH_RANDOM);
    }

    @Test
    public void whenCreatedWithOnePathAtom_then_itKnowsIt() {
        String rawPathAtom = "/one" + random();
        assertThat(new Path(rawPathAtom).getAtoms(), is(asList(new PathAtom(rawPathAtom))));
    }

    @Test
    public void whenCreatedWithSomePathAtoms_then_itKnowsThem() {
        String rawAtom1 = "/one", rawAtom2 = "/two", rawAtom3 = "/three";
        assertThat(new Path(rawAtom1 + rawAtom2 + rawAtom3).getAtoms(),
                is(asList(new PathAtom(rawAtom1), new PathAtom(rawAtom2), new PathAtom(rawAtom3))));
    }

    @Test
    public void whenCreatedWithAPathAtom_then_itFormsThePath() {
        PathAtom pathAtom = new PathAtom("/simple");
        Path path = new Path(pathAtom);
        assertThat(path, is(new Path("/simple")));
        assertThat(path.getAtoms(), is(asList(pathAtom)));
    }

    @Test
    public void whenCreatedWithSeveralPathAtom_then_theyFormThePath() {
        PathAtom pathAtom1 = new PathAtom("/atom1");
        PathAtom pathAtom2 = new PathAtom("/atom2");
        Path path = new Path(pathAtom1, pathAtom2);
        assertThat(path, is(new Path("/atom1/atom2")));
        assertThat(path.getAtoms(), is(asList(pathAtom1, pathAtom2)));
    }

    @Test
    public void whenCreatedWithANullPath_then_itIsThatPath() {
        assertThat(new Path(new Path()), is(new Path()));
    }

    @Test
    public void whenCreatedWithNullAsPath_then_itTreatsItAsNullPath() {
        assertThat(new Path((Path) null), is(new Path()));
    }

    @Test
    public void whenCreatedWithANotNullPath_then_itIsThatPath() {
        assertThat(new Path(new Path("/notnull")), is(new Path("/notnull")));
    }

    @Test
    public void whenCreatedWithAnEmptyPathAtomList_then_itIsTheRootPath() {
        assertThat(new Path(Collections.<PathAtom> emptyList()), is(theIdenticalPathAs(root())));
    }

    @Test
    public void whenCreatedWithANonEmptyPathAtomList_then_itFormsTheRelatedPath() {
        assertThat(new Path(asList(new PathAtom("/one"), new PathAtom("/two"))), is(theIdenticalPathAs("/one/two")));
    }

    @Test
    public void theRawPathIsPublicAndFinal() throws SecurityException, NoSuchFieldException {
        int modifiers = Path.class.getDeclaredField("rawPath").getModifiers();
        assertTrue(isFinal(modifiers));
        assertTrue(isPublic(modifiers));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void thePathAtomsListOfAnEmptyPathIsRequiredNotToBeModifiable() {
        new Path().getAtoms().add(new PathAtom("/toomuch"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void thePathAtomsListOfAnNonEmptyPathIsRequiredNotToBeModifiable() {
        new Path("/notemptyt").getAtoms().add(new PathAtom("/toomuch"));
    }

}
