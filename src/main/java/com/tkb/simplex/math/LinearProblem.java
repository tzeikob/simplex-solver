package com.tkb.simplex.math;

/**
 * A linear problem.
 *
 * @author Akis Papadopoulos
 */
public class LinearProblem {

    protected double minmax;

    protected double[][] A;

    protected double[][] c;

    protected double[][] b;

    protected double[][] eqin;

    protected double[][] nng;

    protected double globalmax;

    protected double[][] colmulti;

    protected double[][] rowmulti;

    public LinearProblem(double minmax, double[][] A, double[][] c, double[][] b, double[][] eqin) {
        this.minmax = minmax;
        this.A = A;
        this.c = c;
        this.b = b;
        this.eqin = eqin;
        this.nng = BasicMath.ones(1, c[0].length);
    }

    public LinearProblem(double minmax, double[][] A, double[][] c, double[][] b, double[][] eqin, double[][] nng) {
        this.minmax = minmax;
        this.A = A;
        this.c = c;
        this.b = b;
        this.eqin = eqin;
        this.nng = nng;
    }

    public int[] size() {
        int[] dim = new int[2];

        dim[0] = A.length;
        dim[1] = c[0].length;

        return dim;
    }

    public double minmax() {
        return minmax;
    }

    public double[][] A() {
        return BasicMath.copy(A);
    }

    public double[][] c() {
        return BasicMath.copy(c);
    }

    public double[][] b() {
        return BasicMath.copy(b);
    }

    public double[][] eqin() {
        return BasicMath.copy(eqin);
    }

    /**
     * A method creating a dual linear problem of the given problem.
     *
     * @return a dual linear problem.
     */
    public LinearProblem dual() {
        int md = c[0].length;
        int nd = A.length;

        double minmaxd = (-1) * minmax;

        double[][] Ad = BasicMath.transpose(A);
        double[][] cd = BasicMath.transpose(b);
        double[][] bd = BasicMath.transpose(c);
        double[][] eqind = BasicMath.zeros(md, 1);
        double[][] nngd = BasicMath.zeros(1, nd);

        if (minmax == 1) {
            for (int j = 0; j < nng[0].length; j++) {
                if (nng[0][j] == 0) {
                    eqind[j][0] = 0;
                } else if (nng[0][j] == 1) {
                    eqind[j][0] = 1;
                } else {
                    eqind[j][0] = -1;
                }
            }

            for (int i = 0; i < eqin.length; i++) {
                if (eqin[i][0] == 0) {
                    nngd[0][i] = 0;
                } else if (eqin[i][0] == -1) {
                    nngd[0][i] = 1;
                } else {
                    nngd[0][i] = -1;
                }
            }
        } else {
            for (int i = 0; i < eqin.length; i++) {
                if (eqin[i][0] == 0) {
                    nngd[0][i] = 0;
                } else if (eqin[i][0] == 1) {
                    nngd[0][i] = 1;
                } else {
                    nngd[0][i] = -1;
                }
            }

            for (int j = 0; j < nng[0].length; j++) {
                if (nng[0][j] == 0) {
                    eqind[j][0] = 0;
                } else if (nng[0][j] == 1) {
                    eqind[j][0] = -1;
                } else {
                    eqind[j][0] = 1;
                }
            }
        }

        return new LinearProblem(minmaxd, Ad, cd, bd, eqind, nngd);
    }

    /**
     * A method scaling the linear problem by one.
     */
    public void scale() {
        scale(1.0);
    }

    /**
     * A method scaling the given linear problem by the global value.
     *
     * @param globalmax the scale global value.
     */
    public void scale(double globalmax) {
        this.globalmax = globalmax;

        int m = A.length;
        int n = c[0].length;

        boolean[] colm = new boolean[m];

        colmulti = BasicMath.ones(1, n);

        for (int j = 0; j < n; j++) {
            int[][] indx = {{j}};

            double[][] col = BasicMath.col(A, indx);

            double[][] colmax = BasicMath.max(BasicMath.abs(col));

            if (colmax[0][0] > BasicMath.TOLERANCE) {
                colm[(int) colmax[0][1]] = true;

                colmulti[0][j] = globalmax / colmax[0][0];

                for (int i = 0; i < m; i++) {
                    A[i][j] *= colmulti[0][j];
                }

                c[0][j] *= colmulti[0][j];
            }
        }

        rowmulti = BasicMath.ones(m, 1);

        for (int i = 0; i < m; i++) {
            if (!colm[i]) {
                int[][] indx = {{i}};

                double[][] row = BasicMath.row(A, indx);

                double[][] rowmax = BasicMath.max(BasicMath.abs(row));

                if (rowmax[0][0] > BasicMath.TOLERANCE) {
                    rowmulti[i][0] = globalmax / rowmax[0][0];

                    for (int j = 0; j < n; j++) {
                        A[i][j] *= rowmulti[i][0];
                    }

                    b[i][0] *= rowmulti[i][0];
                }
            }
        }
    }

    /**
     * A static method returning a random problem given the lower and upper
     * limits of the matrices.
     *
     * @param m the m size of the linear problem.
     * @param n the n size of the linear problem.
     * @param minmaxcard the min max indicator card of the objective.
     * @param Al the A matrix value lower limit.
     * @param Au the A matrix value upper limit.
     * @param cl the c matrix value lower limit.
     * @param cu the c matrix value upper limit.
     * @param bl the b matrix value lower limit.
     * @param bu the b matrix value upper limit.
     * @param eqincard the equation indicator matrix card.
     * @return a linear problem.
     */
    public static LinearProblem random(int m, int n, int minmaxcard, double Al, double Au, double cl, double cu, double bl, double bu, int eqincard) {
        double minmaxr;

        if (minmaxcard == -1) {
            minmaxr = -1;
        } else if (minmaxcard == 1) {
            minmaxr = 1;
        } else {
            minmaxr = -1;
            if (Math.random() < 0.5) {
                minmaxr = 1;
            }
        }

        double[][] Ar = BasicMath.rand(m, n, Al, Au);

        double[][] cr = BasicMath.rand(1, n, cl, cu);

        double[][] br = BasicMath.rand(m, 1, bl, bu);

        double[][] eqinr;

        switch (eqincard) {
            case 1:
                eqinr = BasicMath.ones(m, 1);
                break;
            case 2:
                eqinr = BasicMath.zeros(m, 1);
                break;
            case 3:
                eqinr = BasicMath.rand(m, 1, 0, 1);
                break;
            case 4:
                eqinr = BasicMath.scale(BasicMath.ones(m, 1), -1);
                break;
            case 5:
                eqinr = BasicMath.ones(m, 1);

                for (int i = 0; i < m; i++) {
                    if (Math.random() < 0.5) {
                        eqinr[i][0] = -1;
                    }
                }

                break;
            case 6:
                eqinr = BasicMath.rand(m, 1, -1, 0);
                break;
            default:
                eqinr = BasicMath.rand(m, 1, -1, 1);
        }

        return new LinearProblem(minmaxr, Ar, cr, br, eqinr);
    }

    /**
     * A static method returning a random optimal problem given the lower and
     * upper limits of the matrices.
     *
     * @param m the m size of the linear problem.
     * @param n the n size of the linear problem.
     * @param Al the A matrix value lower limit.
     * @param Au the A matrix value upper limit.
     * @param cl the c matrix value lower limit.
     * @param cu the c matrix value upper limit.
     * @param center the center of the feasible area of the problem.
     * @param r the radius of the feasible area of the problem.
     * @return a linear problem.
     */
    public static LinearProblem optimal(int m, int n, double Al, double Au, double cl, double cu, double center[][], double r) {
        double minmaxo = -1;

        if (Math.random() < 0.5) {
            minmaxo = 1;
        }

        double[][] Ao = BasicMath.rand(m, n, Al, Au);
        double[][] co = BasicMath.rand(1, n, cl, cu);
        double[][] bo = BasicMath.zeros(m, 1);

        for (int i = 0; i < m; i++) {
            int[][] indx = {{i}};

            double[][] a = BasicMath.row(Ao, indx);

            double nrm = BasicMath.norm(a);

            double[][] y = BasicMath.add(center, BasicMath.scale(a, r / nrm));

            bo[i][0] = BasicMath.dot(a, y);
        }

        double[][] eqino = BasicMath.ones(m, 1);

        eqino = BasicMath.scale(eqino, -1);

        return new LinearProblem(minmaxo, Ao, co, bo, eqino);
    }

    @Override
    public String toString() {
        String str = "minmax:\n" + "[" + minmax + "]\n\n";
        str += "c:\n" + BasicMath.toString(c) + "\n";
        str += "A:\n" + BasicMath.toString(A) + "\n";
        str += "b:\n" + BasicMath.toString(b) + "\n";
        str += "eqin:\n" + BasicMath.toString(eqin);

        return str;
    }
}
