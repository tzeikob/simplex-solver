package com.tkb.simplex.math;

import com.tkb.simplex.model.SpacePoint;
import com.tkb.simplex.model.AbstractObject;
import com.tkb.simplex.model.Edge;
import com.tkb.simplex.model.MarkDot;
import com.tkb.simplex.model.Polygon;
import com.tkb.simplex.model.Feasible2DRegion;

/**
 * A basic 2D linear problem solver.
 *
 * @author Akis Papadopoulos
 */
public class Basic2DSolver extends AbstractSolver {

    public Basic2DSolver(double minmax, double[][] A, double[][] c, double[][] b, double[][] eqin) {
        super(minmax, A, c, b, eqin);
    }

    /**
     * A method resolving the given linear problem.
     */
    @Override
    public void solve() {
        int m = A.length;
        int n = A[0].length;

        double[][] A1 = BasicMath.appendrow(new double[][]{{1.0, 0.0}, {0.0, 1.0}}, A);
        double[][] b1 = BasicMath.appendrow(new double[][]{{0.0}, {0.0}}, b);
        double[][] eqin1 = BasicMath.appendrow(new double[][]{{1}, {1}}, eqin);

        for (int i = 0; i < m + n - 1; i++) {
            for (int j = i + 1; j < m + n; j++) {
                int[][] ij = {{i, j}};

                double[][] Aij = BasicMath.row(A1, ij);

                if (BasicMath.det(Aij) != 0) {
                    double[][] bij = BasicMath.row(b1, ij);

                    double[][] x = BasicMath.solve(Aij, bij);
                    double[][] Ax = BasicMath.rowx(A1, ij);
                    double[][] bx = BasicMath.rowx(b1, ij);
                    double[][] eqinx = BasicMath.rowx(eqin1, ij);

                    if (BasicMath.isFeasible(Ax, bx, eqinx, x)) {
                        feasibles = BasicMath.appendcol(feasibles, x);
                    } else {
                        infeasibles = BasicMath.appendcol(infeasibles, x);
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
        }

        for (int i = 0; i < m; i++) {
            double[][] Aix;
            double[][] bix;
            double[][] x;
            double[][] Ai = BasicMath.row(A, new int[][]{{i}});

            Aix = BasicMath.appendrow(Ai, new double[][]{{1.0, 0.0}});
            bix = new double[][]{{b[i][0]}, {xrays[0][1]}};

            x = BasicMath.solve(Aix, bix);

            if (!Double.isInfinite(x[0][0]) && !Double.isNaN(x[0][0]) && BasicMath.isFeasible(A, b, eqin, x) && x[1][0] <= xrays[1][1]) {
                ponrays = BasicMath.appendcol(ponrays, x);
            }

            Aix = BasicMath.appendrow(Ai, new double[][]{{0.0, 1.0}});
            bix = new double[][]{{b[i][0]}, {xrays[1][1]}};

            x = BasicMath.solve(Aix, bix);

            if (!Double.isInfinite(x[0][0]) && !Double.isNaN(x[0][0]) && BasicMath.isFeasible(A, b, eqin, x) && x[0][0] <= xrays[0][1]) {
                ponrays = BasicMath.appendcol(ponrays, x);
            }
        }

        double[][] x;

        x = new double[][]{{xrays[0][1]}, {0.0}};

        if (BasicMath.isFeasible(A, b, eqin, x)) {
            ponrays = BasicMath.appendcol(ponrays, x);
        }

        x = new double[][]{{0.0}, {xrays[1][1]}};

        if (BasicMath.isFeasible(A, b, eqin, x)) {
            ponrays = BasicMath.appendcol(ponrays, x);
        }

        x = new double[][]{{xrays[0][1]}, {xrays[1][1]}};

        if (BasicMath.isFeasible(A, b, eqin, x)) {
            ponrays = BasicMath.appendcol(ponrays, x);
        }

        if (ponrays[0].length > 1) {
            ponrays = BasicMath.uniquecol(ponrays);
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
                    xoptimal = new double[2][0];
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
        }
    }

    /**
     * A method building the geometric model of the linear problem.
     *
     * @return the graphic model of the linear problem.
     */
    @Override
    public AbstractObject build() {
        Edge[] inequalities = new Edge[A.length];

        for (int i = 0; i < A.length; i++) {
            double[][] Ai = BasicMath.row(A, new int[][]{{i}});
            double bi = b[i][0];
            double eqini = eqin[i][0];

            inequalities[i] = createEdge(Ai, bi, eqini);
        }

        MarkDot[] infeasmarks = null;

        if (infeasibles[0].length > 0) {
            infeasmarks = new MarkDot[infeasibles[0].length];

            for (int i = 0; i < infeasibles[0].length; i++) {
                SpacePoint point = new SpacePoint(infeasibles[0][i] / 10.0, infeasibles[1][i] / 10.0, 0.0);

                infeasmarks[i] = new MarkDot(point);
            }
        }

        MarkDot[] feasmarks = null;

        if (feasibles[0].length > 0) {
            feasmarks = new MarkDot[feasibles[0].length];

            double[] z = new double[feasibles[0].length];

            for (int i = 0; i < feasibles[0].length; i++) {
                SpacePoint point = new SpacePoint(feasibles[0][i] / 10.0, feasibles[1][i] / 10.0, 0.0);

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

        Edge[] rays = new Edge[4];

        s = new SpacePoint(xrays[0][0] / 10.0, xrays[1][0] / 10.0, 0.0);
        e = new SpacePoint(xrays[0][0] / 10.0, xrays[1][1] / 10.0, 0.0);
        rays[0] = new Edge(s, e);

        s = new SpacePoint(xrays[0][1] / 10.0, xrays[1][0] / 10.0, 0.0);
        e = new SpacePoint(xrays[0][1] / 10.0, xrays[1][1] / 10.0, 0.0);
        rays[1] = new Edge(s, e);

        s = new SpacePoint(xrays[0][0] / 10.0, xrays[1][0] / 10.0, 0.0);
        e = new SpacePoint(xrays[0][1] / 10.0, xrays[1][0] / 10.0, 0.0);
        rays[2] = new Edge(s, e);

        s = new SpacePoint(xrays[0][0] / 10.0, xrays[1][1] / 10.0, 0.0);
        e = new SpacePoint(xrays[0][1] / 10.0, xrays[1][1] / 10.0, 0.0);
        rays[3] = new Edge(s, e);

        Polygon region = null;

        if (adarap != -1) {
            SpacePoint[] p = new SpacePoint[vertices[0].length];
            for (int i = 0; i < vertices[0].length; i++) {
                p[i] = new SpacePoint(vertices[0][i] / 10.0, vertices[1][i] / 10.0, 0.0);
            }

            region = new Polygon(p);

            boolean gradient;

            if (adarap == 0 && ponrays[0].length == 0) {
                gradient = false;
            } else {
                gradient = true;
            }

            if (gradient) {
                SpacePoint o, a, b;

                //a = new SpacePoint(0.0, 0.0, 0.0);
                //b = a.extend(new SpacePoint(c[0][1]/10.0, c[0][1]/10.0, 0.0), 2.5);
                o = new SpacePoint(0.0, 0.0, 0.0);
                b = new SpacePoint(xrays[0][1] / 10.0, xrays[1][1] / 10.0, 0.0);
                a = o.add(b).scale(0.5);
                region.setGradientAngle(a, b);
            }
        }

        Edge[] objectives = null;

        if (adarap != -1) {
            objectives = new Edge[vertices[0].length];
            double[] z = new double[vertices[0].length];

            for (int i = 0; i < vertices[0].length; i++) {
                z[i] = BasicMath.multiply(c, BasicMath.col(vertices, new int[][]{{i}}))[0][0];

                objectives[i] = createEdge(c, z[i], minmax);
            }

            if (minmax == -1) {
                for (int i = 1; i < objectives.length; i++) {
                    for (int j = objectives.length - 1; j >= i; j--) {
                        if (z[j - 1] < z[j]) {
                            double tempz = z[j - 1];
                            z[j - 1] = z[j];
                            z[j] = tempz;

                            Edge temp = objectives[j - 1];
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

                            Edge temp = objectives[j - 1];
                            objectives[j - 1] = objectives[j];
                            objectives[j] = temp;
                        }
                    }
                }
            }
        }

        AbstractObject animation = new Feasible2DRegion(adarap, inequalities, infeasmarks, feasmarks, rays, region, objectives);

        return animation;
    }

    /**
     * A method creating an edge given the i restriction of the linear problem.
     *
     * @param Ai the i-th row of the A matrix.
     * @param bi the i-th value of the b vector matrix.
     * @param eqini the i-th equation indicator.
     * @return an edge.
     */
    private Edge createEdge(double[][] Ai, double bi, double eqini) {
        if (Ai[0][0] != 0 && Ai[0][1] == 0) {
            double ba = bi / Ai[0][0];

            SpacePoint start = new SpacePoint(ba / 10.0, xrays[1][0] / 10.0, 0.0);
            SpacePoint end = new SpacePoint(ba / 10.0, xrays[1][1] / 10.0, 0.0);

            SpacePoint a = new SpacePoint(Ai[0][0] / 10.0, Ai[0][1] / 10.0, 0.0);

            if (eqini == -1) {
                a = a.scale(-1);
            }

            return new Edge(start, end, a);
        }

        if (Ai[0][1] != 0 && Ai[0][0] == 0) {
            double ba = bi / Ai[0][1];

            SpacePoint start = new SpacePoint(xrays[0][0] / 10.0, ba / 10.0, 0.0);
            SpacePoint end = new SpacePoint(xrays[0][1] / 10.0, ba / 10.0, 0.0);

            SpacePoint a = new SpacePoint(Ai[0][0] / 10.0, Ai[0][1] / 10.0, 0.0);

            if (eqini == -1) {
                a = a.scale(-1);
            }

            return new Edge(start, end, a);
        }

        double[][] Ax, bx;
        double[][] x = new double[2][0];

        Ax = BasicMath.appendrow(new double[][]{{1.0, 0.0}}, Ai);
        bx = new double[][]{{xrays[0][0]}, {bi}};
        x = BasicMath.appendcol(x, BasicMath.solve(Ax, bx));

        Ax = BasicMath.appendrow(new double[][]{{1.0, 0.0}}, Ai);
        bx = new double[][]{{xrays[0][1]}, {bi}};
        x = BasicMath.appendcol(x, BasicMath.solve(Ax, bx));

        Ax = BasicMath.appendrow(new double[][]{{0.0, 1.0}}, Ai);
        bx = new double[][]{{xrays[1][0]}, {bi}};
        x = BasicMath.appendcol(x, BasicMath.solve(Ax, bx));

        Ax = BasicMath.appendrow(new double[][]{{0.0, 1.0}}, Ai);
        bx = new double[][]{{xrays[1][1]}, {bi}};
        x = BasicMath.appendcol(x, BasicMath.solve(Ax, bx));

        int min = 0;

        for (int i = 1; i < x[0].length; i++) {
            if (x[0][i] < x[0][min]) {
                min = i;
            }
        }

        int max = 0;

        for (int i = 1; i < x[0].length; i++) {
            if (x[0][i] > x[0][max]) {
                max = i;
            }
        }

        SpacePoint start = new SpacePoint(x[0][min] / 10.0, x[1][min] / 10.0, 0.0);
        SpacePoint end = new SpacePoint(x[0][max] / 10.0, x[1][max] / 10.0, 0.0);

        SpacePoint a = new SpacePoint(Ai[0][0] / 10.0, Ai[0][1] / 10.0, 0.0);

        if (eqini == -1) {
            a = a.scale(-1);
        }

        return new Edge(start, end, a);
    }
}
