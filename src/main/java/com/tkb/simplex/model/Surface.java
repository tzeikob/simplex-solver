package com.tkb.simplex.model;

import java.awt.Point;
import java.awt.Color;

/**
 * A graphic convex hull surface object.
 * 
 * @author Akis Papadopoulos
 */
public class Surface extends ConvexHull {

    protected AbstractView uvw;

    protected SpacePoint normal;

    public Surface(SpacePoint[] data, SpacePoint normal) {
        super();

        this.data = data;

        this.normal = normal;

        //SpacePoint dirn = data[1].subtract(data[0]).cross(data[2].subtract(data[0])).normalize();
        SpacePoint dirn = normal.normalize();
        SpacePoint up = dirn.cross(data[1]);

        uvw = new View(dirn, up);
        uvw.createImage(100, 100);
        uvw.setOrthographic();

        build();

        centre = vertices[0];

        for (int i = 1; i < vertices.length; i++) {
            centre = centre.add(vertices[i]);
        }

        centre = centre.scale(1.0 / vertices.length);
    }

    public void build() {
        SpacePoint[] pr = new SpacePoint[data.length];

        for (int i = 0; i < data.length; i++) {
            Point p = uvw.project(data[i]);
            pr[i] = new SpacePoint(p.x, p.y, 0.0);
        }

        vertices = new SpacePoint[1];

        SpacePoint bottom = pr[0];
        vertices[0] = data[0];

        for (int i = 1; i < pr.length; i++) {
            if (pr[i].y < bottom.y) {
                bottom = pr[i];
                vertices[0] = data[i];
            }
        }

        SpacePoint p = bottom;

        SpacePoint[] v = new SpacePoint[1];
        v[0] = p;

        do {
            int i = 0;
            if (pr[0] == p) {
                i = 1;
            }

            SpacePoint candidate = pr[i];

            HalfSpace h = new HalfSpace(p, candidate);

            int index = i;

            for (i = i + 1; i < pr.length; i++) {
                if (pr[i] != p && h.isInside(pr[i]) < 0) {
                    candidate = pr[i];
                    h = new HalfSpace(p, candidate);
                    index = i;
                }
            }

            SpacePoint[] vnew = new SpacePoint[v.length + 1];
            System.arraycopy(v, 0, vnew, 0, v.length);
            v = vnew;
            v[v.length - 1] = candidate;

            SpacePoint[] verticesnew = new SpacePoint[vertices.length + 1];
            System.arraycopy(vertices, 0, verticesnew, 0, vertices.length);
            vertices = verticesnew;
            vertices[vertices.length - 1] = data[index];

            p = candidate;
        } while (p != bottom);
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

        //SpacePoint normal = vertices[1].subtract(vertices[0]).cross(vertices[2].subtract(vertices[0]));
        SpacePoint vertex = vertices[0];

        Color color = view.getShade(background, background, normal, vertex);
        view.setColor(color, opacity);
        view.g.fillPolygon(x, y, x.length);

        view.setColor(foreground, opacity);
        view.g.drawPolygon(x, y, x.length);

        view.g.setStroke(view.dashed);

        for (int i = 0; i < vertices.length - 1; i++) {
            Point s, e;
            s = view.project(vertices[i + 1].extend(vertices[i], 1.8));
            e = view.project(vertices[i].extend(vertices[i + 1], 1.8));

            view.setColor(foreground, (int) (opacity * 0.5));
            view.g.drawLine(s.x, s.y, e.x, e.y);
        }

        Point s, e;
        s = view.project(vertices[vertices.length - 1].extend(vertices[0], 1.8));
        e = view.project(vertices[0].extend(vertices[vertices.length - 1], 1.8));

        view.setColor(foreground, (int) (opacity * 0.5));
        view.g.drawLine(s.x, s.y, e.x, e.y);

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
