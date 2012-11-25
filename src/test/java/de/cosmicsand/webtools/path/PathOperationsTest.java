package de.cosmicsand.webtools.path;

import static java.util.Arrays.asList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.List;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PathOperationsTest {

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
    public void whenItIsTheRoot_then_thereAreNoPathAtoms() {
        assertThat(new Path().getAtoms(), is(Collections.<PathAtom> emptyList()));
    }

    @Test
    public void givenIAddTwoNullPaths_then_itGivesANullPath() {
        Path path = initPath();
        path.append(new Path());
        assertResultPathCorrectness(path, Path.ROOT, Collections.<PathAtom> emptyList());
    }

    @Test
    public void givenIAddANotNullPathToANullPath_then_itGivesTheNotNullPath() {
        Path path = initPath();
        path.append(new Path("/notnull"));
        assertResultPathCorrectness(path, new Path("/notnull"), asList(new PathAtom("notnull")));
    }

    @Test
    public void givenIAddANullPathToANotNullPath_then_itGivesTheNotNullPath() {
        Path path = initPath("/notnull");
        path.append(new Path());
        assertResultPathCorrectness(path, new Path("/notnull"), asList(new PathAtom("notnull")));
    }

    @Test
    public void givenIAppendTwoComplexPaths_then_itGivesTheSyntacticallyCorrectSumOfItsParts() {
        Path path = initPath("/one/two");
        path.append(new Path("/three/four"));
        assertResultPathCorrectness(path,
                new Path("/one/two/three/four"),
                asList(new PathAtom("/one"), new PathAtom("/two"), new PathAtom("/three"),
                        new PathAtom("/four")));
    }

    @Test
    @Ignore
    public void givenICalculateTheIntersectionOfTwoNullPaths_then_itIsTheNullPath() {
        fail();
    }

    @Test
    public void givenIMergeTwoNullPaths_then_itGivesANullPath() {
        Path path = new Path();
        Path resultPath = path.merge(new Path());
        assertResultPathCorrectness(resultPath, new Path(), Collections.<PathAtom> emptyList());
    }

    @Test
    public void givenIMergeANullPathWithANotNullPath_then_itGivesTheNotNullPath() {
        Path path = initPath();
        path.merge(new Path("/notnull"));
        assertResultPathCorrectness(path, new Path("/notnull"), asList(new PathAtom("notnull")));
    }

    @Test
    public void givenIMergeTwoEqualNotNullPaths_then_itGivesTheSamePath() {
        Path path = initPath("/equal");
        path.merge(new Path("/equal"));
        assertResultPathCorrectness(path, new Path("/equal"), asList(new PathAtom("equal")));
    }

    private Path initPath() {
        return initPath(null);
    }

    private Path initPath(String rawPath) {
        Path path = new Path(rawPath);
        path.getAtoms(); // trigger lazy initialization
        return path;
    }

    private void assertResultPathCorrectness(Path path, Path expectedPath, List<PathAtom> expectedPathAtoms) {
        assertThat(path, is(expectedPath));
        assertThat(path.getAtoms(), is(expectedPathAtoms));
    }
}
