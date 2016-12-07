package com.tkb.simplex.model;

import java.awt.Point;
import java.awt.Color;

/**
 * A triangle graphic object.
 *
 * @author Akis Papadopoulos
 */
public class Triangle extends AbstractObject {

    protected SpacePoint a;

    protected SpacePoint b;

    protected SpacePoint c;

    public Triangle(SpacePoint a, SpacePoint b, SpacePoint c) {
        super();

        this.a = a;
        this.b = b;
        this.c = c;

        centre = a.add(b).add(c).scale(1.0 / 3.0);
    }

    @Override
    public void render(AbstractView view) {
        Point c1 = view.project(a);
        Point c2 = view.project(b);
        Point c3 = view.project(c);

        int[] x = {c1.x, c2.x, c3.x, c1.x};
        int[] y = {c1.y, c2.y, c3.y, c1.y};

        SpacePoint normal = b.subtract(a).cross(c.subtract(a));

        Color color = view.getShade(background, background.darker(), normal, a);

        view.setColor(color, opacity);
        view.g.fillPolygon(x, y, 4);

        view.setColor(foreground, opacity);
        view.g.drawPolygon(x, y, 4);

        view.g.setStroke(view.dashed);

        SpacePoint sx, ex;

        Point s, e;

        sx = b.extend(a, 2.5);
        ex = a.extend(b, 2.5);

        view.setColor(foreground, (int) (opacity * 0.5));
        s = view.project(a);
        e = view.project(sx);
        view.g.drawLine(s.x, s.y, e.x, e.y);

        view.setColor(foreground, (int) (opacity * 0.5));
        s = view.project(b);
        e = view.project(ex);
        view.g.drawLine(s.x, s.y, e.x, e.y);

        sx = c.extend(b, 2.5);
        ex = b.extend(c, 2.5);

        view.setColor(foreground, (int) (opacity * 0.5));
        s = view.project(b);
        e = view.project(sx);
        view.g.drawLine(s.x, s.y, e.x, e.y);

        view.setColor(foreground, (int) (opacity * 0.5));
        s = view.project(c);
        e = view.project(ex);
        view.g.drawLine(s.x, s.y, e.x, e.y);

        sx = a.extend(c, 2.5);
        ex = c.extend(a, 2.5);

        view.setColor(foreground, (int) (opacity * 0.5));
        s = view.project(c);
        e = view.project(sx);
        view.g.drawLine(s.x, s.y, e.x, e.y);

        view.setColor(foreground, (int) (opacity * 0.5));
        s = view.project(a);
        e = view.project(ex);
        view.g.drawLine(s.x, s.y, e.x, e.y);

        view.initialize();
    }

    @Override
    public void transform(SpaceMatrix m) {
        a = a.transform(m);
        b = b.transform(m);
        c = c.transform(m);

        centre = a.add(b).add(c).scale(1.0 / 3.0);
    }
}
