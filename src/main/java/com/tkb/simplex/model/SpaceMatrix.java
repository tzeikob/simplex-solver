package com.tkb.simplex.model;

/**
 * A matrix used as an transformation matrix for 2D/3D graphics.
 *
 * @author Akis Papadopoulos
 */
public class SpaceMatrix {

    protected double xx, yx, zx;

    protected double xy, yy, zy;

    protected double xz, yz, zz;

    protected double xo, yo, zo;

    public SpaceMatrix() {
        xx = 1.0;
        xy = 0.0;
        xz = 0.0;
        xo = 0.0;

        yx = 0.0;
        yy = 1.0;
        yz = 0.0;
        yo = 0.0;

        zx = 0.0;
        zy = 0.0;
        zz = 1.0;
        zo = 0.0;
    }

    public SpaceMatrix(SpaceMatrix matrix) {
        xx = matrix.xx;
        xy = matrix.xy;
        xz = matrix.xz;
        xo = matrix.xo;

        yx = matrix.yx;
        yy = matrix.yy;
        yz = matrix.yz;
        yo = matrix.yo;

        zx = matrix.zx;
        zy = matrix.zy;
        zz = matrix.zz;
        zo = matrix.zo;
    }

    public void initialize() {
        xx = 1.0;
        xy = 0.0;
        xz = 0.0;
        xo = 0.0;

        yx = 0.0;
        yy = 1.0;
        yz = 0.0;
        yo = 0.0;

        zx = 0.0;
        zy = 0.0;
        zz = 1.0;
        zo = 0.0;
    }

    /**
     * A method applying another transformation to the given matrix.
     *
     * @param matrix a transformation matrix.
     */
    public void multiply(SpaceMatrix matrix) {
        double xxn = xx * matrix.xx + yx * matrix.xy + zx * matrix.xz;
        double xyn = xy * matrix.xx + yy * matrix.xy + zy * matrix.xz;
        double xzn = xz * matrix.xx + yz * matrix.xy + zz * matrix.xz;
        double xon = xo * matrix.xx + yo * matrix.xy + zo * matrix.xz + matrix.xo;

        double yxn = xx * matrix.yx + yx * matrix.yy + zx * matrix.yz;
        double yyn = xy * matrix.yx + yy * matrix.yy + zy * matrix.yz;
        double yzn = xz * matrix.yx + yz * matrix.yy + zz * matrix.yz;
        double yon = xo * matrix.yx + yo * matrix.yy + zo * matrix.yz + matrix.yo;

        double zxn = xx * matrix.zx + yx * matrix.zy + zx * matrix.zz;
        double zyn = xy * matrix.zx + yy * matrix.zy + zy * matrix.zz;
        double zzn = xz * matrix.zx + yz * matrix.zy + zz * matrix.zz;
        double zon = xo * matrix.zx + yo * matrix.zy + zo * matrix.zz + matrix.zo;

        xx = xxn;
        xy = xyn;
        xz = xzn;
        xo = xon;

        yx = yxn;
        yy = yyn;
        yz = yzn;
        yo = yon;

        zx = zxn;
        zy = zyn;
        zz = zzn;
        zo = zon;
    }

    public void translate(double Tx, double Ty, double Tz) {
        xo += Tx;
        yo += Ty;
        zo += Tz;
    }

    public void scale(double S) {
        xx *= S;
        xy *= S;
        xz *= S;
        xo *= S;

        yx *= S;
        yy *= S;
        yz *= S;
        yo *= S;

        zx *= S;
        zy *= S;
        zz *= S;
        zo *= S;
    }

    public void scale(double Sx, double Sy, double Sz) {
        xx *= Sx;
        xy *= Sx;
        xz *= Sx;
        xo *= Sx;

        yx *= Sy;
        yy *= Sy;
        yz *= Sy;
        yo *= Sy;

        zx *= Sz;
        zy *= Sz;
        zz *= Sz;
        zo *= Sz;
    }

    public void shareX(double Sxy, double Sxz) {
        yx += xx * Sxy;
        yy += xy * Sxy;
        yz += xz * Sxy;
        yo += xo * Sxy;

        zx += xx * Sxz;
        zy += xy * Sxz;
        zz += xz * Sxz;
        zo += xo * Sxz;
    }

    public void shareY(double Syx, double Syz) {
        xx += yx * Syx;
        xy += yy * Syx;
        xz += yz * Syx;
        xo += yo * Syx;

        zx += yx * Syz;
        zy += yy * Syz;
        zz += yz * Syz;
        zo += yo * Syz;
    }

    public void shareZ(double Szx, double Szy) {
        xx += zx * Szx;
        xy += zy * Szx;
        xz += zz * Szx;
        xo += zo * Szx;

        yx += zx * Szy;
        yy += zy * Szy;
        yz += zz * Szy;
        yo += zo * Szy;
    }

    public void rotateX(double theta) {
        double rads = Math.toRadians(theta);

        double cos = Math.cos(rads);
        double sin = Math.sin(rads);

        double yxn = yx * cos - zx * sin;
        double yyn = yy * cos - zy * sin;
        double yzn = yz * cos - zz * sin;
        double yon = yo * cos - zo * sin;

        double zxn = yx * sin + zx * cos;
        double zyn = yy * sin + zy * cos;
        double zzn = yz * sin + zz * cos;
        double zon = yo * sin + zo * cos;

        yx = yxn;
        yy = yyn;
        yz = yzn;
        yo = yon;

        zx = zxn;
        zy = zyn;
        zz = zzn;
        zo = zon;
    }

    public void rotateY(double theta) {
        double rads = Math.toRadians(theta);

        double cos = Math.cos(rads);
        double sin = Math.sin(rads);

        double xxn = xx * cos + zx * sin;
        double xyn = xy * cos + zy * sin;
        double xzn = xz * cos + zz * sin;
        double xon = xo * cos + zo * sin;

        double zxn = zx * cos - xx * sin;
        double zyn = zy * cos - xy * sin;
        double zzn = zz * cos - xz * sin;
        double zon = zo * cos - xo * sin;

        xx = xxn;
        xy = xyn;
        xz = xzn;
        xo = xon;

        zx = zxn;
        zy = zyn;
        zz = zzn;
        zo = zon;
    }

    public void rotateZ(double theta) {
        double rads = Math.toRadians(theta);

        double cos = Math.cos(rads);
        double sin = Math.sin(rads);

        double xxn = xx * cos - yx * sin;
        double xyn = xy * cos - yy * sin;
        double xzn = xz * cos - yz * sin;
        double xon = xo * cos - yo * sin;

        double yxn = xx * sin + yx * cos;
        double yyn = xy * sin + yy * cos;
        double yzn = xz * sin + yz * cos;
        double yon = xo * sin + yo * cos;

        xx = xxn;
        xy = xyn;
        xz = xzn;
        xo = xon;

        yx = yxn;
        yy = yyn;
        yz = yzn;
        yo = yon;
    }

    public void reflect() {
        xx *= -1.0;
        xy *= -1.0;
        xz *= -1.0;
        xo *= -1.0;

        yx *= -1.0;
        yy *= -1.0;
        yz *= -1.0;
        yo *= -1.0;

        zx *= -1.0;
        zy *= -1.0;
        zz *= -1.0;
        zo *= -1.0;
    }

    public void reflectXY() {
        zx *= -1.0;
        zy *= -1.0;
        zz *= -1.0;
        zo *= -1.0;
    }

    public void reflectXZ() {
        yx *= -1.0;
        yy *= -1.0;
        yz *= -1.0;
        yo *= -1.0;
    }

    public void reflectYZ() {
        xx *= -1.0;
        xy *= -1.0;
        xz *= -1.0;
        xo *= -1.0;
    }

    @Override
    public String toString() {
        String str
                = "[" + xx + ", " + yx + ", " + zx + ", " + 0.0 + "]\n"
                + "[" + xy + ", " + yy + ", " + zy + ", " + 0.0 + "]\n"
                + "[" + xz + ", " + yz + ", " + zz + ", " + 0.0 + "]\n"
                + "[" + xo + ", " + yo + ", " + zo + ", " + 1.0 + "]\n";

        return str;
    }
}
