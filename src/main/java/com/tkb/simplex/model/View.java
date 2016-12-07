package com.tkb.simplex.model;

import java.awt.Point;
import java.awt.Color;

/**
 * A basic 2D/3D view.
 * 
 * @author Akis Papadopoulos
 */
public class View extends AbstractView {

    public View() {
        super();

        setViewpoint(new SpacePoint(1.0, 1.0, 1.0), new SpacePoint(0.0, 1.0, 0.0));
    }

    public View(SpacePoint dirn) {
        super();

        setViewpoint(dirn, new SpacePoint(0.0, 1.0, 0.0));
    }

    public View(SpacePoint dirn, SpacePoint up) {
        super();

        setViewpoint(dirn, up);
    }

    public View(double radius, double phi, double theta) {
        super();

        setViewpoint(radius, phi, theta);
    }

    /**
     * A method setting the view point camera of the view.
     *
     * @param dirn the direction point vector.
     * @param up the up point.
     */
    @Override
    public void setViewpoint(SpacePoint dirn, SpacePoint up) {
        w = dirn.normalize();
        v = (up.subtract(w.scale(w.dot(up)))).normalize();
        u = v.cross(w);
    }

    /**
     * A method setting the viewpoint camera of the view.
     *
     * @param radius the radius distance.
     * @param phi the phi angle value.
     * @param theta the theta angle value.
     */
    @Override
    public void setViewpoint(double radius, double phi, double theta) {
        phi = Math.toRadians(phi);
        theta = Math.toRadians(theta);

        double x = radius * Math.sin(phi) * Math.sin(theta);
        double y = radius * Math.cos(phi);
        double z = radius * Math.sin(phi) * Math.cos(theta);

        SpacePoint dirn = new SpacePoint(x, y, z);
        SpacePoint up = new SpacePoint(0.0, 1.0, 0.0);

        w = dirn.normalize();
        v = (up.subtract(w.scale(w.dot(up)))).normalize();
        u = v.cross(w);
    }

    /**
     * A method adjusting the view point camera by the given x and y amounts.
     *
     * @param dx the amount in horizontal axis.
     * @param dy the amount in vertical axis.
     */
    @Override
    public void adjust(double dx, double dy) {
        dx /= -img.getWidth();
        dy /= img.getHeight();

        SpacePoint dirn = w.add(u.scale(dx).add(v.scale(dy)));
        setViewpoint(dirn, v);
    }

    /**
     * A method zooming in and out of the view given the scale factor, where
     * negative values means zoom out and positive zoom in.
     *
     * @param scale the zoom scale factor.
     */
    @Override
    public void zoom(double scale) {
        if (scale != 0.0) {
            scale = 1 / Math.abs(scale);
        } else {
            scale = 1.0;
        }

        wx -= (scale - 1) * wwidth / 2.0;
        wy -= (scale - 1) * wheight / 2.0;

        wwidth *= scale;
        wheight *= scale;
    }

    /**
     * A method initializes the zoom in order to get the full view area.
     *
     * @param scale the scale factor.
     */
    @Override
    public void dolly(double scale) {
        if (scale != 0.0) {
            scale = 1 / Math.abs(scale);
        } else {
            scale = 1.0;
        }

        dinverse /= scale;
    }

    /**
     * A method translates the view point by the amount of x and y values.
     *
     * @param dx the amount of horizontal value.
     * @param dy the amount of vertical value.
     */
    @Override
    public void pan(double dx, double dy) {
        double xscale = img.getWidth() / wwidth;
        double yscale = img.getHeight() / wheight;

        dx /= -xscale * wwidth;
        dy /= yscale * wheight;

        wx += dx * wwidth;
        wy += dy * wheight;
    }

    /**
     * A method to project a given point into the view.
     *
     * @param point the point to be projected.
     * @return the projected point.
     */
    @Override
    public Point project(SpacePoint point) {
        double divisor = 1 - dinverse * w.dot(point);

        double xscale = img.getWidth() / wwidth;
        double yscale = img.getHeight() / wheight;

        int x = (int) ((u.dot(point) / divisor - wx) * xscale);
        int y = img.getHeight() - (int) ((v.dot(point) / divisor - wy) * yscale);

        return new Point(x, y);
    }

    /**
     * A method checking if the given point is in front visible face.
     *
     * @param normal the normal point.
     * @param corner the corner point.
     * @return true if visible otherwise false.
     */
    @Override
    public boolean isFrontFace(SpacePoint normal, SpacePoint corner) {
        if (dinverse == 0) {
            return normal.dot(w) > 0;
        } else {
            return normal.dot(w.scale(1 / dinverse).subtract(corner)) > 0;
        }
    }

    /**
     * A method returning the depth of the point in the given view.
     *
     * @param point the point.
     * @return the value of depth.
     */
    @Override
    public double depth(SpacePoint point) {
        if (dinverse == 0) {
            return w.dot(point);
        } else {
            return w.scale(1 / dinverse).subtract(point).norm();
        }
    }

    /**
     * A method calculating the shade color given the point of view and the
     * front and back color shades.
     *
     * @param front the front color shade.
     * @param back the back color shade.
     * @param normal the normal point.
     * @param corner the corner point.
     * @return
     */
    @Override
    public Color getShade(Color front, Color back, SpacePoint normal, SpacePoint corner) {
        normal = normal.normalize();

        double angle;

        if (dinverse == 0) {
            angle = normal.dot(w);
        } else {
            angle = normal.dot(w.scale(1 / dinverse).subtract(corner).normalize());
        }

        Color sc;

        if (angle > 0) {
            sc = front;
        } else {
            sc = back;
        }

        angle = Math.abs(angle);

        Color lc = light;
        Color a = ambient;

        int red = clamp(a.getRed() + angle * sc.getRed() * lc.getRed() / 255.0);
        int green = clamp(a.getGreen() + angle * sc.getGreen() * lc.getGreen() / 255.0);
        int blue = clamp(a.getBlue() + angle * sc.getBlue() * lc.getBlue() / 255.0);

        return new Color(red, green, blue);
    }

    /**
     * A method ensuring the given value of color channel falls with the range
     * of a 255 RGB value.
     *
     * @param value an RGB color channel value.
     * @return the clamped value.
     */
    protected static int clamp(double value) {
        if (value > 255) {
            return 255;
        } else if (value < 0) {
            return 0;
        } else {
            return (int) value;
        }
    }
}
