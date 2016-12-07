package com.tkb.simplex.text;

import java.util.regex.Pattern;
import com.tkb.simplex.math.LinearProblem;

/**
 * A linear problem parser.
 *
 * @author Akis Papadopoulos
 */
public class LinearProblemParser extends AbstractParser {

    private static final String COMPARATOR = "(" + LT_EQ + "|" + GT_EQ + ")";

    private static final String FUNCTION = "(" + SIGN + "?" + NUMBER + "?" + VARIABLE + "(" + SIGN + NUMBER + "?" + VARIABLE + ")*" + ")";

    private static final String CONSTRAINT = "(" + FUNCTION + COMPARATOR + SIGN + "?" + NUMBER + ")";

    private static final String SUBJECT = "(" + "(" + CONSTRAINT + COMMA + ")*" + CONSTRAINT + ")";

    private static final Pattern SYNTAX = Pattern.compile(OBJECT + Z + FUNCTION + ST + SUBJECT);

    public LinearProblemParser() {
        super();
    }

    /**
     * A method to parse a linear problem given in textual form.
     *
     * @param text the linear problem in text.
     * @return a linear problem.
     */
    @Override
    public LinearProblem parse(String text) {
        String script = filter(text);

        if (!isMatched(script)) {
            return null;
        }

        String[] parts = script.split(Z + "|" + ST);

        String object = parts[0];
        String objective = parts[1];
        String subject = parts[2];

        String[] functions = filter(subject.split(COMPARATOR + SIGN + "?" + NUMBER + COMMA + "?"));

        String[] comparators = filter(subject.split(FUNCTION + "|" + "(" + SIGN + "?" + NUMBER + COMMA + "?" + ")"));

        String[] constants = filter(subject.split("(" + FUNCTION + COMPARATOR + ")" + "|" + COMMA));

        if (!areVariablesUnique(objective)) {
            return null;
        }

        for (int i = 0; i < functions.length; i++) {
            if (!areVariablesUnique(functions[i])) {
                return null;
            }
        }

        int m = functions.length;
        int n = getVariablesCount(objective, functions);

        double minmax = parseMinmax(object);

        double[][] c = parseFactors(objective, n);

        double[][] A = new double[m][n];

        for (int i = 0; i < functions.length; i++) {
            double[][] Ai = parseFactors(functions[i], n);

            for (int j = 0; j < n; j++) {
                A[i][j] = Ai[0][j];
            }
        }

        double[][] bt = parseDouble(constants);
        double[][] b = new double[bt[0].length][1];

        for (int j = 0; j < bt[0].length; j++) {
            b[j][0] = bt[0][j];
        }

        double[][] eqin = parseEqin(comparators);

        report = "Proccess completed.";

        return new LinearProblem(minmax, A, c, b, eqin);
    }

    /**
     * A method matching if a given linear problem has the valid syntax rules.
     *
     * @param text the linear problem in text.
     * @return true if matching the syntax rules otherwise false.
     */
    @Override
    public boolean isMatched(String text) {
        String script = filter(text);

        if (script.matches(SYNTAX.pattern())) {
            return true;
        } else {
            try {
                if (!script.matches(OBJECT + ANYTHING)) {
                    throw new MySyntaxException(MySyntaxException.MISSING_OBJECT);
                } else if (!script.matches(OBJECT + Z + ANYTHING)) {
                    throw new MySyntaxException(MySyntaxException.MISSING_Z);
                } else if (!script.matches(OBJECT + Z + ANYTHING + ST + ANYTHING)) {
                    throw new MySyntaxException(MySyntaxException.MISSING_ST);
                } else if (!script.matches(OBJECT + Z + FUNCTION + ST + ANYTHING)) {
                    throw new MySyntaxException(MySyntaxException.ON_OBJECTIVE_FUNCTION);
                } else if (!script.matches(OBJECT + Z + FUNCTION + ST + SUBJECT)) {
                    throw new MySyntaxException(MySyntaxException.ON_SUBJECT);
                }
            } catch (MySyntaxException msexc) {
                report = msexc.getMessage();
            }

            return false;
        }
    }

    /**
     * A method checking if the variables defined within that function area have
     * unique representation.
     *
     * @param function the function in textual form.
     * @return true if variables are unique otherwise false.
     */
    private boolean areVariablesUnique(String function) {
        String[] indexes = filter(function.split(SIGN + "?" + NUMBER + "?" + X));

        for (int i = 0; i < indexes.length - 1; i++) {
            for (int j = i + 1; j < indexes.length; j++) {
                if (indexes[i].equals(indexes[j])) {
                    try {
                        throw new IndexDefinedException(IndexDefinedException.IN_FUNCTION, "x" + indexes[i], function);
                    } catch (IndexDefinedException midexc) {
                        report = midexc.getMessage();
                    }

                    return false;
                }
            }
        }

        return true;
    }

    /**
     * A method extracting the number of the variables contained within the
     * given functions.
     *
     * @param objective the min/max objective of the function.
     * @param functions the functions in textual form.
     * @return the number of the variables.
     */
    private int getVariablesCount(String objective, String[] functions) {
        int n = getMaxOrder(objective);

        for (int i = 0; i < functions.length; i++) {
            int ni = getMaxOrder(functions[i]);
            if (ni > n) {
                n = ni;
            }
        }

        return n;
    }

    /**
     * A method extracting the maximum order within the given function.
     *
     * @param function the function in textual form.
     * @return the maximum order.
     */
    private int getMaxOrder(String function) {
        String[] alphs = filter(function.split(SIGN + "?" + NUMBER + "?" + X));
        int[] indexes = parseInteger(alphs);

        return getMax(indexes);
    }

    /**
     * A method extracting the factors of a given function.
     *
     * @param function the function in textual form.
     * @param n the number of factors expected.
     * @return the factors matrix.
     */
    private double[][] parseFactors(String function, int n) {
        String[] alphs = function.split(VARIABLE);

        if (alphs.length == 0) {
            alphs = new String[]{""};
        }

        double[][] numbers = parseDouble(alphs);

        alphs = filter(function.split(SIGN + "?" + NUMBER + "?" + X));

        int[] indexes = parseInteger(alphs);

        double[][] factors = new double[1][n];

        for (int j = 0; j < indexes.length; j++) {
            factors[0][indexes[j] - 1] = numbers[0][j];
        }

        return factors;
    }
}
