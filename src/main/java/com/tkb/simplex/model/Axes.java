package com.tkb.simplex.model;

import java.awt.Point;

/**
 * A graphics object representing the XYZ axes of the view port area.
 *
 * @author Akis Papadopoulos
 */
public class Axes extends AbstractObject {

    public static final int PLAIN = 0;
    public static final int GUIDELINES = 1;
    public static final int MEASUREMENTS = 2;

    protected SpacePoint o = new SpacePoint(0.0, 0.0, 0.0);

    protected SpacePoint ox = new SpacePoint(1.0, 0.0, 0.0);
    protected SpacePoint oy = new SpacePoint(0.0, 1.0, 0.0);
    protected SpacePoint oz = new SpacePoint(0.0, 0.0, 1.0);

    protected SpacePoint xo = new SpacePoint(-1.0, 0.0, 0.0);
    protected SpacePoint yo = new SpacePoint(0.0, -1.0, 0.0);
    protected SpacePoint zo = new SpacePoint(0.0, 0.0, -1.0);

    protected String labelx = "x";
    protected String labely = "y";
    protected String labelz = "z";

    protected String unitx = "";
    protected String unity = "";
    protected String unitz = "";

    protected int mode;

    public Axes() {
        super();

        centre = o;
        mode = PLAIN;
    }

    public Axes(int mode) {
        super();

        centre = o;
        this.mode = mode;
    }

    public Axes(String labelx, String labely, String labelz, String unitx, String unity, String unitz, int mode) {
        super();

        centre = o;

        this.labelx = labelx;
        this.labely = labely;
        this.labelz = labelz;

        this.unitx = unitx;
        this.unity = unity;
        this.unitz = unitz;

        this.mode = mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    @Override
    public void render(AbstractView view) {
        Point s, e, p;

        view.setColor(foreground, opacity);

        s = view.project(o);

        e = view.project(ox);
        view.g.drawLine(s.x, s.y, e.x, e.y);

        p = view.project(o.extend(ox, 1.1));
        view.g.drawString(labelx + " " + unitx, p.x, p.y);

        e = view.project(oy);
        view.g.drawLine(s.x, s.y, e.x, e.y);

        p = view.project(o.extend(oy, 1.1));
        view.g.drawString(labely + " " + unity, p.x, p.y);

        for (int i = 1; i <= 10; i++) {
            double step = i / 10.0;

            String measure = String.valueOf(i);

            SpacePoint a, n;

            a = o.extend(ox, step);
            n = a.normal(o, oz);

            if (mode == 1 || mode == 3) {
                s = view.project(a);
                e = view.project(a.extend(n, 0.01));
                view.g.drawLine(s.x, s.y, e.x, e.y);
            }

            if (mode == 2 || mode == 3) {
                s = view.project(a);
                e = view.project(a.extend(n, 0.03));
                view.g.drawString(measure, e.x, e.y);
            }

            a = o.extend(oy, step);
            n = a.normal(oz, o);

            if (mode == 1 || mode == 3) {
                s = view.project(a);
                e = view.project(a.extend(n, 0.01));
                view.g.drawLine(s.x, s.y, e.x, e.y);
            }

            if (mode == 2 || mode == 3) {
                s = view.project(a);
                e = view.project(a.extend(n, 0.03));
                view.g.drawString(measure, e.x, e.y);
            }
        }

        if (view.isPerspective()) {
            s = view.project(o);

            e = view.project(oz);
            view.g.drawLine(s.x, s.y, e.x, e.y);

            p = view.project(o.extend(oz, 1.1));
            view.g.drawString(labelz + " " + unitz, p.x, p.y);

            for (int i = 1; i <= 10; i++) {
                double step = i / 10.0;

                String measure = String.valueOf(i);

                SpacePoint a, n;
                a = o.extend(oz, step);
                n = a.normal(ox, o);

                if (mode == 1 || mode == 3) {
                    s = view.project(a);
                    e = view.project(a.extend(n, 0.01));
                    view.g.drawLine(s.x, s.y, e.x, e.y);
                }

                if (mode == 2 || mode == 3) {
                    s = view.project(a);
                    e = view.project(a.extend(n, 0.03));
                    view.g.drawString(measure, e.x, e.y);
                }
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
