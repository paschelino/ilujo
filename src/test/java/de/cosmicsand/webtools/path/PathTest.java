package de.cosmicsand.webtools.path;

import static java.lang.Math.random;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PathTest {

    private static final String WRONG_PATH_START_WITH_RANDOM = "wrongpathstartwithrandompart" + random();
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void whenCreatedWithEmptyConstructorInformation_then_itTransformsToTheEmptyString() {
        assertThat(new Path().toString(), is(""));
        assertThat(new Path((String) null).toString(), is(""));
        assertThat(new Path("").toString(), is(""));
    }

    @Test
    public void itProvidesAnEmptyPathObject() {
        assertTrue(Path.EMPTY_PATH instanceof Path);
        assertThat(Path.EMPTY_PATH.toString(), is(""));
    }

    @Test
    public void whenCreatedWithASlash_then_itTransformsToASlash() {
        assertThat(new Path("/").toString(), is("/"));
    }

    @Test
    public void whenNotEmptyAndNotStartingWithASlash_then_throwAURLPathException() {
        thrown.expect(URLPathException.class);
        thrown.expectMessage("If not empty the given path is required to follow the pattern '/your/path'. Current value: '"
                + WRONG_PATH_START_WITH_RANDOM + "'");
        new Path(WRONG_PATH_START_WITH_RANDOM);
    }

}
