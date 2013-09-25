package de.cosmicsand.webtools.path;

import static de.cosmicsand.webtools.path.test.PathMatcher.root;
import static de.cosmicsand.webtools.path.test.PathMatcher.theIdenticalPathAs;
import static java.lang.String.format;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

//@RunWith(HierarchicalContextRunner.class)
public class PathOperationsTest {

    public static final Path PATH_ONE_TWO = new Path("/one/two");
    public static final String ONE_TWO_THREE_FOUR = "/one/two/three/four";
    public static final String ONE_TWO_THREE = "/one/two/three";
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    // add operation ///////////////////////////////////////////////////////////////////////////////////////////////////

    @Test
    public void givenIAdd_null_toAPath_then_thePathStaysTheSame() {
        assertThat(new Path("/path").add((Path) null), is(theIdenticalPathAs(new Path("/path"))));
    }

    @Test
    public void givenIAddTwoNullPaths_then_itGivesANullPath() {
        assertThat(Path.ROOT.add(Path.ROOT), is(theIdenticalPathAs(root())));
    }

    @Test
    public void givenIAddANotNullPathToANullPath_then_itGivesTheNotNullPath() {
        assertThat(Path.ROOT.add(new Path("/notnull")), is(theIdenticalPathAs("/notnull")));
    }

    @Test
    public void givenIAddANullPathToANotNullPath_then_itGivesTheNotNullPath() {
        assertThat(new Path("/notnull").add(Path.ROOT), is(theIdenticalPathAs("/notnull")));
    }

    @Test
    public void givenIAddTwoComplexPaths_then_itGivesTheSyntacticallyCorrectSumOfItsParts() {
        assertThat(PATH_ONE_TWO.add(new Path("/three/four")), is(theIdenticalPathAs(ONE_TWO_THREE_FOUR)));
    }

    @Test
    public void itShouldAcceptStringPathsForAdding() {
        assertThat(PATH_ONE_TWO.add("/three/four"), is(theIdenticalPathAs(ONE_TWO_THREE_FOUR)));
    }

    @Test
    public void itShouldAcceptASequenceOfVariableLengthOfPathAtomsForAdding() {
        assertThat(PATH_ONE_TWO.add(new PathAtom("three")), is(theIdenticalPathAs(ONE_TWO_THREE)));
        assertThat(PATH_ONE_TWO.add(new PathAtom("three"), new PathAtom("four")), is(theIdenticalPathAs(ONE_TWO_THREE_FOUR)));
    }

    @Test
    public void itShouldAcceptACollectionOfPathAtoms() {
        Collection<PathAtom> pathAtoms = Arrays.asList(new PathAtom("three"), new PathAtom("four"));
        assertThat(PATH_ONE_TWO.add(pathAtoms), is(theIdenticalPathAs(ONE_TWO_THREE_FOUR)));
    }

    @Test
    public void itShouldAcceptASequenceOfVariableLengthOfPathsForAdding() {
        assertThat(PATH_ONE_TWO.add(new Path("/three"), new Path("/four")), is(theIdenticalPathAs(ONE_TWO_THREE_FOUR)));
    }

    // indexOf(PathAtom) ///////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void givenAPathAtomIsNotContained_then_returnANegative_1() {
        assertThat(new Path().indexOf(new PathAtom("other")), is(-1));
    }

    @Test
    public void givenAPathAtomIsContainedAtPosition_0_then_return_0() {
        assertThat(new Path("/atom").indexOf(new PathAtom("atom")), is(0));
    }

    @Test
    public void givenAPathATomIsContainedSomewhereInThePath_then_returnItsPosition() {
        assertThat(new Path(ONE_TWO_THREE).indexOf(new PathAtom("two")), is(1));
    }

    // intersection ////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void givenICalculateTheIntersectionOfTwoNullPaths_then_itIsTheNullPath() {
        assertThat(new Path().intersection(new Path()), is(theIdenticalPathAs(root())));
    }

    @Test
    public void givenICalculateTheIntersectionOfTwoEqualPaths_then_itIsTheSamePath() {
        assertThat(new Path("/first").intersection(new Path("/first")), is(theIdenticalPathAs("/first")));
    }

    @Test
    public void givenICalculateTheIntersectionOfTwoIntersectingAndNotNullPaths_then_returnTheIntersection() {
        assertThat(new Path("/first/second").intersection(new Path("/second/third")), is(theIdenticalPathAs("/second")));
    }

    @Test
    public void givenICalculateTheIntersectionOfTwoTwiceMatchingPaths_then_returnRoot() {
        assertThat(new Path("/first/firstmatch/continued/secondmatch").intersection(new Path(
                "/firstmatch/different/secondmatch/tail")), is(theIdenticalPathAs(root())));
    }

    // remove //////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void ifIRemoveTheNullPath_then_theResultPathIsTheSame() {
        assertThat(new Path("/one").remove(new Path()), is(theIdenticalPathAs("/one")));
    }

    @Test
    public void ifITryToRemoveAPartNotContained_then_theResultPathIsTheSame() {
        assertThat(new Path("/one").remove(new Path("/other")), is(theIdenticalPathAs("/one")));
    }

    @Test
    public void itIsPossibleToRemoveAPathThatIsContained() {
        assertThat(new Path(ONE_TWO_THREE_FOUR).remove(new Path("/two/three")), is(theIdenticalPathAs("/one/four")));
    }

    // merge ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void givenIMergeTwoNullPaths_then_itGivesANullPath() {
        assertThat(new Path().merge(new Path()), is(theIdenticalPathAs(root())));
    }

    @Test
    public void givenIMergeANullPathWithANotNullPath_then_itGivesTheNotNullPath() {
        assertThat(new Path().merge(new Path("/notnull")), is(theIdenticalPathAs(new Path("/notnull"))));
    }

    @Test
    public void givenIMergeTwoEqualNotNullPaths_then_itGivesTheSamePath() {
        assertThat(new Path("/equal").merge(new Path("/equal")), is(theIdenticalPathAs(new Path("/equal"))));
    }

    @Test
    public void givenIMergeANotNullPathWithANullPath_then_itGivesTheNotNullPath() {
        assertThat(new Path("/notnull").merge(new Path()), is(theIdenticalPathAs("/notnull")));
    }

    @Test
    public void givenIMergeTwoPathsWithEmptyIntersection_then_aMergeIsEqualToAConcatenation() {
        assertThat(PATH_ONE_TWO.merge(new Path("/three/four")), is(theIdenticalPathAs(ONE_TWO_THREE_FOUR)));
    }

    // contains(Path) //////////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void theRootPathIsAllwaysContained() {
        assertThat(new Path().contains(Path.ROOT), is(true));
    }

    @Test
    public void aPathFragmentWhichIsContainedInTheRawPathStringButPointingOnADifferentResourceIsNotContained() {
        assertThat(new Path("/nothere").contains(new Path("/not")), is(false));
    }

    @Test
    public void nullIsTreatedLikeRootAndThusAllwaysContained() {
        assertThat(new Path().contains((Path) null), is(true));
    }

    @Test
    public void aPathContainsItself() {
        Path path = new Path("/identity");
        assertThat(path.contains(path), is(true));
    }

    @Test
    public void itKnowsIfAnotherPathIsContained() {
        assertThat(new Path("/longer/path/as/the/other").contains(new Path("/as/the")), is(true));
    }

    @Test
    public void givenTheOtherPathIntersectsWithThisButIsNotContained_then_returnFalse() {
        assertThat(new Path(ONE_TWO_THREE).contains(new Path("/two/three/four")), is(false));
    }

    @Test
    public void givenSomeOfTheOtherPathsAtomsAreContainedButThePathAsSuchIsNot_then_returnFalse() {
        assertThat(new Path(ONE_TWO_THREE_FOUR).contains(new Path("/two/notcontained/four")), is(false));
    }

    // containsAtom(Atom) //////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void aPathKnowsIfAnAtomIsContained() {
        assertThat(new Path("/contained").containsAtom(new PathAtom("/contained")), is(true));
        assertThat(new Path("/some/path").containsAtom(new PathAtom("/notcontained")), is(false));
    }

    // getPathAtomCount() //////////////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void itKnowsTheCountOfItsPathAtoms() {
        assertThat(new Path().getPathAtomCount(), is(0));
        assertThat(new Path("/one").getPathAtomCount(), is(1));
        assertThat(PATH_ONE_TWO.getPathAtomCount(), is(2));
    }

    // getSubpath(Integer,Integer) /////////////////////////////////////////////////////////////////////////////////////
    @Test
    public void itProvidesAPathAtomIndexOutOfBoundsException() {
        assertThat(new PathAtomIndexOutOfBoundsException(), is(instanceOf(RuntimeException.class)));
    }

    @Test
    public void givenITryToExtractASubpathWithABeginIndexLargerThanTheCountOfItsAtoms_then_throwAnException() {
        Path path = PATH_ONE_TWO;
        thrown.expect(PathAtomIndexOutOfBoundsException.class);
        thrown.expectMessage(format(Path.ERR_MESS_PATH_ATOMS_INDEX_OUT_OF_BOUNDS, "begin", path.getPathAtomCount(),
                path.getPathAtomCount() - 1));
        path.getSubpath(path.getPathAtomCount(), path.getPathAtomCount() + 1);
    }

    @Test
    public void givenITryToExtractASubpathWithABeginIndexLowerThanZero_then_throwAnException() {
        Path path = PATH_ONE_TWO;
        thrown.expect(PathAtomIndexOutOfBoundsException.class);
        thrown.expectMessage(format(Path.ERR_MESS_PATH_ATOMS_INDEX_OUT_OF_BOUNDS, "begin", -1,
                path.getPathAtomCount() - 1));
        path.getSubpath(-1, 1);
    }

    @Test
    public void givenITryToExtractASubpathWithAnEndIndexLargerThanTheCountOfItsAtoms_then_throwAnException() {
        Path path = PATH_ONE_TWO;
        thrown.expect(PathAtomIndexOutOfBoundsException.class);
        thrown.expectMessage(format(Path.ERR_MESS_PATH_ATOMS_INDEX_OUT_OF_BOUNDS, "end", path.getPathAtomCount() + 1,
                path.getPathAtomCount() - 1));
        path.getSubpath(path.getPathAtomCount() - 1, path.getPathAtomCount() + 1);
    }

    @Test
    public void givenITryToExtractASubpathWithAnEndIndexLowerThanZero_then_throwAnException() {
        Path path = PATH_ONE_TWO;
        thrown.expect(PathAtomIndexOutOfBoundsException.class);
        thrown.expectMessage(format(Path.ERR_MESS_PATH_ATOMS_INDEX_OUT_OF_BOUNDS, "end", -1, path.getPathAtomCount() - 1));
        path.getSubpath(0, -1);
    }

    @Test
    public void givenITryToExtractASubpathWithAnEndIndexEqualToTheBeginIndex_then_returnRoot() {
        Path path = PATH_ONE_TWO;
        assertThat(path.getSubpath(0, 0), is(theIdenticalPathAs(root())));
    }

    @Test
    public void givenITryToExtractTheFirstAtomAsSubpath_then_returnThatPath() {
        assertThat(PATH_ONE_TWO.getSubpath(0, 1), is(theIdenticalPathAs("/one")));
    }

    @Test
    public void givenITryToExtractALongerSubpath_then_returnThatPath() {
        assertThat(new Path(ONE_TWO_THREE_FOUR).getSubpath(1, 3), is(theIdenticalPathAs("/two/three")));
    }

}
