package com.tkb.simplex.model;

import java.awt.Point;
import java.awt.Color;

/**
 * A graphic cube object.
 *
 * @author Akis Papadopoulos
 */
public class Cube extends AbstractObject {

    protected SpacePoint[] vertices = {
        new SpacePoint(-1.0, -1.0, 1.0),
        new SpacePoint(1.0, -1.0, 1.0),
        new SpacePoint(1.0, 1.0, 1.0),
        new SpacePoint(-1.0, 1.0, 1.0),
        new SpacePoint(-1.0, 1.0, -1.0),
        new SpacePoint(1.0, 1.0, -1.0),
        new SpacePoint(1.0, -1.0, -1.0),
        new SpacePoint(-1.0, -1.0, -1.0)
    };

    protected int[][] surfaces = {
        {0, 1, 2, 3},
        {0, 3, 4, 7},
        {7, 4, 5, 6},
        {6, 5, 2, 1},
        {1, 0, 7, 6},
        {2, 5, 4, 3}
    };

    public Cube() {
        super();
    }

    public Cube(SpacePoint centre, double radius) {
        super();

        SpaceMatrix m = new SpaceMatrix();
        m.scale(radius);
        m.translate(centre.x, centre.y, centre.z);

        this.transform(m);
    }

    @Override
    public void render(AbstractView view) {
        Point p[] = new Point[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            p[i] = view.project(vertices[i]);
        }

        for (int i = 0; i < surfaces.length; i++) {
            SpacePoint vertex = vertices[surfaces[i][0]];
            SpacePoint a = vertices[surfaces[i][1]];
            SpacePoint b = vertices[surfaces[i][2]];

            SpacePoint normal = a.subtract(vertex).cross(b.subtract(vertex));

            if (view.isFrontFace(normal, vertex)) {
                int[] x = new int[surfaces[i].length + 1];
                int[] y = new int[surfaces[i].length + 1];

                for (int j = 0; j < surfaces[i].length; j++) {
                    x[j] = p[surfaces[i][j]].x;
                    y[j] = p[surfaces[i][j]].y;
                }

                x[surfaces[i].length] = x[0];
                y[surfaces[i].length] = y[0];

                Color c = view.getShade(background, background, normal, vertex);

                view.setColor(c, opacity);
                view.g.fillPolygon(x, y, surfaces[i].length + 1);

                view.setColor(foreground, opacity);
                view.g.drawPolygon(x, y, surfaces[i].length + 1);
            }
        }

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
