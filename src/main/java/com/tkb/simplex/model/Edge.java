package com.tkb.simplex.model;

import java.awt.Point;

/**
 * A graphics edge object.
 *
 * @author Akis Papadopoulos
 */
public class Edge extends AbstractObject {

    protected SpacePoint start, end;

    protected HalfSpace halfspace;

    protected HalfSpace up;

    protected HalfSpace down;

    /**
     * A constructor creating an edge starting and ending from and to the given
     * points.
     *
     * @param start the start point.
     * @param end the end point.
     */
    public Edge(SpacePoint start, SpacePoint end) {
        super();

        this.start = start;
        this.end = end;

        centre = start.add(end).scale(1.0 / 2.0);

        up = new HalfSpace(end, start);

        down = new HalfSpace(start, end);
    }

    /**
     * A constructor creating an edge starting and ending from and to the given
     * points, marking the half-space area according to the given a point and
     * the edge orientation.
     *
     * @param start the start point.
     * @param end the end point.
     * @param a the half-space reference point.
     */
    public Edge(SpacePoint start, SpacePoint end, SpacePoint a) {
        super();

        this.start = start;
        this.end = end;

        centre = start.add(end).scale(1.0 / 2.0);

        up = new HalfSpace(end, start);

        down = new HalfSpace(start, end);

        SpaceMatrix m = new SpaceMatrix();
        m.translate(start.x, start.y, start.z);

        a = a.transform(m);

        if (down.isInside(a) < 0) {
            halfspace = down;
        } else {
            halfspace = up;
        }
    }

    /**
     * A method setting the half-space regarding the edge orientation given a
     * reference point.
     *
     * @param a the half-space reference point.
     */
    public void setHalfspace(SpacePoint a) {
        SpaceMatrix m = new SpaceMatrix();
        m.translate(start.x, start.y, start.z);

        a = a.transform(m);

        if (down.isInside(a) < 0) {
            halfspace = down;
        } else {
            halfspace = up;
        }
    }

    @Override
    public void render(AbstractView view) {
        Point s, e;

        view.g.setStroke(view.dashed);

        SpacePoint startx = end.extend(start, 1.2);
        SpacePoint endx = start.extend(end, 1.2);

        view.setColor(foreground, (int) (opacity * 0.3));
        s = view.project(start);
        e = view.project(startx);
        view.g.drawLine(s.x, s.y, e.x, e.y);

        view.setColor(foreground, (int) (opacity * 0.3));
        s = view.project(end);
        e = view.project(endx);
        view.g.drawLine(s.x, s.y, e.x, e.y);

        if (halfspace != null) {
            view.g.setStroke(view.thin);

            SpacePoint as, ae;
            SpacePoint bs, be;

            if (halfspace == up) {
                HalfSpace hs;
                as = start.extend(end, 0.1);
                hs = new HalfSpace(as, start);
                ae = as.extend(hs.normal, 0.01);

                bs = start.extend(end, 0.9);
                hs = new HalfSpace(bs, start);
                be = bs.extend(hs.normal, 0.01);
            } else {
                HalfSpace hs;
                as = start.extend(end, 0.1);
                hs = new HalfSpace(as, end);
                ae = as.extend(hs.normal, 0.01);

                bs = start.extend(end, 0.9);
                hs = new HalfSpace(bs, end);
                be = bs.extend(hs.normal, 0.01);
            }

            Point c1 = view.project(as);
            Point c2 = view.project(ae);
            Point c3 = view.project(be);
            Point c4 = view.project(bs);

            int[] x = {c1.x, c2.x, c3.x, c4.x, c1.x};
            int[] y = {c1.y, c2.y, c3.y, c4.y, c1.y};

            view.setColor(background, (int) (opacity * 0.4));
            view.g.fillPolygon(x, y, 5);

            view.setColor(foreground, (int) (opacity * 0.4));
            view.g.drawPolygon(x, y, 5);
        }

        view.g.setStroke(view.plain);
        view.setColor(foreground, opacity);
        s = view.project(start);
        e = view.project(end);
        view.g.drawLine(s.x, s.y, e.x, e.y);

        view.initialize();
    }

    @Override
    public void transform(SpaceMatrix m) {
        start = start.transform(m);
        end = end.transform(m);

        centre = start.add(end).scale(1.0 / 2.0);

        up = new HalfSpace(end, start);

        down = new HalfSpace(start, end);
    }
}
