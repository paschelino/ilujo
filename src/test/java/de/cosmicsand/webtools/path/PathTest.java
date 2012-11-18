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
    public void givenIAddTwoNullPaths_then_itGivesANullPath() {
        Path path = new Path();
        path.append(new Path());
        assertThat(path, is(Path.ROOT));
    }

    @Test
    public void givenIAddANotNullPathToANullPath_then_itGivesTheNotNullPath() {
        Path path = new Path();
        path.getAtoms(); // trigger lazy initialization
        path.append(new Path("/notnull"));
        assertThat(path, is(new Path("/notnull")));
        assertThat(path.getAtoms(), is(asList(new PathAtom("notnull"))));
    }

    @Test
    public void givenIAddANullPathToANotNullPath_then_itGivesTheNotNullPath() {
        Path path = new Path("/notnull");
        path.getAtoms(); // trigger lazy initialization
        path.append(new Path());
        assertThat(path, is(new Path("/notnull")));
        assertThat(path.getAtoms(), is(asList(new PathAtom("notnull"))));
    }

    @Test
    public void givenIAppendTwoComplexPaths_then_itGivesTheSyntacticallyCorrectSumOfItsParts() {
        Path path = new Path("/one/two");
        path.getAtoms(); // trigger lazy initialization
        path.append(new Path("/three/four"));
        assertThat(path, is(new Path("/one/two/three/four")));
        assertThat(path.getAtoms(),
                is(asList(new PathAtom("/one"), new PathAtom("/two"), new PathAtom("/three"), new PathAtom("/four"))));
    }
}
