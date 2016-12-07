package com.tkb.simplex.model;

/**
 * A definition of a half-space area defined by the given points.
 *
 * @author Akis Papadopoulos
 */
public class HalfSpace {

    protected SpacePoint a, b;

    protected SpacePoint normal;

    /**
     * A constructor creating a half-space defined between the given point.
     *
     * @param a the first point.
     * @param b the second point.
     */
    public HalfSpace(SpacePoint a, SpacePoint b) {
        this.a = new SpacePoint(a);
        this.b = new SpacePoint(b);

        normal = b.subtract(a).cross(SpacePoint.k).normalize();

        SpaceMatrix m = new SpaceMatrix();
        m.translate(a.x, a.y, a.z);
        normal = normal.transform(m);
    }

    /**
     * A method checking if the given point is inside the half-space.
     *
     * @param x the point x.
     * @return 1 if is inside otherwise -1.
     */
    public int isInside(SpacePoint x) {
        SpacePoint n = a.normal(b, x);

        double dz = n.z - a.z;

        if (dz > 0) {
            return 1;
        } else if (dz < 0) {
            return -1;
        } else {
            return 0;
        }
    }
}
