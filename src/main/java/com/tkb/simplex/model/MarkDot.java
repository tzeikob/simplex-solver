package com.tkb.simplex.model;

import java.awt.Point;

/**
 * A mark dot graphic object for points.
 *
 * @author Akis Papadopoulos
 */
public class MarkDot extends AbstractObject {

    public MarkDot() {
        super();
    }

    /**
     * A constructor creating a mark dot object given the center point.
     *
     * @param centre the center point.
     */
    public MarkDot(SpacePoint centre) {
        super();

        this.centre = centre;
    }

    /**
     * A constructor creating a mark dot given the radius and the angles from
     * the origin point of axes.
     *
     * @param radius the radius from the origin point.
     * @param phi the phi angle.
     * @param theta the theta angle.
     */
    public MarkDot(double radius, double phi, double theta) {
        super();

        phi = Math.toRadians(phi);
        theta = Math.toRadians(theta);

        double x = radius * Math.sin(phi) * Math.sin(theta);
        double y = radius * Math.cos(phi);
        double z = radius * Math.sin(phi) * Math.cos(theta);

        centre = new SpacePoint(x, y, z);
    }

    @Override
    public void render(AbstractView view) {
        Point c = view.project(centre);

        int r = 3;

        view.setColor(background, opacity);
        view.g.fillOval(c.x - r, c.y - r, 2 * r, 2 * r);

        view.g.setStroke(view.plain);

        view.setColor(foreground, opacity);
        view.g.drawOval(c.x - r, c.y - r, 2 * r, 2 * r);

        view.initialize();
    }

    @Override
    public void transform(SpaceMatrix m) {
        centre = centre.transform(m);
    }
}
