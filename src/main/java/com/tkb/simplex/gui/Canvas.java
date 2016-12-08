package com.tkb.simplex.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Locale;
import com.tkb.simplex.model.AbstractView;
import com.tkb.simplex.model.View;
import com.tkb.simplex.model.AbstractObject;
import com.tkb.simplex.model.Animation;
import com.tkb.simplex.model.Axes;
import com.tkb.simplex.model.Grid;
import encoder.AnimatedGifEncoder;
import org.apache.log4j.Logger;

/**
 * A canvas panel component.
 *
 * @author Akis Papadopoulos
 */
public class Canvas extends JPanel implements Runnable, ComponentListener, MouseListener, MouseMotionListener {

    private static final Logger logger = Logger.getLogger(Canvas.class);

    private static ResourceBundle actions;

    private AbstractView view;

    private Image offscreen;

    private double radius = 1.0;

    private double phi = 45.0;

    private double theta = 45.0;

    private Axes axes;

    private Grid grid;

    private boolean axesOn;

    private boolean gridOn;

    private AbstractObject scene;

    private Thread thread;

    private volatile boolean suspended = false;

    private int lastx, lasty;

    private StatusBar status;

    private boolean recording = false;

    private AnimatedGifEncoder encoder;

    private FileOutputStream out;

    static {
        try {
            actions = ResourceBundle.getBundle("bundle/actions", Locale.getDefault());
        } catch (MissingResourceException exc) {
            logger.error("Missing resource bundle file", exc);
        }
    }

    public Canvas(StatusBar status) {
        super(true);

        this.status = status;

        view = new View(radius, phi, theta);

        axes = new Axes("x1", "x2", "x3", "", "", "", Axes.GUIDELINES + Axes.MEASUREMENTS);
        axes.setOpacity(80);

        grid = new Grid();
        grid.setOpacity(40);

        axesOn = true;
        gridOn = true;

        scene = null;

        addComponentListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void paint(Graphics g) {
        if (offscreen != null) {
            int left = (getWidth() - view.getImage().getWidth()) / 2;
            int top = (getHeight() - view.getImage().getHeight()) / 2;

            g.drawImage(offscreen, left, top, null);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                if (view.isPrepared()) {
                    try {
                        synchronized (this) {
                            while (suspended) {
                                wait();
                            }
                        }
                    } catch (InterruptedException exc) {
                        logger.error(exc.getMessage(), exc);
                    }

                    view.clear();

                    if (scene != null) {
                        scene.render(view);
                    }

                    if (axesOn) {
                        axes.render(view);
                    }

                    if (gridOn) {
                        grid.render(view);
                    }

                    if (recording) {
                        encoder.setDelay(200);
                        encoder.addFrame(view.getImage());
                        view.setText("Recording");
                    }

                    Graphics g = getGraphics();

                    if (g != null) {
                        int left = (getWidth() - view.getImage().getWidth()) / 2;
                        int top = (getHeight() - view.getImage().getHeight()) / 2;

                        offscreen = view.getImage();

                        g.drawImage(offscreen, left, top, null);
                    }

                    if (scene != null && scene instanceof Animation) {
                        Animation animation = (Animation) scene;

                        if (!animation.isAlive()) {
                            JLabel label = status.getLabel("status.animation");

                            if (label != null) {
                                label.setText(actions.getString("animation.finished"));
                            }
                        }
                    }
                } else {
                    view.createImage(500, 500);
                }
            } catch (NullPointerException exc) {
                logger.error(exc.getMessage(), exc);
            }
        }
    }

    public void setScene(AbstractObject object) {
        scene = object;
    }

    public AbstractObject getScene() {
        return scene;
    }

    public synchronized boolean capture() {
        suspended = true;

        boolean done = false;

        BufferedImage frame = view.getImage();

        File file = new File("screen-" + System.currentTimeMillis() + ".png");

        try {
            ImageIO.write(frame, "PNG", file);
            done = true;
        } catch (IOException exc) {
            done = false;
        } finally {
            suspended = false;
            notify();

            return done;
        }
    }

    public void record(boolean state) {
        if (state) {
            try {
                File file = new File("movie-" + System.currentTimeMillis() + ".gif");

                out = new FileOutputStream(file);
            } catch (FileNotFoundException exc) {
                logger.error(exc.getMessage(), exc);
            }

            encoder = new AnimatedGifEncoder();
            encoder.start(out);
            encoder.setQuality(20);
            encoder.setRepeat(0);

            recording = true;
        } else {
            recording = false;
            encoder.finish();

            try {
                out.close();
            } catch (IOException exc) {
                logger.error(exc.getMessage(), exc);
            }
        }
    }

    public boolean isRecording() {
        return recording;
    }

    public void setOrthographic() {
        view.setOrthographic();
        view.setViewpoint(radius, 90.0, 0.0);
        view.pan(-view.getImage().getWidth() / 4, view.getImage().getHeight() / 4);
    }

    public void setPerspective() {
        view.setPerspective();
        view.setViewpoint(radius, 45.0, 45.0);
    }

    public void zoom(double scale) {
        view.zoom(scale);
    }

    public void refresh() {
        Graphics g = getGraphics();

        if (g != null && view.isPrepared()) {
            g.clearRect(0, 0, getWidth(), getHeight());

            int left = (getWidth() - view.getImage().getWidth()) / 2;
            int top = (getHeight() - view.getImage().getHeight()) / 2;

            offscreen = view.getImage();

            g.drawImage(offscreen, left, top, null);
        }
    }

    public void dispose() {
        thread = null;
    }

    public void setAxesMode(int mode) {
        axes.setMode(mode);
    }

    public boolean isAxesOn() {
        return axesOn;
    }

    public void setAxesOn(boolean flag) {
        axesOn = flag;
    }

    public boolean isGridOn() {
        return gridOn;
    }

    public void setGridOn(boolean flag) {
        gridOn = flag;
    }

    @Override
    public synchronized void componentResized(ComponentEvent e) {
        int w = getWidth();
        int h = getHeight();

        if (w > 0 && h > 0) {
            refresh();

            if (w > h) {
                view.createImage((int) (0.98 * h), (int) (0.98 * h));
            } else {
                view.createImage((int) (0.98 * w), (int) (0.98 * w));
            }

            refresh();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        lastx = e.getX();
        lasty = e.getY();

        e.consume();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int newx = e.getX();
        int newy = e.getY();

        JLabel label = status.getLabel("status.messager");

        if (e.getModifiers() == MouseEvent.BUTTON1_MASK && view.isPerspective()) {
            if (label != null) {
                label.setText(actions.getString("canvas.rotate"));
            }

            view.adjust(newx - lastx, newy - lasty);

            phi -= newy - lasty;
            theta -= newx - lastx;

            //view.setViewpoint(1.0, phi, theta);
        } else if (e.getModifiers() == MouseEvent.BUTTON3_MASK) {
            if (label != null) {
                label.setText(actions.getString("canvas.zoom"));
            }

            int dy = newy - lasty;
            double scale = Math.pow(1.01, -dy);

            view.zoom(scale);
        } else if (e.getModifiers() == MouseEvent.BUTTON3_MASK + MouseEvent.CTRL_MASK) {
            if (label != null) {
                label.setText(actions.getString("canvas.dolly"));
            }

            int dy = newy - lasty;
            double scale = Math.pow(1.01, -dy);

            view.dolly(scale);
        } else if (e.getModifiers() == MouseEvent.BUTTON1_MASK + MouseEvent.BUTTON3_MASK) {
            if (label != null) {
                label.setText(actions.getString("canvas.pan"));
            }

            int dx = newx - lastx;
            int dy = newy - lasty;

            view.pan(dx, dy);
        }

        lastx = newx;
        lasty = newy;

        e.consume();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JLabel label = status.getLabel("status.messager");

        if (label != null) {
            label.setText(null);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        JLabel label = status.getLabel("status.messager");

        if (label != null) {
            label.setText(actions.getString("canvas.entered"));
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        JLabel label = status.getLabel("status.messager");

        if (label != null) {
            label.setText(actions.getString("canvas.entered"));
        }
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
