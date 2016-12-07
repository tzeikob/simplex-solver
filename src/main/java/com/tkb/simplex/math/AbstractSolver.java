package com.tkb.simplex.math;

import com.tkb.simplex.model.AbstractObject;

/**
 * An abstract linear problem solver.
 *
 * @author Akis Papadopoulos
 */
public abstract class AbstractSolver {

    protected double minmax;

    protected double[][] A;

    protected double[][] c;

    protected double[][] b;

    protected double[][] eqin;

    protected double adarap;

    protected double zoptimal;

    protected double[][] xoptimal;

    protected double[][] feasibles;

    protected double[][] infeasibles;

    protected double[][] ponrays;

    protected double[][] vertices;

    protected double[][] xrays;

    public AbstractSolver(double minmax, double[][] A, double[][] c, double[][] b, double[][] eqin) {
        this.minmax = minmax;
        this.A = A;
        this.c = c;
        this.b = b;
        this.eqin = eqin;
        int n = A[0].length;

        adarap = -1;
        zoptimal = Double.NaN;
        xoptimal = new double[n][0];
        feasibles = new double[n][0];
        infeasibles = new double[n][0];
        ponrays = new double[n][0];
        vertices = new double[n][0];
        xrays = new double[n][2];
    }

    /**
     * An abstract method resolving the linear problem,
     */
    public abstract void solve();

    /**
     * An abstract method building the graphic model of the geometric solution
     * of the linear problem.
     *
     * @return the geometric graphic model of the solution.
     */
    public abstract AbstractObject build();

    @Override
    public String toString() {
        String str = "adarap:\n" + "[" + adarap + "]\n\n";
        str += "zoptimal:\n" + "[" + zoptimal + "]\n\n";
        str += "xoptimal:\n" + BasicMath.toString(xoptimal) + "\n";
        str += "feasibles:\n" + BasicMath.toString(feasibles) + "\n";
        str += "infeasibles:\n" + BasicMath.toString(infeasibles) + "\n";
        str += "ponrays:\n" + BasicMath.toString(ponrays) + "\n";
        str += "vertices:\n" + BasicMath.toString(vertices) + "\n";
        str += "rays:\n" + BasicMath.toString(xrays);

        return str;
    }
}
