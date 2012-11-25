package de.cosmicsand.webtools.path;

import static de.cosmicsand.webtools.path.test.PathMatcher.theIdenticalPathAs;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Collections;

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
        path.add(new Path());
        assertThat(path, is(theIdenticalPathAs(Path.ROOT)));
    }

    @Test
    public void givenIAddANotNullPathToANullPath_then_itGivesTheNotNullPath() {
        Path path = initPath();
        path = path.add(new Path("/notnull"));
        assertThat(path, is(theIdenticalPathAs("/notnull")));
    }

    @Test
    public void givenIAddANullPathToANotNullPath_then_itGivesTheNotNullPath() {
        Path path = initPath("/notnull");
        path.add(new Path());
        assertThat(path, is(theIdenticalPathAs("/notnull")));
    }

    @Test
    public void givenIAddTwoComplexPaths_then_itGivesTheSyntacticallyCorrectSumOfItsParts() {
        Path path = initPath("/one/two");
        path = path.add(new Path("/three/four"));
        assertThat(path, is(theIdenticalPathAs("/one/two/three/four")));
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
        assertThat(resultPath, is(theIdenticalPathAs(new Path())));
    }

    @Test
    @Ignore
    public void givenIMergeANullPathWithANotNullPath_then_itGivesTheNotNullPath() {
        Path path = initPath();
        path.merge(new Path("/notnull"));
        assertThat(path, is(theIdenticalPathAs(new Path("/notnull"))));
    }

    @Test
    public void givenIMergeTwoEqualNotNullPaths_then_itGivesTheSamePath() {
        Path path = initPath("/equal");
        path.merge(new Path("/equal"));
        assertThat(path, is(theIdenticalPathAs(new Path("/equal"))));
    }

    private Path initPath() {
        return initPath(null);
    }

    private Path initPath(String rawPath) {
        Path path = new Path(rawPath);
        path.getAtoms(); // trigger lazy initialization
        return path;
    }
}
