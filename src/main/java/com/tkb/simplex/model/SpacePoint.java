package com.tkb.simplex.model;

/**
 * A 2D/3D point definition.
 *
 * @author Akis Papadopoulos
 */
public class SpacePoint {

    protected static SpacePoint o = new SpacePoint(0.0, 0.0, 0.0);

    protected static SpacePoint i = new SpacePoint(1.0, 0.0, 0.0);
    protected static SpacePoint j = new SpacePoint(0.0, 1.0, 0.0);
    protected static SpacePoint k = new SpacePoint(0.0, 0.0, 1.0);

    protected double x;
    protected double y;
    protected double z;

    public SpacePoint() {
        x = 0.0;
        y = 0.0;
        z = 0.0;
    }

    public SpacePoint(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public SpacePoint(SpacePoint point) {
        x = point.x;
        y = point.y;
        z = point.z;
    }

    public SpacePoint add(SpacePoint point) {
        double nx = x + point.x;
        double ny = y + point.y;
        double nz = z + point.z;

        return new SpacePoint(nx, ny, nz);
    }

    public SpacePoint subtract(SpacePoint point) {
        double nx = x - point.x;
        double ny = y - point.y;
        double nz = z - point.z;

        return new SpacePoint(nx, ny, nz);
    }

    public SpacePoint scale(double dx, double dy, double dz) {
        double nx = x * dx;
        double ny = y * dy;
        double nz = z * dz;

        return new SpacePoint(nx, ny, nz);
    }

    public SpacePoint scale(double d) {
        double nx = x * d;
        double ny = y * d;
        double nz = z * d;

        return new SpacePoint(nx, ny, nz);
    }

    public double dot(SpacePoint point) {
        return x * point.x + y * point.y + z * point.z;
    }

    public SpacePoint cross(SpacePoint point) {
        double nx = y * point.z - z * point.y;
        double ny = z * point.x - x * point.z;
        double nz = x * point.y - y * point.x;

        return new SpacePoint(nx, ny, nz);
    }

    public double norm() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public SpacePoint normalize() {
        double n = this.norm();

        double nx = x / n;
        double ny = y / n;
        double nz = z / n;

        return new SpacePoint(nx, ny, nz);
    }

    public SpacePoint transform(SpaceMatrix m) {
        double xt = x * m.xx + y * m.xy + z * m.xz + m.xo;
        double yt = x * m.yx + y * m.yy + z * m.yz + m.yo;
        double zt = x * m.zx + y * m.zy + z * m.zz + m.zo;

        return new SpacePoint(xt, yt, zt);
    }

    public SpacePoint normal(SpacePoint a, SpacePoint b) {
        SpaceMatrix m = new SpaceMatrix();
        m.translate(-x, -y, -z);

        SpacePoint at = a.transform(m);
        SpacePoint bt = b.transform(m);

        SpacePoint n = at.cross(bt).normalize();

        m.initialize();
        m.translate(x, y, z);
        n = n.transform(m);

        return n;
    }

    public SpacePoint extend(SpacePoint p, double lamda) {
        SpaceMatrix m = new SpaceMatrix();
        m.translate(-x, -y, -z);

        SpacePoint a = this.transform(m);
        SpacePoint b = p.transform(m);

        SpacePoint ext = a.add(b).scale(lamda);

        m.initialize();
        m.translate(x, y, z);

        ext = ext.transform(m);

        return ext;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + "]";
    }
}
