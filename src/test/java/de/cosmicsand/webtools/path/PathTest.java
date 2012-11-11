package de.cosmicsand.webtools.path;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PathTest {
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

}
