package com.noahseidman.backbaseexample;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void abortableSearchTest() {
        AbortableSearch abortableSearch = new AbortableSearch(new ArrayList<>(), "");
        abortableSearch.startsWith(null, null);

        assertTrue(abortableSearch.startsWith("test", "te"));
        assertTrue(abortableSearch.startsWith("test", "tes"));
        assertTrue(abortableSearch.startsWith("test", "test"));

        assertFalse(abortableSearch.startsWith("cheese", "please"));
    }
}