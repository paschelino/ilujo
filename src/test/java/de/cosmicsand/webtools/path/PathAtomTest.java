package de.cosmicsand.webtools.path;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.regex.Matcher;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PathAtomTest {
    private static final String SYNTAX_ERR_MESS = "A path atom may not be null or empty and needs to be of the form '/innerName', 'innerName' or 'innerName/'!";
    private static final String SAMPLE_WHITE_SPACE = "\t\n\r ";
    private static final String RAW_ATOM_INNER = "rawAtom";
    private static final String RAW_ATOM = "/" + RAW_ATOM_INNER;
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void aPathAtomMayNotBeNull() {
        thrown.expect(URLPathException.class);
        thrown.expectMessage(SYNTAX_ERR_MESS);
        new PathAtom((String) null);
    }

    @Test
    public void aPathAtomMayNotBeEmpty() {
        thrown.expect(URLPathException.class);
        thrown.expectMessage(SYNTAX_ERR_MESS);
        new PathAtom("");
    }

    @Test
    public void aPathAtomMayBeTransformedToItsRawValue() {
        assertThat(new PathAtom(RAW_ATOM).getOuterName(), is(RAW_ATOM));
    }

    @Test
    public void aPathAtomKnowsItsInnerName() {
        assertThat(new PathAtom(RAW_ATOM).getInnerName(), is(RAW_ATOM_INNER));
    }

    @Test
    public void aPathAtomsInnerNameMayNotBeEmpty() {
        thrown.expect(URLPathException.class);
        thrown.expectMessage(SYNTAX_ERR_MESS);
        new PathAtom("/");
    }

    @Test
    public void aPathAtomsInnerNameMayNotBeWhiteSpace() {
        thrown.expect(URLPathException.class);
        thrown.expectMessage(SYNTAX_ERR_MESS);
        new PathAtom("/" + SAMPLE_WHITE_SPACE);
    }

    @Test
    public void aPathAtomKnowsItsOuterLengthWhichCorrespondsToItsRawValuesLength() {
        assertThat(new PathAtom(RAW_ATOM).outerLength(), is(RAW_ATOM.length()));
        String rawAtom = RAW_ATOM + "Longer";
        assertThat(new PathAtom(rawAtom).outerLength(), is(rawAtom.length()));
    }

    @Test
    public void aPathAtomKnowsItsInnerLength() {
        String rawAtom = RAW_ATOM;
        int expectedLength = rawAtom.length() - 1;
        assertThat(new PathAtom(rawAtom).innerLength(), is(expectedLength));
        rawAtom += "Longer";
        expectedLength = rawAtom.length() - 1;
        assertThat(new PathAtom(rawAtom).innerLength(), is(expectedLength));
    }

    @Test
    public void whenCreatedWithARawInnerName_then_itAddsA_slash_toFormTheOuterName() {
        assertThat(new PathAtom(RAW_ATOM_INNER).getOuterName(), is(RAW_ATOM));
    }

    @Test
    public void toString_isAnAliasFor_getOuterName() {
        PathAtom atom = new PathAtom(RAW_ATOM);
        assertThat(atom.toString(), is(atom.getOuterName()));
    }

    @Test
    public void whenCreatedWithLeadingOrTrailingWhiteSpace_theRawValueIsBeingTrimmed() {
        assertThat(new PathAtom(SAMPLE_WHITE_SPACE + RAW_ATOM + SAMPLE_WHITE_SPACE).getOuterName(), is(RAW_ATOM));
        assertThat(new PathAtom(SAMPLE_WHITE_SPACE + RAW_ATOM_INNER + SAMPLE_WHITE_SPACE).getOuterName(), is(RAW_ATOM));
    }

    @Test
    public void whenCreatedWithATrailing_slash_thenRemoveIt() {
        assertThat(new PathAtom(RAW_ATOM + "/").getOuterName(), is(RAW_ATOM));
    }

    @Test
    public void itProvidesARegexPatternToExtractTheInnerName() {
        Matcher matcher = PathAtom.PATH_ATOM_PATTERN.matcher(RAW_ATOM_INNER);
        assertTrue(matcher.matches());
        assertThat(matcher.group(1), is(RAW_ATOM_INNER));

        matcher = PathAtom.PATH_ATOM_PATTERN.matcher(RAW_ATOM);
        assertTrue(matcher.matches());
        assertThat(matcher.group(1), is(RAW_ATOM_INNER));

        matcher = PathAtom.PATH_ATOM_PATTERN.matcher(RAW_ATOM + "/");
        assertTrue(matcher.matches());
        assertThat(matcher.group(1), is(RAW_ATOM_INNER));

        matcher = PathAtom.PATH_ATOM_PATTERN.matcher("/");
        assertFalse(matcher.matches());

        matcher = PathAtom.PATH_ATOM_PATTERN.matcher("//");
        assertFalse(matcher.matches());

        matcher = PathAtom.PATH_ATOM_PATTERN.matcher("/a/a/");
        assertFalse(matcher.matches());

        matcher = PathAtom.PATH_ATOM_PATTERN.matcher(SAMPLE_WHITE_SPACE + RAW_ATOM + SAMPLE_WHITE_SPACE);
        assertTrue(matcher.matches());
        assertThat(matcher.group(1), is(RAW_ATOM_INNER));
    }

}
