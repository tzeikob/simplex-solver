package com.tkb.simplex.model;

import java.awt.Color;

/**
 * An abstract graphic object.
 *
 * @author Akis Papadopoulos
 */
public abstract class AbstractObject {

    protected static int counter = 0;

    protected int id;

    protected SpacePoint centre;

    protected Color foreground;

    protected Color background;

    protected int opacity;

    public AbstractObject() {
        id = counter++;

        centre = new SpacePoint(0.0, 0.0, 0.0);

        foreground = new Color(0, 0, 0);

        background = new Color(255, 255, 255);

        opacity = 255;
    }

    public void setForeground(Color color) {
        foreground = color;
    }

    public void setBackground(Color color) {
        background = color;
    }

    public void setOpacity(int value) {
        if (value < 0) {
            opacity = 0;
        } else if (value > 255) {
            opacity = 255;
        } else {
            opacity = value;
        }
    }

    /**
     * A method rendering the graphic object into the given view.
     *
     * @param view the view.
     */
    public abstract void render(AbstractView view);

    /**
     * A method to transform the graphics object given the transformation
     * matrix.
     *
     * @param m the transformation matrix.
     */
    public abstract void transform(SpaceMatrix m);

    @Override
    public String toString() {
        return "[" + id + ", " + centre + "]";
    }
}
