package de.cosmicsand.webtools.path.test;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import de.cosmicsand.webtools.path.Path;

public class PathMatcher extends TypeSafeMatcher<Path> {

    private final Path expectedPath;

    public PathMatcher(Path expectedPath) {
        this.expectedPath = expectedPath;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("the identical path as <" + expectedPath.rawPath + ">");
    }

    @Override
    public boolean matchesSafely(Path path) {
        return expectedPath.equals(path);
    }

    @Factory
    public static PathMatcher theIdenticalPathAs(Path expectedPath) {
        return new PathMatcher(expectedPath);
    }

    @Factory
    public static PathMatcher theIdenticalPathAs(String expectedPath) {
        return new PathMatcher(new Path(expectedPath));
    }

    @Factory
    public static PathMatcher theIdenticalPathAs(PathMatcher pathMatcher) {
        return pathMatcher;
    }

    @Factory
    public static PathMatcher root() {
        return new PathMatcher(Path.ROOT);
    }

}
