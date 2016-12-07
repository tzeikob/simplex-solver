package com.tkb.simplex.math;

import java.util.Vector;
import java.util.Random;
import java.text.DecimalFormat;

/**
 * A basic math functions module.
 *
 * @author Akis Papadopoulos
 */
public final class BasicMath {

    public static final double EPS = 2.2204e-016;

    public static final double TOLERANCE = 10e-8;

    public static final int NOD = 4;

    public static DecimalFormat former = new DecimalFormat("#.####");

    public static Random generator = new Random();

    /**
     * A method setting the seed of the random generator.
     *
     * @param seed the seed number.
     */
    public static void setSeed(long seed) {
        generator.setSeed(seed);
    }

    /**
     * A method returning a random square matrix populated with value in the
     * given range.
     *
     * @param m the size of the square matrix.
     * @param l the lower limit value.
     * @param u the upper limit value.
     * @return a random square matrix.
     */
    public static double[][] rand(int m, double l, double u) {
        return rand(m, m, l, u);
    }

    /**
     * A method returning a random matrix populated with value in the given
     * range.
     *
     * @param m the number of the rows.
     * @param n the number of the columns.
     * @param l the lower limit value.
     * @param u the upper limit value.
     * @return a random matrix.
     */
    public static double[][] rand(int m, int n, double l, double u) {
        double[][] a = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = Math.floor((u - l + 1) * generator.nextDouble()) + l;
            }
        }

        return a;
    }

    /**
     * A method returning a square matrix of zeros.
     *
     * @param m the size of the square matrix.
     * @return a square matrix of zeros.
     */
    public static double[][] zeros(int m) {
        return zeros(m, m);
    }

    /**
     * A method returning a matrix of zeros.
     *
     * @param m the number of rows.
     * @param n the number of columns.
     * @return a matrix of zeros.
     */
    public static double[][] zeros(int m, int n) {
        double[][] a = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = 0.0;
            }
        }

        return a;
    }

    /**
     * A method returning a square matrix of ones.
     *
     * @param m the size of the square matrix.
     * @return a square matrix of ones.
     */
    public static double[][] ones(int m) {
        return ones(m, m);
    }

    /**
     * A method returning a matrix of ones.
     *
     * @param m the number of rows.
     * @param n the number of columns.
     * @return a matrix of ones.
     */
    public static double[][] ones(int m, int n) {
        double[][] a = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                a[i][j] = 1.0;
            }
        }

        return a;
    }

    /**
     * A method returning a square diagonal eye matrix.
     *
     * @param m the size of the square matrix.
     * @return a square eye matrix.
     */
    public static double[][] eye(int m) {
        return eye(m, m);
    }

    /**
     * A method returning a diagonal eye matrix.
     *
     * @param m the number of rows.
     * @param n the number of columns.
     * @return an eye matrix.
     */
    public static double[][] eye(int m, int n) {
        double[][] a = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    a[i][j] = 0.0;
                } else {
                    a[i][j] = 1.0;
                }
            }
        }

        return a;
    }

    /**
     * A method returning the min value within a given matrix as well as the
     * indexes of the position the value found.
     *
     * @param a a matrix.
     * @return the min value and the position indexes.
     */
    public static double[][] min(double[][] a) {
        double[][] mn = zeros(1, 3);

        mn[0][0] = a[0][0];

        mn[0][1] = 0;
        mn[0][2] = 0;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (a[i][j] < mn[0][0]) {
                    mn[0][0] = a[i][j];

                    mn[0][1] = i;
                    mn[0][2] = j;
                }
            }
        }

        return mn;
    }

    /**
     * A method returning the max value within a given matrix as well as the
     * indexes of the position the value found.
     *
     * @param a a matrix.
     * @return the max value and the position indexes.
     */
    public static double[][] max(double[][] a) {
        double[][] mx = zeros(1, 3);

        mx[0][0] = a[0][0];

        mx[0][1] = 0;
        mx[0][2] = 0;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (a[i][j] > mx[0][0]) {
                    mx[0][0] = a[i][j];

                    mx[0][1] = i;
                    mx[0][2] = j;
                }
            }
        }

        return mx;
    }

    /**
     * A method returning the trace of the given matrix.
     *
     * @param a a matrix.
     * @return the trace value.
     */
    public static double trace(double[][] a) {
        double sum = 0.0;

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                if (i == j) {
                    sum += a[i][j];
                }
            }
        }

        return sum;
    }

    /**
     * A method returning the determinant of the given square matrix.
     *
     * @param a a square matrix.
     * @return the determinant value.
     */
    public static double det(double[][] a) {
        if (a.length != a[0].length) {
            throw new IllegalArgumentException("Array must be squear");
        }

        int m = a.length;

        double d = 0.0;

        if (m == 1) {
            d = a[0][0];
        } else if (m == 2) {
            d = a[0][0] * a[1][1] - a[1][0] * a[0][1];
        } else {
            int[][] rx = {{0}};

            for (int j = 0; j < m; j++) {
                int[][] cx = {{j}};

                double[][] ax = rowx(colx(a, cx), rx);

                d += Math.pow(-1, j + 2) * a[0][j] * det(ax);
            }
        }

        return d;
    }

    /**
     * A method returning the rank value of the given matrix.
     *
     * @param a a matrix.
     * @return the rank value.
     */
    public static double rank(double[][] a) {
        double[][] ar = rref(a);

        double nnzr = 0.0;

        for (int i = 0; i < ar.length; i++) {
            for (int j = 0; j < ar[0].length; j++) {
                if (Math.abs(ar[i][j]) > TOLERANCE) {
                    nnzr += 1.0;
                    break;
                }
            }
        }

        return nnzr;
    }

    /**
     * A method returning the dot product of the given vector matrices.
     *
     * @param x a vector matrix.
     * @param y a vector matrix.
     * @return the dot product.
     */
    public static double dot(double[][] x, double[][] y) {
        if (((x.length != 1) && (x[0].length != 1)) || ((y.length != 1) && (y[0].length != 1))) {
            throw new IllegalArgumentException("Arrays must be both vectors");
        }

        double[] xv;

        if (x.length == 1) {
            xv = new double[x[0].length];

            for (int j = 0; j < x[0].length; j++) {
                xv[j] = x[0][j];
            }
        } else {
            xv = new double[x.length];

            for (int i = 0; i < x.length; i++) {
                xv[i] = x[i][0];
            }
        }

        double[] yv;

        if (y.length == 1) {
            yv = new double[y[0].length];

            for (int j = 0; j < y[0].length; j++) {
                yv[j] = y[0][j];
            }
        } else {
            yv = new double[y.length];

            for (int i = 0; i < y.length; i++) {
                yv[i] = y[i][0];
            }
        }

        if (xv.length != yv.length) {
            throw new IllegalArgumentException("Vectors must have equal length");
        }

        double dt = 0.0;

        for (int i = 0; i < xv.length; i++) {
            dt += xv[i] * yv[i];
        }

        return dt;
    }

    /**
     * A method returning the norm value of the given vector matrix.
     *
     * @param a a vector matrix.
     * @return the norm value.
     */
    public static double norm(double[][] a) {
        if ((a.length != 1) && (a[0].length != 1)) {
            throw new IllegalArgumentException("Array must be vector");
        }

        double value = max(abs(a))[0][0];

        double[][] y = scale(a, 1 / value);

        double nrm = value * Math.sqrt(dot(y, y));

        if (Double.isNaN(nrm)) {
            nrm = 0.0;
        }

        return nrm;
    }

    /**
     * A method returning the norm value of the given vector matrix.
     *
     * @param a a matrix.
     * @param description the INF description.
     * @return the norm value.
     */
    public static double norm(double[][] a, String description) {
        double nrm = 0.0;

        if (description != null && description.equalsIgnoreCase("inf")) {
            double sum = 0.0;

            for (int j = 0; j < a[0].length; j++) {
                sum += Math.abs(a[0][j]);
            }

            double max = sum;

            for (int i = 1; i < a.length; i++) {
                sum = 0.0;

                for (int j = 0; j < a[0].length; j++) {
                    sum += Math.abs(a[i][j]);
                }

                if (sum > max) {
                    max = sum;
                }
            }

            nrm = max;
        }

        return nrm;
    }

    /**
     * A method returning a matrix containing the absolute values of the given
     * matrix.
     *
     * @param a a matrix.
     * @return the matrix of absolute values.
     */
    public static double[][] abs(double[][] a) {
        int m = a.length;
        int n = a[0].length;

        double[][] aabs = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                aabs[i][j] = Math.abs(a[i][j]);
            }
        }

        return aabs;
    }

    /**
     * A method returning the sum of two given matrices.
     *
     * @param x a matrix.
     * @param y a matrix.
     * @return the sum matrix.
     */
    public static double[][] add(double[][] x, double[][] y) {
        if ((x.length != y.length) || (x[0].length != y[0].length)) {
            throw new IllegalArgumentException("Arrays must have equal length");
        }

        int m = x.length;
        int n = x[0].length;

        double[][] sum = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sum[i][j] = x[i][j] + y[i][j];
            }
        }

        return sum;
    }

    /**
     * A method returning the subtract of two given matrices.
     *
     * @param x a matrix.
     * @param y a matrix.
     * @return the subtract matrix.
     */
    public static double[][] subtract(double[][] x, double[][] y) {
        if ((x.length != y.length) || (x[0].length != y[0].length)) {
            throw new IllegalArgumentException("Arrays must have equal length");
        }

        int m = x.length;
        int n = x[0].length;

        double[][] sub = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                sub[i][j] = x[i][j] - y[i][j];
            }
        }

        return sub;
    }

    /**
     * A method returning the scaled version of a given matrix.
     *
     * @param a a matrix.
     * @param value the scale factor.
     * @return the scaled matrix.
     */
    public static double[][] scale(double[][] a, double value) {
        int m = a.length;
        int n = a[0].length;

        double[][] an = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                an[i][j] = a[i][j] * value;
            }
        }

        return an;
    }

    /**
     * A method returning the multiplication matrix of the given two matrices.
     *
     * @param x a matrix.
     * @param y a matrix.
     * @return the multiplication matrix.
     */
    public static double[][] multiply(double[][] x, double[][] y) {
        if (x[0].length != y.length) {
            throw new IllegalArgumentException("Inner arrays dimensions must agree");
        }

        int m = x.length;
        int n = y[0].length;

        double[][] mul = new double[m][n];

        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < y[0].length; j++) {
                double sum = 0.0;

                for (int k = 0; k < y.length; k++) {
                    sum += x[i][k] * y[k][j];
                }

                mul[i][j] = sum;
            }
        }

        return mul;
    }

    /**
     * A method returning the transposed version of a given matrix.
     *
     * @param a a matrix.
     * @return the transposed matrix.
     */
    public static double[][] transpose(double[][] a) {
        try {
            int m = a[0].length;
            int n = a.length;

            double[][] at = new double[m][n];

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    at[i][j] = a[j][i];
                }
            }

            return at;
        } catch (ArrayIndexOutOfBoundsException aiobexc) {
            return new double[0][0];
        }
    }

    /**
     * A method returning the inverse version of the given square matrix.
     *
     * @param a a square matrix.
     * @return the reverse matrix.
     */
    public static double[][] inverse(double[][] a) {
        if (a.length != a[0].length) {
            throw new IllegalArgumentException("Array must be squear to inverse");
        }

        int m = a.length;

        double[][] I = eye(m);

        double[][] AI = appendcol(a, I);

        AI = rref(AI);

        double[][] inv = new double[m][m];

        for (int i = 0; i < m; i++) {
            for (int j = m; j < m + m; j++) {
                inv[i][j - m] = AI[i][j];
            }
        }

        return inv;
    }

    /**
     * A method returning the RREF matrix of a given matrix.
     *
     * @param a a matrix.
     * @return the RREF matrix.
     */
    public static double[][] rref(double[][] a) {
        double[][] ar = copy(a);

        int i = 0, j = 0;

        while ((i < ar.length) && (j < ar[0].length)) {
            double pivot = Math.abs(ar[i][j]);

            int k = i;

            for (int ni = i + 1; ni < ar.length; ni++) {
                if (Math.abs(ar[ni][j]) > pivot) {
                    pivot = Math.abs(ar[ni][j]);

                    k = ni;
                }
            }

            if (pivot <= TOLERANCE) {
                for (int ni = i; ni < ar.length; ni++) {
                    ar[ni][j] = 0.0;
                }

                j++;
            } else {
                ar = swaprows(ar, i, k);
                ar = mulrow(ar, i, 1 / ar[i][j]);

                for (int ni = 0; ni < i; ni++) {
                    ar = sumrows(ar, ni, i, -ar[ni][j]);
                }

                for (int ni = i + 1; ni < ar.length; ni++) {
                    ar = sumrows(ar, ni, i, -ar[ni][j]);
                }

                i++;
                j++;
            }
        }

        return ar;
    }

    /**
     * A method resolving the linear problem defined by the given matrices.
     *
     * @param A the A matrix.
     * @param b the b vector matrix.
     * @return the solution vector matrix.
     */
    public static double[][] solve(double[][] A, double[][] b) {
        if (A.length != b.length) {
            throw new IllegalArgumentException("Arrays must have equal length");
        }

        int n = A[0].length;

        double[][] x = new double[n][1];

        double[][] Ab = appendcol(A, b);

        if (rank(A) == rank(Ab) && rank(A) == n) {
            x = multiply(inverse(A), b);

            x = round(x);
        } else if (rank(A) == rank(Ab) && rank(A) < n) {
            for (int i = 0; i < n; i++) {
                x[i][0] = Double.POSITIVE_INFINITY;
            }
        } else {
            for (int i = 0; i < n; i++) {
                x[i][0] = Double.NaN;
            }
        }

        return x;
    }

    /**
     * A method copying the given matrix.
     *
     * @param a a matrix.
     * @return the copy matrix.
     */
    public static double[][] copy(double[][] a) {
        int m = a.length;
        int n = a[0].length;

        double[][] ac = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ac[i][j] = a[i][j];
            }
        }

        return ac;
    }

    /**
     * A method converting a given integers matrix into a matrix of doubles.
     *
     * @param a an integers matrix.
     * @return the doubles matrix.
     */
    public static double[][] convert(int[][] a) {
        int m = a.length;
        int n = a[0].length;

        double[][] ac = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ac[i][j] = (double) a[i][j];
            }
        }

        return ac;
    }

    /**
     * A method converting a given integers matrix into a matrix of doubles.
     *
     * @param a an integers matrix.
     * @return the doubles matrix.
     */
    public static double[][] convert(long[][] a) {
        int m = a.length;
        int n = a[0].length;

        double[][] ac = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ac[i][j] = (double) a[i][j];
            }
        }

        return ac;
    }

    /**
     * A method converting a given floats matrix into a matrix of doubles.
     *
     * @param a a floats matrix.
     * @return the doubles matrix.
     */
    public static double[][] convert(float[][] a) {
        int m = a.length;
        int n = a[0].length;

        double[][] ac = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ac[i][j] = (double) a[i][j];
            }
        }

        return ac;
    }

    /**
     * A method rounding up the value of the given matrix.
     *
     * @param a a matrix.
     * @return the rounded matrix.
     */
    public static double[][] round(double[][] a) {
        int m = a.length;
        int n = a[0].length;

        double[][] ar = new double[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ar[i][j] = round(a[i][j], NOD);
            }
        }

        return ar;
    }

    /**
     * A method returning the columns of the given matrix.
     *
     * @param a a matrix.
     * @param indx the indexes of the columns to return.
     * @return the matrix containing the columns.
     */
    public static double[][] col(double[][] a, int[][] indx) {
        int m = a.length;
        int n = indx.length * indx[0].length;

        double[][] an = new double[m][n];

        try {
            int t = 0;

            for (int j = 0; j < indx[0].length; j++) {
                for (int i = 0; i < indx.length; i++, t++) {
                    for (int k = 0; k < a.length; k++) {
                        an[k][t] = a[k][indx[i][j]];
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException aiobexc) {
            throw new ArrayIndexOutOfBoundsException("Index out of array bounds");
        }

        return an;
    }

    /**
     * A method excluding columns of a given matrix.
     *
     * @param a a matrix.
     * @param indx the indexes of the columns to be excluded.
     * @return the remaining columns matrix.
     */
    public static double[][] colx(double[][] a, int[][] indx) {
        Vector indexes = new Vector(0, 1);

        for (int j = 0; j < indx[0].length; j++) {
            for (int i = 0; i < indx.length; i++) {
                if (!indexes.contains(indx[i][j])) {
                    indexes.add(indx[i][j]);
                }
            }
        }

        if (a[0].length < indexes.size()) {
            throw new IllegalArgumentException("Index of elements to remove exceeds array dimensions");
        }

        int m = a.length;
        int n = a[0].length - indexes.size();

        double[][] an = new double[m][n];

        try {
            for (int i = 0; i < a.length; i++) {
                for (int j = 0, t = 0; j < a[0].length; j++) {
                    if (!indexes.contains(j)) {
                        an[i][t++] = a[i][j];
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException aiobexc) {
            throw new ArrayIndexOutOfBoundsException("Index out of array bounds");
        }

        return an;
    }

    /**
     * A method swapping the columns of a given matrix.
     *
     * @param a a matrix.
     * @param ai a column of the matrix.
     * @param aj a column of the matrix.
     * @return the swapped matrix.
     */
    public static double[][] swapcols(double[][] a, int ai, int aj) {
        if ((ai < 0) || (ai >= a[0].length) || (aj < 0) || (aj >= a[0].length)) {
            throw new IllegalArgumentException("Index out of array bounds");
        }

        int m = a.length;
        int n = a[0].length;

        double[][] an = new double[m][n];

        for (int i = 0; i < m; i++) {
            an[i][ai] = a[i][aj];
            an[i][aj] = a[i][ai];
        }

        for (int j = 0; j < n; j++) {
            if ((j != ai) && (j != aj)) {
                for (int i = 0; i < m; i++) {
                    an[i][j] = a[i][j];
                }
            }
        }

        return an;
    }

    /**
     * A method multiplying a column of the given matrix with the given value.
     *
     * @param a the matrix.
     * @param ai the column to be multiplied.
     * @param r the value.
     * @return the updated matrix.
     */
    public static double[][] mulcol(double[][] a, int ai, double r) {
        if ((ai < 0) || (ai >= a[0].length)) {
            throw new IllegalArgumentException("Index out of array bounds");
        }

        int m = a.length;
        int n = a[0].length;

        double[][] an = new double[m][n];

        for (int i = 0; i < m; i++) {
            an[i][ai] = r * a[i][ai];
        }

        for (int j = 0; j < n; j++) {
            if (j != ai) {
                for (int i = 0; i < m; i++) {
                    an[i][j] = a[i][j];
                }
            }
        }

        return an;
    }

    /**
     * A method updates to column of a given matrix by adding its values with
     * the values another's columns multiplied by a given value.
     *
     * @param a the matrix.
     * @param ai the column to be updated.
     * @param aj the column to be multiplied.
     * @param r the value.
     * @return the updated matrix.
     */
    public static double[][] sumcols(double[][] a, int ai, int aj, double r) {
        if ((ai < 0) || (ai >= a[0].length) || (aj < 0) || (aj >= a[0].length)) {
            throw new IllegalArgumentException("Index out of array bounds");
        }

        int m = a.length;
        int n = a[0].length;

        double[][] an = new double[m][n];

        for (int i = 0; i < m; i++) {
            an[i][ai] = a[i][ai] + r * a[i][aj];
        }

        for (int j = 0; j < n; j++) {
            if (j != ai) {
                for (int i = 0; i < m; i++) {
                    an[i][j] = a[i][j];
                }
            }
        }

        return an;
    }

    /**
     * A method discarding duplicate columns of a given matrix.
     *
     * @param a a matrix.
     * @return the unique column matrix.
     */
    public static double[][] uniquecol(double[][] a) {
        Vector indexes = new Vector(0, 1);

        for (int i = 0; i < a[0].length - 1; i++) {
            int[][] ci = {{i}};
            double[][] coli = col(a, ci);

            for (int j = i + 1; j < a[0].length; j++) {
                if (!indexes.contains(j)) {
                    int[][] cj = {{j}};
                    double[][] colj = col(a, cj);

                    double[][] sub = subtract(coli, colj);

                    if (norm(sub) < 10 * EPS) {
                        indexes.add(j);
                    }
                }
            }
        }

        int m = a.length;
        int n = a[0].length - indexes.size();

        double[][] au = new double[m][n];

        for (int i = 0; i < a.length; i++) {
            for (int j = 0, k = 0; j < a[0].length; j++) {
                if (!indexes.contains(j)) {
                    au[i][k++] = a[i][j];
                }
            }
        }

        return au;
    }

    /**
     * A method appending the given matrices of the same form by column.
     *
     * @param a a matrix.
     * @param x a matrix.
     * @return a matrix.
     */
    public static double[][] appendcol(double[][] a, double[][] x) {
        try {
            if (a.length != x.length) {
                throw new IllegalArgumentException("Arrays must have equal length");
            }

            double[][] ax = new double[a.length][a[0].length + x[0].length];

            for (int i = 0; i < a.length; i++) {
                for (int j = 0; j < a[0].length; j++) {
                    ax[i][j] = a[i][j];
                }
            }

            for (int i = 0; i < x.length; i++) {
                for (int j = 0; j < x[0].length; j++) {
                    ax[i][j + a[0].length] = x[i][j];
                }
            }

            return ax;
        } catch (ArrayIndexOutOfBoundsException aiobexc) {
            return new double[0][0];
        }
    }

    /**
     * A method returning the rows of a given matrix.
     *
     * @param a a matrix.
     * @param indx the indexes of the rows to be returned.
     * @return a matrix.
     */
    public static double[][] row(double[][] a, int[][] indx) {
        int m = indx.length * indx[0].length;
        int n = a[0].length;

        double[][] an = new double[m][n];

        try {
            int t = 0;

            for (int j = 0; j < indx[0].length; j++) {
                for (int i = 0; i < indx.length; i++, t++) {
                    for (int k = 0; k < a[0].length; k++) {
                        an[t][k] = a[indx[i][j]][k];
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException aiobexc) {
            throw new ArrayIndexOutOfBoundsException("Index out of array bounds");
        }

        return an;
    }

    /**
     * A method excluding rows of a given matrix.
     *
     * @param a a matrix.
     * @param indx the indexes of the rows to be excluded.
     * @return a matrix.
     */
    public static double[][] rowx(double[][] a, int[][] indx) {
        Vector indexes = new Vector(0, 1);

        for (int j = 0; j < indx[0].length; j++) {
            for (int i = 0; i < indx.length; i++) {
                if (!indexes.contains(indx[i][j])) {
                    indexes.add(indx[i][j]);
                }
            }
        }

        if (a.length < indexes.size()) {
            throw new IllegalArgumentException("Index of elements to remove exceeds array dimensions");
        }

        int m = a.length - indexes.size();
        int n = a[0].length;

        double[][] an = new double[m][n];

        try {
            for (int i = 0, t = 0; i < a.length; i++) {
                if (!indexes.contains(i)) {
                    for (int j = 0; j < a[0].length; j++) {
                        an[t][j] = a[i][j];
                    }
                    t++;
                }
            }
        } catch (ArrayIndexOutOfBoundsException aiobexc) {
            throw new ArrayIndexOutOfBoundsException("Index out of array bounds");
        }

        return an;
    }

    /**
     * A method swapping rows in a given matrix.
     *
     * @param a a matrix.
     * @param ai a row index.
     * @param aj a row index.
     * @return a matrix.
     */
    public static double[][] swaprows(double[][] a, int ai, int aj) {
        if ((ai < 0) || (ai >= a.length) || (aj < 0) || (aj >= a.length)) {
            throw new IllegalArgumentException("Index out of array bounds");
        }

        int m = a.length;
        int n = a[0].length;

        double[][] an = new double[m][n];

        for (int j = 0; j < n; j++) {
            an[ai][j] = a[aj][j];
            an[aj][j] = a[ai][j];
        }

        for (int i = 0; i < m; i++) {
            if ((i != ai) && (i != aj)) {
                for (int j = 0; j < n; j++) {
                    an[i][j] = a[i][j];
                }
            }
        }

        return an;
    }

    /**
     * A method multiplying a row of a given matrix with a given value.
     *
     * @param a a matrix.
     * @param ai the row index.
     * @param r the value.
     * @return a matrix.
     */
    public static double[][] mulrow(double[][] a, int ai, double r) {
        if ((ai < 0) || (ai >= a.length)) {
            throw new IllegalArgumentException("Index out of array bounds");
        }

        int m = a.length;
        int n = a[0].length;

        double[][] an = new double[m][n];

        for (int j = 0; j < n; j++) {
            an[ai][j] = r * a[ai][j];
        }

        for (int i = 0; i < m; i++) {
            if (i != ai) {
                for (int j = 0; j < n; j++) {
                    an[i][j] = a[i][j];
                }
            }
        }

        return an;
    }

    /**
     * A method adding a row of a given matrix with the values of another row
     * multiplied by the given value.
     *
     * @param a a matrix.
     * @param ai the row index.
     * @param aj the row index.
     * @param r the value.
     * @return a matrix.
     */
    public static double[][] sumrows(double[][] a, int ai, int aj, double r) {
        if ((ai < 0) || (ai >= a.length) || (aj < 0) || (aj >= a.length)) {
            throw new IllegalArgumentException("Index out of array bounds");
        }

        int m = a.length;
        int n = a[0].length;

        double[][] an = new double[m][n];

        for (int j = 0; j < n; j++) {
            an[ai][j] = a[ai][j] + r * a[aj][j];
        }

        for (int i = 0; i < m; i++) {
            if (i != ai) {
                for (int j = 0; j < n; j++) {
                    an[i][j] = a[i][j];
                }
            }
        }

        return an;
    }

    /**
     * A method discarding duplicated rows of a given matrix.
     *
     * @param a a matrix.
     * @return a matrix.
     */
    public static double[][] uniquerow(double[][] a) {
        Vector indexes = new Vector(0, 1);

        for (int i = 0; i < a.length - 1; i++) {
            int[][] ri = {{i}};
            double[][] rowi = row(a, ri);

            for (int j = i + 1; j < a.length; j++) {
                if (!indexes.contains(j)) {
                    int[][] rj = {{j}};
                    double[][] rowj = row(a, rj);

                    double[][] sub = subtract(rowi, rowj);

                    if (norm(sub) < 10 * EPS) {
                        indexes.add(j);
                    }
                }
            }
        }

        int m = a.length - indexes.size();
        int n = a[0].length;

        double[][] au = new double[m][n];

        for (int i = 0, k = 0; i < a.length; i++) {
            if (!indexes.contains(i)) {
                for (int j = 0; j < a[0].length; j++) {
                    au[k][j] = a[i][j];
                }

                k++;
            }
        }

        return au;
    }

    /**
     * A method appending two given matrices by row.
     *
     * @param a a matrix.
     * @param x a matrix.
     * @return a matrix.
     */
    public static double[][] appendrow(double[][] a, double[][] x) {
        double[][] at = transpose(a);
        double[][] xt = transpose(x);

        double[][] ax = appendcol(at, xt);

        ax = transpose(ax);

        return ax;
    }

    /**
     * A method calculating if a given vector matrix is a feasible solution to
     * the given linear problem.
     *
     * @param A the A matrix of the problem.
     * @param b the b vector matrix of the problem.
     * @param eqin the equation vector matrix.
     * @param x the solution vector matrix.
     * @return true if feasible otherwise false.
     */
    public static boolean isFeasible(double[][] A, double[][] b, double[][] eqin, double[][] x) {
        if ((A.length != b.length) || (A.length != eqin.length) || (A[0].length != x.length)) {
            throw new IllegalArgumentException("Arrays length not matched");
        }

        for (int i = 0; i < x.length; i++) {
            if (x[i][0] < 0) {
                return false;
            }
        }

        for (int i = 0; i < eqin.length; i++) {
            double[][] Ai = row(A, new int[][]{{i}});
            double valuei = multiply(Ai, x)[0][0];

            double bi = b[i][0];

            if (eqin[i][0] == -1) {
                if (valuei > bi) {
                    return false;
                }
            } else if (eqin[i][0] == 0) {
                if (valuei != bi) {
                    return false;
                }
            } else if (eqin[i][0] == 1) {
                if (valuei < bi) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * A method appending a given value to a given vector matrix.
     *
     * @param a a vector matrix.
     * @param x the value.
     * @return a vector matrix.
     */
    public static int[] append(int[] a, int x) {
        int[] ax = new int[a.length + 1];

        for (int i = 0; i < a.length; i++) {
            ax[i] = a[i];
        }

        ax[ax.length - 1] = x;

        return ax;
    }

    /**
     * A method to print the values of a given matrix.
     *
     * @param a a matrix.
     * @return the string representation of the matrix.
     */
    public static String toString(double[][] a) {
        if (a.length == 0 || a[0].length == 0) {
            return "[]\n";
        }

        String str = "";

        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                str += "[" + round(a[i][j], NOD) + "]   \t";
            }

            str += "\n";
        }

        return str;
    }

    /**
     * A method to print the values of the vector matrix.
     *
     * @param a a matrix.
     * @return the string representation of the matrix.
     */
    public static String toString(int[] a) {
        if (a.length == 0) {
            return "[]\n";
        }

        String str = "";

        for (int i = 0; i < a.length; i++) {
            str += "[" + a[i] + "]   \t";
        }

        return str;
    }

    /**
     * A method rounding the given value by the given digits.
     *
     * @param value the value to round.
     * @param d the digits to be used.
     * @return the rounded value.
     */
    public static double round(double value, int d) {
        d = Math.abs(d);
        value *= Math.pow(10, d);

        double valuer = Math.round(value) / Math.pow(10, d);

        return valuer;
    }

    /**
     * A method formating the given value to a string representation for
     * decimals.
     *
     * @param value the value to be formated.
     * @return the string representation.
     */
    public static String format(double value) {
        return former.format(value);
    }
}
