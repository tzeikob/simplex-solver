package com.tkb.simplex.unit;

import com.tkb.simplex.math.LinearProblem;
import com.tkb.simplex.text.AbstractParser;
import com.tkb.simplex.text.LinearProblemParser;
import org.apache.log4j.Logger;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * A unit test case to test the parsing of linear problems.
 *
 * @author Akis Papadopoulos
 */
public class ParseTestCase {

    private static final Logger logger = Logger.getLogger(ParseTestCase.class);

    private AbstractParser parser;

    @Before
    public void init() {
        parser = new LinearProblemParser();
    }

    @Test
    public void testParseEmptyText() {
        final String text = "";

        LinearProblem problem = parser.parse(text);

        assertNull(problem);
    }

    @Test
    public void testParseTextWithSyntaxError() {
        final String text = "max z = x1 + x2 st x1x2 <= 5, 2x1 + x2 >= 4";

        LinearProblem problem = parser.parse(text);

        assertNull(problem);
    }

    @Test
    public void testParseText() {
        final String text = "max z = x1 + x2 st x1 + x2 <= 5, 2x1 - 3x2 >= 4";

        LinearProblem problem = parser.parse(text);

        assertNotNull(problem);
        assertTrue(problem.A()[1][1] == -3);
        assertTrue(problem.b()[1][0] == 4);
    }
}
