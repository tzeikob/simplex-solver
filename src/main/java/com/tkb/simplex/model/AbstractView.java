package com.tkb.simplex.model;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.BasicStroke;
import java.awt.RenderingHints;

/**
 * An abstract graphics view.
 *
 * @author Akis Papadopoulos
 */
public abstract class AbstractView {

    protected RenderingHints render;

    protected BasicStroke thin = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

    protected BasicStroke plain = new BasicStroke(1.5f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

    protected BasicStroke thik = new BasicStroke(2.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND);

    protected BasicStroke dashed = new BasicStroke(2.0f, BasicStroke.CAP_SQUARE, BasicStroke.CAP_ROUND, 10.0f, new float[]{6.0f}, 0.0f);

    protected Color foreground = new Color(0, 0, 0);

    protected Color background = new Color(255, 255, 255);

    protected Color ambient = new Color(50, 50, 50);

    protected Color light = new Color(200, 200, 200);

    protected Font font = new Font("Verdana", Font.PLAIN, 12);

    protected double wx, wy, wwidth, wheight;

    protected double dinverse;

    protected SpacePoint u, v, w;

    protected SpacePoint spotlight = new SpacePoint(1.0, 1.0, 1.0);

    protected BufferedImage img;

    protected Graphics2D g;

    public AbstractView() {
        wx = -1.0;
        wy = -1.0;

        wwidth = 2.0;
        wheight = 2.0;

        dinverse = 1 / (2 * wwidth);
    }

    /**
     * A method initializing the graphics of the view.
     *
     * @param width the width size of the view.
     * @param height the height size of the view.
     */
    public void createImage(int width, int height) {
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        g = img.createGraphics();

        render = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        render.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        render.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        render.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        render.put(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        render.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        render.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        render.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        render.put(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

        g.setRenderingHints(render);
        g.setStroke(plain);
        g.setColor(foreground);
        g.setBackground(background);
        g.setFont(font);
    }

    /**
     * A method returning the graphics of this view as an image.
     *
     * @return the graphic image.
     */
    public BufferedImage getImage() {
        return img;
    }

    public boolean isPrepared() {
        return g != null;
    }

    /**
     * A method setting the rendering quality of the view.
     *
     * @param hints rendering settings.
     */
    public void setRenderingQuality(RenderingHints hints) {
        if (g != null) {
            g.setRenderingHints(hints);
        }
    }

    public void setColor(Color color, int opacity) {
        if (g != null) {
            int red = color.getRed();
            int green = color.getGreen();
            int blue = color.getBlue();

            color = new Color(red, green, blue, opacity);

            g.setColor(color);
        }
    }

    /**
     * A method clearing the graphics from the view.
     */
    public void clear() {
        if (g != null) {
            g.clearRect(0, 0, img.getWidth(), img.getHeight());
            g.setColor(foreground);
            g.draw3DRect(0, 0, img.getWidth() - 1, img.getHeight() - 1, false);

            initialize();
        }
    }

    /**
     * A method initializing the status of the view.
     */
    public void initialize() {
        if (g != null) {
            g.setStroke(plain);
            g.setColor(foreground);
        }
    }

    public void setText(String text) {
        if (g != null) {
            g.setStroke(plain);
            g.setColor(Color.red);
            g.drawString(text, 5, 20);

            initialize();
        }
    }

    /**
     * A method setting the view in an orthographic mode.
     */
    public void setOrthographic() {
        wx = -1.0;
        wy = -1.0;

        wwidth = 2.0;
        wheight = 2.0;

        dinverse = 0.0;
    }

    /**
     * A method setting the view in a perspective mode.
     */
    public void setPerspective() {
        wx = -1.0;
        wy = -1.0;

        wwidth = 2.0;
        wheight = 2.0;

        dinverse = 1 / (2 * wwidth);
    }

    public boolean isOrthographic() {
        return dinverse == 0;
    }

    public boolean isPerspective() {
        return dinverse != 0;
    }

    /**
     * A method setting the view point camera of the view.
     *
     * @param dirn the direction point vector.
     * @param up the up point.
     */
    public abstract void setViewpoint(SpacePoint dirn, SpacePoint up);

    /**
     * A method setting the viewpoint camera of the view.
     *
     * @param radius the radius distance.
     * @param phi the phi angle value.
     * @param theta the theta angle value.
     */
    public abstract void setViewpoint(double radius, double phi, double theta);

    /**
     * A method adjusting the view point camera by the given x and y amounts.
     *
     * @param dx the amount in horizontal axis.
     * @param dy the amount in vertical axis.
     */
    public abstract void adjust(double dx, double dy);

    /**
     * A method zooming in and out of the view given the scale factor, where
     * negative values means zoom out and positive zoom in.
     *
     * @param scale the zoom scale factor.
     */
    public abstract void zoom(double scale);

    /**
     * A method initializes the zoom in order to get the full view area.
     *
     * @param scale the scale factor.
     */
    public abstract void dolly(double scale);

    /**
     * A method translates the view point by the amount of x and y values.
     *
     * @param dx the amount of horizontal value.
     * @param dy the amount of vertical value.
     */
    public abstract void pan(double dx, double dy);

    /**
     * A method to project a given point into the view.
     *
     * @param point the point to be projected.
     * @return the projected point.
     */
    public abstract Point project(SpacePoint point);

    /**
     * A method checking if the given point is in front visible face.
     *
     * @param normal the normal point.
     * @param corner the corner point.
     * @return true if visible otherwise false.
     */
    public abstract boolean isFrontFace(SpacePoint normal, SpacePoint corner);

    /**
     * A method returning the depth of the point in the given view.
     *
     * @param point the point.
     * @return the value of depth.
     */
    public abstract double depth(SpacePoint point);

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
    public abstract Color getShade(Color front, Color back, SpacePoint normal, SpacePoint corner);

    @Override
    public String toString() {
        return "[" + wx + ", " + wy + ", "
                + wwidth + ", " + wheight + ", "
                + dinverse + ", " + u + ", " + v + ", " + w + "]";
    }
}
