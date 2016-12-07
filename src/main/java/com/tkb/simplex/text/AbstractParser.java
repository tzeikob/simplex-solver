package com.tkb.simplex.text;

import java.util.Vector;
import com.tkb.simplex.math.LinearProblem;

/**
 * An abstract linear problem parser.
 *
 * @author Akis Papadopoulos
 */
public abstract class AbstractParser {

    protected static final String WHITE_SPACES = "(\\s)";

    protected static final String COMMA = "(,)";

    protected static final String ANYTHING = "(.*)";

    protected static final String CHAR = "([a-zA-Z])";

    protected static final String LABEL = "([a-zA-Z$_][a-zA-Z$_0-9]*)";

    protected static final String SIGN = "(\\+|\\-)";

    protected static final String EQ = "(=)";

    protected static final String GT_EQ = "(>=)";

    protected static final String LT_EQ = "(<=)";

    protected static final String GT = "(>)";

    protected static final String LT = "(<)";

    protected static final String NUMBER = "(\\d*\\.?\\d+(e(\\+|\\-)?\\d+)?)";

    protected static final String INDEX = "([1-9]([0-9]){0,2})";

    protected static final String X = "(x)";

    protected static final String VARIABLE = "(" + X + INDEX + ")";

    protected static final String OBJECT = "(min|max)";

    protected static final String Z = "(z=)";

    protected static final String ST = "(st|s\\.t\\.|subjectto)";

    protected String report;

    public AbstractParser() {
        super();

        report = "";
    }

    /**
     * An abstract method to parse a linear problem given in textual form.
     *
     * @param text the linear problem in text.
     * @return a linear problem.
     */
    public abstract LinearProblem parse(String text);

    /**
     * An abstract method matching if a given linear problem has the valid
     * syntax rules.
     *
     * @param text the linear problem in text.
     * @return true if matching the syntax rules otherwise false.
     */
    public abstract boolean isMatched(String text);

    /**
     * A method returning a textual report of the parsing status.
     *
     * @return a textual report.
     */
    public String getReport() {
        return report;
    }

    /**
     * A method filtering out special and trailing whitespace characters.
     *
     * @param text a text.
     * @return a text.
     */
    protected String filter(String text) {
        String filteredText = text;

        filteredText = filteredText.trim();
        filteredText = filteredText.replaceAll(WHITE_SPACES, "");
        filteredText = filteredText.toLowerCase();

        return filteredText;
    }

    /**
     * A method filtering out special and trailing characters on the values of a
     * given matrix.
     *
     * @param array a matrix of text.
     * @return a text matrix.
     */
    protected String[] filter(String[] array) {
        Vector vector = new Vector(0, 1);

        for (int i = 0; i < array.length; i++) {
            if (!array[i].equals("")) {
                vector.add(array[i]);
            }
        }

        String[] filteredArray = new String[vector.size()];

        for (int i = 0; i < filteredArray.length; i++) {
            filteredArray[i] = (String) vector.get(i);
        }

        return filteredArray;
    }

    /**
     * A method returning the maximum value of a given vector matrix.
     *
     * @param array a vector matrix.
     * @return a value.
     */
    protected int getMax(int[] array) {
        int max = array[0];

        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }

        return max;
    }

    /**
     * A method parsing textual values into integers.
     *
     * @param array the matrix of textual numbers.
     * @return a vector matrix.
     */
    protected int[] parseInteger(String[] array) {
        int[] numbers = new int[array.length];

        for (int i = 0; i < array.length; i++) {
            try {
                numbers[i] = Integer.parseInt(array[i]);
            } catch (NumberFormatException nfexc) {
                numbers[i] = Integer.MAX_VALUE;
            }
        }

        return numbers;
    }

    /**
     * A method parsing a textual value into double.
     *
     * @param value the textual number.
     * @return a value.
     */
    protected double parseDouble(String value) {
        double number;

        try {
            number = Double.parseDouble(value);
        } catch (NumberFormatException nfexc) {
            if (value.equals("+") || value.equals("")) {
                number = 1.0;
            } else if (value.equals("-")) {
                number = -1.0;
            } else {
                number = Double.NaN;
            }
        }

        return number;
    }

    /**
     * A method parsing textual values into doubles.
     *
     * @param array the matrix of textual numbers.
     * @return a matrix.
     */
    protected double[][] parseDouble(String[] array) {
        double[][] numbers = new double[1][array.length];

        for (int j = 0; j < array.length; j++) {
            numbers[0][j] = parseDouble(array[j]);
        }

        return numbers;
    }

    /**
     * A method parsing the min max indicators into integers indicators.
     *
     * @param object the objective of the linear problem in text.
     * @return -1 indicates min and +1 indicates max.
     */
    protected double parseMinmax(String object) {
        if (object.equals("min")) {
            return -1;
        } else {
            return +1;
        }
    }

    /**
     * A method parsing the equation comparators into integers indicators.
     *
     * @param comparators the textual equation comparators.
     * @return -1 indicates '<=', 0 indicates '==' and +1 indicates '>='
     */
    protected double[][] parseEqin(String[] comparators) {
        double[][] eqin = new double[comparators.length][1];

        for (int i = 0; i < comparators.length; i++) {
            if (comparators[i].equals("<=")) {
                eqin[i][0] = -1;
            } else if (comparators[i].equals(">=")) {
                eqin[i][0] = 1;
            } else {
                eqin[i][0] = 0;
            }
        }

        return eqin;
    }
}
