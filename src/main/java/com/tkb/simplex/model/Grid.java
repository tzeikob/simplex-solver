package com.tkb.simplex.model;

import java.awt.Point;

/**
 * A grid graphic object used as compliment for axes.
 *
 * @author Akis Papadopoulos
 */
public class Grid extends AbstractObject {

    protected SpacePoint o = new SpacePoint(0.0, 0.0, 0.0);

    protected SpacePoint ox = new SpacePoint(1.0, 0.0, 0.0);
    protected SpacePoint oy = new SpacePoint(0.0, 1.0, 0.0);
    protected SpacePoint oz = new SpacePoint(0.0, 0.0, 1.0);

    protected SpacePoint xo = new SpacePoint(-1.0, 0.0, 0.0);
    protected SpacePoint yo = new SpacePoint(0.0, -1.0, 0.0);
    protected SpacePoint zo = new SpacePoint(0.0, 0.0, -1.0);

    public Grid() {
        super();

        centre = o;
    }

    @Override
    public void render(AbstractView view) {
        Point s, e;

        view.setColor(foreground, opacity);

        s = view.project(o);
        e = view.project(ox);
        view.g.drawLine(s.x, s.y, e.x, e.y);

        if (view.isPerspective()) {
            view.setColor(foreground, (int) (opacity * 0.2));
        }

        s = view.project(o);
        e = view.project(oy);
        view.g.drawLine(s.x, s.y, e.x, e.y);

        for (int i = 1; i <= 10; i++) {
            double step = i / 10.0;

            SpacePoint a, n;

            a = o.extend(ox, step);
            n = a.normal(o, oz);

            s = view.project(a);
            e = view.project(n);
            view.g.drawLine(s.x, s.y, e.x, e.y);

            a = o.extend(oy, step);
            n = a.normal(oz, o);

            s = view.project(a);
            e = view.project(n);
            view.g.drawLine(s.x, s.y, e.x, e.y);
        }

        if (view.isPerspective()) {
            view.setColor(foreground, opacity);

            s = view.project(o);
            e = view.project(oz);
            view.g.drawLine(s.x, s.y, e.x, e.y);

            s = view.project(o);
            e = view.project(xo);
            view.g.drawLine(s.x, s.y, e.x, e.y);

            s = view.project(o);
            e = view.project(zo);
            view.g.drawLine(s.x, s.y, e.x, e.y);

            for (int i = 1; i <= 10; i++) {
                view.setColor(foreground, opacity);

                double step = i / 10.0;

                SpacePoint a, n;

                a = o.extend(xo, step);
                n = a.normal(o, oy);

                s = view.project(a);
                e = view.project(n);
                view.g.drawLine(s.x, s.y, e.x, e.y);

                a = o.extend(oz, step);
                n = a.normal(oy, o);

                s = view.project(a);
                e = view.project(n);
                view.g.drawLine(s.x, s.y, e.x, e.y);

                a = o.extend(xo, step);
                n = a.normal(oy, o);

                s = view.project(a);
                e = view.project(n);
                view.g.drawLine(s.x, s.y, e.x, e.y);

                a = o.extend(zo, step);
                n = a.normal(o, oy);

                s = view.project(a);
                e = view.project(n);
                view.g.drawLine(s.x, s.y, e.x, e.y);

                a = o.extend(ox, step);
                n = a.normal(o, oy);

                s = view.project(a);
                e = view.project(n);
                view.g.drawLine(s.x, s.y, e.x, e.y);

                a = o.extend(zo, step);
                n = a.normal(oy, o);

                s = view.project(a);
                e = view.project(n);
                view.g.drawLine(s.x, s.y, e.x, e.y);

                a = o.extend(ox, step);
                n = a.normal(oy, o);

                s = view.project(a);
                e = view.project(n);
                view.g.drawLine(s.x, s.y, e.x, e.y);

                a = o.extend(oz, step);
                n = a.normal(o, oy);

                s = view.project(a);
                e = view.project(n);
                view.g.drawLine(s.x, s.y, e.x, e.y);

                view.setColor(foreground, (int) (opacity * 0.2));

                a = o.extend(oz, step);
                n = a.normal(ox, o);

                s = view.project(a);
                e = view.project(n);
                view.g.drawLine(s.x, s.y, e.x, e.y);

                a = o.extend(oy, step);
                n = a.normal(o, ox);

                s = view.project(a);
                e = view.project(n);
                view.g.drawLine(s.x, s.y, e.x, e.y);
            }
        }

        view.initialize();
    }

    @Override
    public void transform(SpaceMatrix m) {
        o = o.transform(m);

        ox = ox.transform(m);
        oy = oy.transform(m);
        oz = oz.transform(m);

        xo = xo.transform(m);
        yo = yo.transform(m);
        zo = zo.transform(m);

        centre = centre.transform(m);
    }
}
