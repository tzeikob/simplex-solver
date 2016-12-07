package com.tkb.simplex.math;

import com.tkb.simplex.model.SpacePoint;
import com.tkb.simplex.model.AbstractObject;
import com.tkb.simplex.model.Surface;
import com.tkb.simplex.model.Edge;
import com.tkb.simplex.model.MarkDot;
import com.tkb.simplex.model.Feasible3DRegion;

/**
 * A basic 3D linear problem solver.
 *
 * @author Akis Papadopoulos
 */
public class Basic3DSolver extends AbstractSolver {

    private double[][] feasv;

    private int[][] faces;

    private double[][] infeasv;

    private int[][] infaces;

    private double[][] objv;

    private int[][] objfaces;

    public Basic3DSolver(double minmax, double[][] A, double[][] c, double[][] b, double[][] eqin) {
        super(minmax, A, c, b, eqin);

        int m = A.length;
        int n = A[0].length;

        feasv = new double[n][0];
        faces = new int[m + 3][0];

        infeasv = new double[n][0];
        infaces = new int[m][0];
    }

    /**
     * A method resolving the given linear problem.
     */
    @Override
    public void solve() {
        int m = A.length;
        int n = A[0].length;

        double[][] A1 = BasicMath.appendrow(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}}, A);
        double[][] b1 = BasicMath.appendrow(new double[][]{{0.0}, {0.0}, {0.0}}, b);
        double[][] eqin1 = BasicMath.appendrow(new double[][]{{1}, {1}, {1}}, eqin);

        for (int i = 0; i < m + n - 2; i++) {
            for (int j = i + 1; j < m + n - 1; j++) {
                for (int k = j + 1; k < m + n; k++) {
                    int[][] ijk = {{i, j, k}};

                    double[][] Aijk = BasicMath.row(A1, ijk);

                    if (BasicMath.det(Aijk) != 0) {
                        double[][] bijk = BasicMath.row(b1, ijk);

                        double[][] x = BasicMath.solve(Aijk, bijk);
                        double[][] Ax = BasicMath.rowx(A1, ijk);
                        double[][] bx = BasicMath.rowx(b1, ijk);
                        double[][] eqinx = BasicMath.rowx(eqin1, ijk);

                        if (BasicMath.isFeasible(Ax, bx, eqinx, x)) {
                            feasibles = BasicMath.appendcol(feasibles, x);

                            feasv = BasicMath.appendcol(feasv, x);

                            int index = feasv[0].length - 1;

                            faces[i] = BasicMath.append(faces[i], index);
                            faces[j] = BasicMath.append(faces[j], index);
                            faces[k] = BasicMath.append(faces[k], index);
                        } else {
                            infeasibles = BasicMath.appendcol(infeasibles, x);
                        }
                    }
                }
            }
        }

        if (feasibles[0].length > 1) {
            feasibles = BasicMath.uniquecol(feasibles);
        }

        if (infeasibles[0].length > 1) {
            infeasibles = BasicMath.uniquecol(infeasibles);
        }

        if (feasibles[0].length > 0) {
            double min, max;

            min = BasicMath.min(BasicMath.row(feasibles, new int[][]{{0}}))[0][0];
            xrays[0][0] = BasicMath.min(new double[][]{{min, c[0][0] / 3, 0.0}})[0][0] - 0.5;

            max = BasicMath.max(BasicMath.row(feasibles, new int[][]{{0}}))[0][0];
            xrays[0][1] = BasicMath.max(new double[][]{{max, c[0][0] / 3, 0.0}})[0][0] + 1.0;

            min = BasicMath.min(BasicMath.row(feasibles, new int[][]{{1}}))[0][0];
            xrays[1][0] = BasicMath.min(new double[][]{{min, c[0][1] / 3, 0.0}})[0][0] - 0.5;

            max = BasicMath.max(BasicMath.row(feasibles, new int[][]{{1}}))[0][0];
            xrays[1][1] = BasicMath.max(new double[][]{{max, c[0][1] / 3, 0.0}})[0][0] + 1.0;

            min = BasicMath.min(BasicMath.row(feasibles, new int[][]{{2}}))[0][0];
            xrays[2][0] = BasicMath.min(new double[][]{{min, c[0][2] / 3, 0.0}})[0][0] - 0.5;

            max = BasicMath.max(BasicMath.row(feasibles, new int[][]{{2}}))[0][0];
            xrays[2][1] = BasicMath.max(new double[][]{{max, c[0][2] / 3, 0.0}})[0][0] + 1.0;
        } else {
            double min, max;

            min = BasicMath.min(BasicMath.row(infeasibles, new int[][]{{0}}))[0][0];
            xrays[0][0] = BasicMath.min(new double[][]{{min, c[0][0] / 3, 0.0}})[0][0] - 0.5;

            max = BasicMath.max(BasicMath.row(infeasibles, new int[][]{{0}}))[0][0];
            xrays[0][1] = BasicMath.max(new double[][]{{max, c[0][0] / 3, 0.0}})[0][0] + 0.5;

            min = BasicMath.min(BasicMath.row(infeasibles, new int[][]{{1}}))[0][0];
            xrays[1][0] = BasicMath.min(new double[][]{{min, c[0][1] / 3, 0.0}})[0][0] - 0.5;

            max = BasicMath.max(BasicMath.row(infeasibles, new int[][]{{1}}))[0][0];
            xrays[1][1] = BasicMath.max(new double[][]{{max, c[0][1] / 3, 0.0}})[0][0] + 0.5;

            min = BasicMath.min(BasicMath.row(infeasibles, new int[][]{{2}}))[0][0];
            xrays[2][0] = BasicMath.min(new double[][]{{min, c[0][2] / 3, 0.0}})[0][0] - 0.5;

            max = BasicMath.max(BasicMath.row(infeasibles, new int[][]{{2}}))[0][0];
            xrays[2][1] = BasicMath.max(new double[][]{{max, c[0][2] / 3, 0.0}})[0][0] + 0.5;
        }

        if (feasibles[0].length > 0) {
            for (int i = 0; i < m + n - 1; i++) {
                for (int j = i + 1; j < m + n; j++) {
                    double[][] Aijx;
                    double[][] bijx;

                    double[][] x;

                    double[][] Aij = BasicMath.row(A1, new int[][]{{i, j}});

                    Aijx = BasicMath.appendrow(Aij, new double[][]{{1.0, 0.0, 0.0}});
                    bijx = new double[][]{{b1[i][0]}, {b1[j][0]}, {xrays[0][1]}};

                    x = BasicMath.solve(Aijx, bijx);

                    if (!Double.isInfinite(x[0][0]) && !Double.isNaN(x[0][0]) && BasicMath.isFeasible(A, b, eqin, x) && x[1][0] <= xrays[1][1] && x[2][0] <= xrays[2][1]) {
                        feasv = BasicMath.appendcol(feasv, x);

                        int index = feasv[0].length - 1;
                        faces[i] = BasicMath.append(faces[i], index);
                        faces[j] = BasicMath.append(faces[j], index);

                        ponrays = BasicMath.appendcol(ponrays, x);
                    }

                    Aijx = BasicMath.appendrow(Aij, new double[][]{{0.0, 1.0, 0.0}});
                    bijx = new double[][]{{b1[i][0]}, {b1[j][0]}, {xrays[1][1]}};

                    x = BasicMath.solve(Aijx, bijx);

                    if (!Double.isInfinite(x[0][0]) && !Double.isNaN(x[0][0]) && BasicMath.isFeasible(A, b, eqin, x) && x[0][0] <= xrays[0][1] && x[2][0] <= xrays[2][1]) {
                        feasv = BasicMath.appendcol(feasv, x);

                        int index = feasv[0].length - 1;
                        faces[i] = BasicMath.append(faces[i], index);
                        faces[j] = BasicMath.append(faces[j], index);

                        ponrays = BasicMath.appendcol(ponrays, x);
                    }

                    Aijx = BasicMath.appendrow(Aij, new double[][]{{0.0, 0.0, 1.0}});
                    bijx = new double[][]{{b1[i][0]}, {b1[j][0]}, {xrays[2][1]}};

                    x = BasicMath.solve(Aijx, bijx);

                    if (!Double.isInfinite(x[0][0]) && !Double.isNaN(x[0][0]) && BasicMath.isFeasible(A, b, eqin, x) && x[0][0] <= xrays[0][1] && x[1][0] <= xrays[1][1]) {
                        feasv = BasicMath.appendcol(feasv, x);

                        int index = feasv[0].length - 1;
                        faces[i] = BasicMath.append(faces[i], index);
                        faces[j] = BasicMath.append(faces[j], index);

                        ponrays = BasicMath.appendcol(ponrays, x);
                    }
                }
            }

            if (ponrays[0].length > 1) {
                ponrays = BasicMath.uniquecol(ponrays);
            }
        }

        if (feasibles[0].length > 0) {
            adarap = 2;

            double[][] c1 = c;

            if (minmax == 1) {
                c1 = BasicMath.scale(c1, -1);
            }

            double[][] zf = new double[1][feasibles[0].length];

            for (int i = 0; i < feasibles[0].length; i++) {
                zf[0][i] = BasicMath.multiply(c1, BasicMath.col(feasibles, new int[][]{{i}}))[0][0];
            }

            double[][] zp = new double[1][ponrays[0].length];

            for (int i = 0; i < ponrays[0].length; i++) {
                zp[0][i] = BasicMath.multiply(c1, BasicMath.col(ponrays, new int[][]{{i}}))[0][0];
            }

            double[][] min = BasicMath.min(zf);

            adarap = 0;

            zoptimal = min[0][0];
            xoptimal = BasicMath.col(feasibles, new int[][]{{(int) min[0][2]}});

            if (ponrays[0].length > 0) {
                double z = BasicMath.min(zp)[0][0];

                if (z < zoptimal) {
                    adarap = 1;

                    zoptimal = Double.POSITIVE_INFINITY;
                    xoptimal = new double[3][0];
                }
            }

            if (adarap == 0 && minmax == 1) {
                zoptimal *= -1;
            }

            if (feasibles[0].length > 0) {
                vertices = BasicMath.copy(feasibles);
            }

            if (ponrays[0].length > 0) {
                vertices = BasicMath.appendcol(feasibles, ponrays);
            }

            objv = new double[n][0];
            objfaces = new int[vertices[0].length][0];

            double[] zv = new double[vertices[0].length];

            for (int i = 0; i < vertices[0].length; i++) {
                double gap = 1.0;

                double x1min = vertices[0][i] - gap;
                double x1max = vertices[0][i] + gap;
                double x2min = vertices[1][i] - gap;
                double x2max = vertices[1][i] + gap;
                double x3min = vertices[2][i] - gap;
                double x3max = vertices[2][i] + gap;

                zv[i] = BasicMath.multiply(c, BasicMath.col(vertices, new int[][]{{i}}))[0][0];

                if (c[0][0] != 0 && c[0][1] == 0 && c[0][2] == 0) {
                    double ba = zv[i] / c[0][0];
                    double[][] x = {{ba}, {x2min}, {x3min}};

                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);

                    x = new double[][]{{ba}, {x2max}, {x3min}};

                    objv = BasicMath.appendcol(objv, x);

                    index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);

                    x = new double[][]{{ba}, {x2max}, {x3max}};

                    objv = BasicMath.appendcol(objv, x);

                    index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);

                    x = new double[][]{{ba}, {x2min}, {x3max}};

                    objv = BasicMath.appendcol(objv, x);

                    index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);

                    continue;
                }

                if (c[0][1] != 0 && c[0][0] == 0 && c[0][2] == 0) {
                    double ba = zv[i] / c[0][1];
                    double[][] x = {{x1min}, {ba}, {x3min}};

                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);

                    x = new double[][]{{x1max}, {ba}, {x3min}};

                    objv = BasicMath.appendcol(objv, x);

                    index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);

                    x = new double[][]{{x1max}, {ba}, {x3max}};

                    objv = BasicMath.appendcol(objv, x);

                    index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);

                    x = new double[][]{{x1min}, {ba}, {x3max}};

                    objv = BasicMath.appendcol(objv, x);

                    index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);

                    continue;
                }

                if (c[0][2] != 0 && c[0][0] == 0 && c[0][1] == 0) {
                    double ba = zv[i] / c[0][2];
                    double[][] x = {{x1min}, {x2min}, {ba}};

                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);

                    x = new double[][]{{x1min}, {x2max}, {ba}};

                    objv = BasicMath.appendcol(objv, x);

                    index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);

                    x = new double[][]{{x1max}, {x2max}, {ba}};

                    objv = BasicMath.appendcol(objv, x);

                    index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);

                    x = new double[][]{{x1max}, {x2min}, {ba}};

                    objv = BasicMath.appendcol(objv, x);

                    index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);

                    continue;
                }

                double[][] Aix = BasicMath.appendrow(c, new double[][]{{0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
                double[][] bix = new double[][]{{zv[i]}, {x2min}, {x3min}};

                double[][] x = BasicMath.solve(Aix, bix);

                if (x[0][0] >= x1min && x[0][0] <= x1max) {
                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);
                }

                bix = new double[][]{{zv[i]}, {x2min}, {x3max}};
                x = BasicMath.solve(Aix, bix);

                if (x[0][0] >= x1min && x[0][0] <= x1max) {
                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);
                }

                bix = new double[][]{{zv[i]}, {x2max}, {x3max}};

                x = BasicMath.solve(Aix, bix);

                if (x[0][0] >= x1min && x[0][0] <= x1max) {
                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);
                }

                bix = new double[][]{{zv[i]}, {x2max}, {x3min}};

                x = BasicMath.solve(Aix, bix);

                if (x[0][0] >= x1min && x[0][0] <= x1max) {
                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);
                }

                Aix = BasicMath.appendrow(c, new double[][]{{1.0, 0.0, 0.0}, {0.0, 0.0, 1.0}});
                bix = new double[][]{{zv[i]}, {x1min}, {x3min}};

                x = BasicMath.solve(Aix, bix);

                if (x[1][0] >= x2min && x[1][0] <= x2max) {
                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);
                }

                bix = new double[][]{{zv[i]}, {x1max}, {x3min}};

                x = BasicMath.solve(Aix, bix);

                if (x[1][0] >= x2min && x[1][0] <= x2max) {
                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);
                }

                bix = new double[][]{{zv[i]}, {x1max}, {x3max}};

                x = BasicMath.solve(Aix, bix);

                if (x[1][0] >= x2min && x[1][0] <= x2max) {
                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);
                }

                bix = new double[][]{{zv[i]}, {x1min}, {x3max}};

                x = BasicMath.solve(Aix, bix);

                if (x[1][0] >= x2min && x[1][0] <= x2max) {
                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);
                }

                Aix = BasicMath.appendrow(c, new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}});
                bix = new double[][]{{zv[i]}, {x1min}, {x2min}};

                x = BasicMath.solve(Aix, bix);

                if (x[2][0] >= x3min && x[2][0] <= x3max) {
                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);
                }

                bix = new double[][]{{zv[i]}, {x1max}, {x2min}};

                x = BasicMath.solve(Aix, bix);

                if (x[2][0] >= x3min && x[2][0] <= x3max) {
                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);
                }

                bix = new double[][]{{zv[i]}, {x1max}, {x2max}};

                x = BasicMath.solve(Aix, bix);

                if (x[2][0] >= x3min && x[2][0] <= x3max) {
                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);
                }

                bix = new double[][]{{zv[i]}, {x1min}, {x2max}};

                x = BasicMath.solve(Aix, bix);

                if (x[2][0] >= x3min && x[2][0] <= x3max) {
                    objv = BasicMath.appendcol(objv, x);

                    int index = objv[0].length - 1;
                    objfaces[i] = BasicMath.append(objfaces[i], index);
                }
            }
        } else {
            for (int i = 0; i < m; i++) {
                if (A[i][0] != 0 && A[i][1] == 0 && A[i][2] == 0) {
                    double ba = b[i][0] / A[i][0];
                    double[][] x = {{ba}, {0.0}, {0.0}};

                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);

                    x = new double[][]{{ba}, {xrays[1][1]}, {0.0}};
                    infeasv = BasicMath.appendcol(infeasv, x);

                    index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);

                    x = new double[][]{{ba}, {xrays[1][1]}, {xrays[2][1]}};

                    infeasv = BasicMath.appendcol(infeasv, x);

                    index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);

                    x = new double[][]{{ba}, {0.0}, {xrays[2][1]}};

                    infeasv = BasicMath.appendcol(infeasv, x);

                    index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);

                    continue;
                }

                if (A[i][1] != 0 && A[i][0] == 0 && A[i][2] == 0) {
                    double ba = b[i][0] / A[i][1];
                    double[][] x = {{0.0}, {ba}, {0.0}};

                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);

                    x = new double[][]{{xrays[0][1]}, {ba}, {0.0}};

                    infeasv = BasicMath.appendcol(infeasv, x);

                    index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);

                    x = new double[][]{{xrays[0][1]}, {ba}, {xrays[2][1]}};

                    infeasv = BasicMath.appendcol(infeasv, x);

                    index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);

                    x = new double[][]{{0.0}, {ba}, {xrays[2][1]}};

                    infeasv = BasicMath.appendcol(infeasv, x);

                    index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);

                    continue;
                }

                if (A[i][2] != 0 && A[i][0] == 0 && A[i][1] == 0) {
                    double ba = b[i][0] / A[i][2];
                    double[][] x = {{0.0}, {0.0}, {ba}};

                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);

                    x = new double[][]{{0.0}, {xrays[1][1]}, {ba}};

                    infeasv = BasicMath.appendcol(infeasv, x);

                    index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);

                    x = new double[][]{{xrays[0][1]}, {xrays[1][1]}, {ba}};

                    infeasv = BasicMath.appendcol(infeasv, x);

                    index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);

                    x = new double[][]{{xrays[0][1]}, {0.0}, {ba}};

                    infeasv = BasicMath.appendcol(infeasv, x);

                    index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);

                    continue;
                }

                double[][] Ai = BasicMath.row(A, new int[][]{{i}});
                double[][] Aix = BasicMath.appendrow(Ai, new double[][]{{0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}});
                double[][] bix = new double[][]{{b[i][0]}, {0.0}, {0.0}};

                double[][] x = BasicMath.solve(Aix, bix);

                if (x[0][0] >= xrays[0][0] && x[0][0] <= xrays[0][1]) {
                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);
                }

                bix = new double[][]{{b[i][0]}, {0.0}, {xrays[2][1]}};

                x = BasicMath.solve(Aix, bix);

                if (x[0][0] >= xrays[0][0] && x[0][0] <= xrays[0][1]) {
                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);
                }

                bix = new double[][]{{b[i][0]}, {xrays[1][1]}, {xrays[2][1]}};

                x = BasicMath.solve(Aix, bix);

                if (x[0][0] >= xrays[0][0] && x[0][0] <= xrays[0][1]) {
                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);
                }

                bix = new double[][]{{b[i][0]}, {xrays[1][1]}, {0.0}};

                x = BasicMath.solve(Aix, bix);

                if (x[0][0] >= xrays[0][0] && x[0][0] <= xrays[0][1]) {
                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);
                }

                Aix = BasicMath.appendrow(Ai, new double[][]{{1.0, 0.0, 0.0}, {0.0, 0.0, 1.0}});
                bix = new double[][]{{b[i][0]}, {0.0}, {0.0}};

                x = BasicMath.solve(Aix, bix);

                if (x[1][0] >= xrays[1][0] && x[1][0] <= xrays[1][1]) {
                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);
                }

                bix = new double[][]{{b[i][0]}, {xrays[0][1]}, {0.0}};

                x = BasicMath.solve(Aix, bix);

                if (x[1][0] >= xrays[1][0] && x[1][0] <= xrays[1][1]) {
                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);
                }

                bix = new double[][]{{b[i][0]}, {xrays[0][1]}, {xrays[2][1]}};

                x = BasicMath.solve(Aix, bix);

                if (x[1][0] >= xrays[1][0] && x[1][0] <= xrays[1][1]) {
                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);
                }

                bix = new double[][]{{b[i][0]}, {0.0}, {xrays[2][1]}};

                x = BasicMath.solve(Aix, bix);

                if (x[1][0] >= xrays[1][0] && x[1][0] <= xrays[1][1]) {
                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);
                }

                Aix = BasicMath.appendrow(Ai, new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}});
                bix = new double[][]{{b[i][0]}, {0.0}, {0.0}};

                x = BasicMath.solve(Aix, bix);

                if (x[2][0] >= xrays[2][0] && x[2][0] <= xrays[2][1]) {
                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);
                }

                bix = new double[][]{{b[i][0]}, {xrays[0][1]}, {0.0}};

                x = BasicMath.solve(Aix, bix);

                if (x[2][0] >= xrays[2][0] && x[2][0] <= xrays[2][1]) {
                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);
                }

                bix = new double[][]{{b[i][0]}, {xrays[0][1]}, {xrays[1][1]}};

                x = BasicMath.solve(Aix, bix);

                if (x[2][0] >= xrays[2][0] && x[2][0] <= xrays[2][1]) {
                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);
                }

                bix = new double[][]{{b[i][0]}, {0.0}, {xrays[1][1]}};

                x = BasicMath.solve(Aix, bix);

                if (x[2][0] >= xrays[2][0] && x[2][0] <= xrays[2][1]) {
                    infeasv = BasicMath.appendcol(infeasv, x);

                    int index = infeasv[0].length - 1;
                    infaces[i] = BasicMath.append(infaces[i], index);
                }
            }
        }
    }

    /**
     * A method building the graphic model of the linear problem.
     *
     * @return the graphic model of the linear problem.
     */
    @Override
    public AbstractObject build() {
        MarkDot[] infeasmarks = null;

        if (infeasibles[0].length > 0) {
            infeasmarks = new MarkDot[infeasibles[0].length];

            for (int i = 0; i < infeasibles[0].length; i++) {
                SpacePoint point = new SpacePoint(infeasibles[0][i] / 10.0, infeasibles[1][i] / 10.0, infeasibles[2][i] / 10.0);

                infeasmarks[i] = new MarkDot(point);
            }
        }

        MarkDot[] feasmarks = null;

        if (feasibles[0].length > 0) {
            feasmarks = new MarkDot[feasibles[0].length];
            double[] z = new double[feasibles[0].length];

            for (int i = 0; i < feasibles[0].length; i++) {
                SpacePoint point = new SpacePoint(feasibles[0][i] / 10.0, feasibles[1][i] / 10.0, feasibles[2][i] / 10.0);

                feasmarks[i] = new MarkDot(point);

                z[i] = BasicMath.multiply(c, BasicMath.col(feasibles, new int[][]{{i}}))[0][0];
            }

            if (minmax == -1) {
                for (int i = 1; i < feasmarks.length; i++) {
                    for (int j = feasmarks.length - 1; j >= i; j--) {
                        if (z[j - 1] < z[j]) {
                            double tempz = z[j - 1];
                            z[j - 1] = z[j];
                            z[j] = tempz;

                            MarkDot temp = feasmarks[j - 1];
                            feasmarks[j - 1] = feasmarks[j];
                            feasmarks[j] = temp;
                        }
                    }
                }
            } else {
                for (int i = 1; i < feasmarks.length; i++) {
                    for (int j = feasmarks.length - 1; j >= i; j--) {
                        if (z[j - 1] > z[j]) {
                            double tempz = z[j - 1];
                            z[j - 1] = z[j];
                            z[j] = tempz;

                            MarkDot temp = feasmarks[j - 1];
                            feasmarks[j - 1] = feasmarks[j];
                            feasmarks[j] = temp;
                        }
                    }
                }
            }
        }

        SpacePoint s, e;

        Edge[] rays = new Edge[12];

        s = new SpacePoint(xrays[0][0] / 10.0, xrays[1][0] / 10.0, xrays[2][0] / 10.0);
        e = new SpacePoint(xrays[0][0] / 10.0, xrays[1][1] / 10.0, xrays[2][0] / 10.0);
        rays[0] = new Edge(s, e);

        s = new SpacePoint(xrays[0][0] / 10.0, xrays[1][1] / 10.0, xrays[2][0] / 10.0);
        e = new SpacePoint(xrays[0][0] / 10.0, xrays[1][1] / 10.0, xrays[2][1] / 10.0);
        rays[1] = new Edge(s, e);

        s = new SpacePoint(xrays[0][0] / 10.0, xrays[1][1] / 10.0, xrays[2][1] / 10.0);
        e = new SpacePoint(xrays[0][0] / 10.0, xrays[1][0] / 10.0, xrays[2][1] / 10.0);
        rays[2] = new Edge(s, e);

        s = new SpacePoint(xrays[0][0] / 10.0, xrays[1][0] / 10.0, xrays[2][1] / 10.0);
        e = new SpacePoint(xrays[0][0] / 10.0, xrays[1][0] / 10.0, xrays[2][0] / 10.0);
        rays[3] = new Edge(s, e);

        s = new SpacePoint(xrays[0][1] / 10.0, xrays[1][0] / 10.0, xrays[2][0] / 10.0);
        e = new SpacePoint(xrays[0][1] / 10.0, xrays[1][1] / 10.0, xrays[2][0] / 10.0);
        rays[4] = new Edge(s, e);

        s = new SpacePoint(xrays[0][1] / 10.0, xrays[1][1] / 10.0, xrays[2][0] / 10.0);
        e = new SpacePoint(xrays[0][1] / 10.0, xrays[1][1] / 10.0, xrays[2][1] / 10.0);
        rays[5] = new Edge(s, e);

        s = new SpacePoint(xrays[0][1] / 10.0, xrays[1][1] / 10.0, xrays[2][1] / 10.0);
        e = new SpacePoint(xrays[0][1] / 10.0, xrays[1][0] / 10.0, xrays[2][1] / 10.0);
        rays[6] = new Edge(s, e);

        s = new SpacePoint(xrays[0][1] / 10.0, xrays[1][0] / 10.0, xrays[2][1] / 10.0);
        e = new SpacePoint(xrays[0][1] / 10.0, xrays[1][0] / 10.0, xrays[2][0] / 10.0);
        rays[7] = new Edge(s, e);

        s = new SpacePoint(xrays[0][0] / 10.0, xrays[1][0] / 10.0, xrays[2][0] / 10.0);
        e = new SpacePoint(xrays[0][1] / 10.0, xrays[1][0] / 10.0, xrays[2][0] / 10.0);
        rays[8] = new Edge(s, e);

        s = new SpacePoint(xrays[0][0] / 10.0, xrays[1][0] / 10.0, xrays[2][1] / 10.0);
        e = new SpacePoint(xrays[0][1] / 10.0, xrays[1][0] / 10.0, xrays[2][1] / 10.0);
        rays[9] = new Edge(s, e);

        s = new SpacePoint(xrays[0][0] / 10.0, xrays[1][1] / 10.0, xrays[2][1] / 10.0);
        e = new SpacePoint(xrays[0][1] / 10.0, xrays[1][1] / 10.0, xrays[2][1] / 10.0);
        rays[10] = new Edge(s, e);

        s = new SpacePoint(xrays[0][0] / 10.0, xrays[1][1] / 10.0, xrays[2][0] / 10.0);
        e = new SpacePoint(xrays[0][1] / 10.0, xrays[1][1] / 10.0, xrays[2][0] / 10.0);
        rays[11] = new Edge(s, e);

        Surface[] surfaces = null;

        Surface[] objectives = null;

        double[][] A1 = BasicMath.appendrow(new double[][]{{1.0, 0.0, 0.0}, {0.0, 1.0, 0.0}, {0.0, 0.0, 1.0}}, A);

        if (adarap == 0 || adarap == 1) {
            surfaces = new Surface[0];

            for (int i = 0; i < faces.length; i++) {
                if (faces[i].length >= 3) {
                    SpacePoint[] points = new SpacePoint[faces[i].length];

                    for (int j = 0; j < faces[i].length; j++) {
                        int col = faces[i][j];
                        points[j] = new SpacePoint(feasv[0][col] / 10.0, feasv[1][col] / 10.0, feasv[2][col] / 10.0);
                    }

                    Surface[] surfacesnew = new Surface[surfaces.length + 1];
                    System.arraycopy(surfaces, 0, surfacesnew, 0, surfaces.length);

                    surfaces = surfacesnew;
                    surfaces[surfaces.length - 1] = new Surface(points, new SpacePoint(A1[i][0], A1[i][1], A1[i][2]));
                }
            }

            objectives = new Surface[0];

            for (int i = 0; i < objfaces.length; i++) {
                if (objfaces[i].length >= 3) {
                    SpacePoint[] points = new SpacePoint[objfaces[i].length];

                    for (int j = 0; j < objfaces[i].length; j++) {
                        int col = objfaces[i][j];
                        points[j] = new SpacePoint(objv[0][col] / 10.0, objv[1][col] / 10.0, objv[2][col] / 10.0);
                    }

                    Surface[] objectivesnew = new Surface[objectives.length + 1];
                    System.arraycopy(objectives, 0, objectivesnew, 0, objectives.length);

                    objectives = objectivesnew;
                    objectives[objectives.length - 1] = new Surface(points, new SpacePoint(c[0][0], c[0][1], c[0][2]));
                }
            }

            double[] z = new double[vertices[0].length];

            for (int i = 0; i < vertices[0].length; i++) {
                z[i] = BasicMath.multiply(c, BasicMath.col(vertices, new int[][]{{i}}))[0][0];
            }

            if (minmax == -1) {
                for (int i = 1; i < objectives.length; i++) {
                    for (int j = objectives.length - 1; j >= i; j--) {
                        if (z[j - 1] < z[j]) {
                            double tempz = z[j - 1];
                            z[j - 1] = z[j];
                            z[j] = tempz;

                            Surface temp = objectives[j - 1];
                            objectives[j - 1] = objectives[j];
                            objectives[j] = temp;
                        }
                    }
                }
            } else {
                for (int i = 1; i < objectives.length; i++) {
                    for (int j = objectives.length - 1; j >= i; j--) {
                        if (z[j - 1] > z[j]) {
                            double tempz = z[j - 1];
                            z[j - 1] = z[j];
                            z[j] = tempz;

                            Surface temp = objectives[j - 1];
                            objectives[j - 1] = objectives[j];
                            objectives[j] = temp;
                        }
                    }
                }
            }
        } else {
            surfaces = new Surface[0];

            for (int i = 0; i < infaces.length; i++) {
                if (infaces[i].length >= 3) {
                    SpacePoint[] points = new SpacePoint[infaces[i].length];

                    for (int j = 0; j < infaces[i].length; j++) {
                        int col = infaces[i][j];
                        points[j] = new SpacePoint(infeasv[0][col] / 10.0, infeasv[1][col] / 10.0, infeasv[2][col] / 10.0);
                    }

                    Surface[] surfacesnew = new Surface[surfaces.length + 1];
                    System.arraycopy(surfaces, 0, surfacesnew, 0, surfaces.length);

                    surfaces = surfacesnew;
                    surfaces[surfaces.length - 1] = new Surface(points, new SpacePoint(A[i][0], A[i][1], A[i][2]));
                }
            }
        }

        Feasible3DRegion scene = new Feasible3DRegion(adarap, surfaces, infeasmarks, feasmarks, rays, objectives);

        return scene;
    }
}
