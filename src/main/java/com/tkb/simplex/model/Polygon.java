package com.tkb.simplex.model;

import java.awt.Point;
import java.awt.Color;
import java.awt.GradientPaint;

/**
 * A convex hull polygon graphics object.
 *
 * @author Akis Papadopoulos
 */
public class Polygon extends ConvexHull {

    protected SpacePoint gradientFrom, gradientTo;

    public Polygon(SpacePoint[] data) {
        super();

        this.data = data;

        build();

        centre = vertices[0];

        for (int i = 1; i < vertices.length; i++) {
            centre = centre.add(vertices[i]);
        }

        centre = centre.scale(1.0 / vertices.length);
    }

    public void build() {
        SpacePoint bottom = data[0];

        for (int i = 1; i < data.length; i++) {
            if (data[i].y < bottom.y) {
                bottom = data[i];
            }
        }

        SpacePoint p = bottom;

        vertices = new SpacePoint[1];
        vertices[0] = p;

        do {
            int i = 0;
            if (data[0] == p) {
                i = 1;
            }

            SpacePoint candidate = data[i];

            HalfSpace h = new HalfSpace(p, candidate);

            for (i = i + 1; i < data.length; i++) {
                if (data[i] != p && h.isInside(data[i]) < 0) {
                    candidate = data[i];
                    h = new HalfSpace(p, candidate);
                }
            }

            SpacePoint[] verticesnew = new SpacePoint[vertices.length + 1];
            System.arraycopy(vertices, 0, verticesnew, 0, vertices.length);

            vertices = verticesnew;
            vertices[vertices.length - 1] = candidate;

            p = candidate;
        } while (p != bottom);
    }

    public void setGradientAngle(SpacePoint from, SpacePoint to) {
        gradientFrom = from;
        gradientTo = to;
    }

    @Override
    public void render(AbstractView view) {
        int[] x = new int[vertices.length + 1];
        int[] y = new int[vertices.length + 1];

        for (int i = 0; i < vertices.length; i++) {
            x[i] = view.project(vertices[i]).x;
            y[i] = view.project(vertices[i]).y;
        }

        x[x.length - 1] = x[0];
        y[y.length - 1] = y[0];

        if (gradientFrom != null) {
            Point start = view.project(gradientFrom);
            Point end = view.project(gradientTo);
            GradientPaint gradient = new GradientPaint((float) start.x, (float) start.y, background, (float) end.x, (float) end.y, new Color(255, 255, 255, 0));
            view.g.setPaint(gradient);
        } else {
            view.setColor(background, opacity);
        }

        view.g.fillPolygon(x, y, x.length);

        view.initialize();
    }

    @Override
    public void transform(SpaceMatrix m) {
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = vertices[i].transform(m);
        }

        centre = centre.transform(m);
    }
}
