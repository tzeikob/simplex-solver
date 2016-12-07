package com.tkb.simplex.gui;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import java.awt.Component;
import java.awt.Point;
import java.beans.PropertyVetoException;
import org.apache.log4j.Logger;

/**
 * A desktop pane.
 *
 * @author Akis Papadopoulos
 */
public class Desktop extends JDesktopPane {
    
    private static final Logger logger = Logger.getLogger(Desktop.class);

    private static final int OFFSET = 20;

    public static final int HORIZONTAL = 0;

    public static final int VERTICAL = 1;

    private DesktopManager manager;

    public Desktop() {
        super();

        manager = new DesktopManager(this);

        setDesktopManager(manager);
    }

    public Component add(JInternalFrame frame) {
        JInternalFrame[] frames = getAllFrames();

        Component component = super.add(frame);

        if (getParent() != null && isVisible()) {
            manager.resize();
        }

        Point p;

        if (frames.length > 0) {
            int x = frames[0].getLocation().x + OFFSET;
            int y = frames[0].getLocation().y + OFFSET;
            p = new Point(x, y);
        } else {
            p = new Point(0, 0);
        }

        frame.setLocation(p.x, p.y);

        if (frame.isResizable()) {
            int w = getWidth() - (getWidth() / 3);
            int h = getHeight() - (getHeight() / 3);

            if (w < frame.getMinimumSize().getWidth()) {
                w = (int) frame.getMinimumSize().getWidth();
            }

            if (h < frame.getMinimumSize().getHeight()) {
                h = (int) frame.getMinimumSize().getHeight();
            }

            frame.setSize(w, h);
        }

        moveToFront(frame);

        frame.setVisible(true);
        try {
            frame.setSelected(true);
        } catch (PropertyVetoException exc) {
            frame.toBack();
        }

        return component;
    }

    @Override
    public void remove(Component component) {
        super.remove(component);

        if (getParent() != null && isVisible()) {
            manager.resize();
        }
    }

    @Override
    public void setBounds(int x, int y, int w, int h) {
        super.setBounds(x, y, w, h);

        if (getParent() != null && isVisible()) {
            manager.resize();
        }
    }

    public void next() {
        JInternalFrame[] frames = getAllFrames();

        if (frames.length > 1) {
            JInternalFrame selectedFrame = getSelectedFrame();

            moveToFront(frames[1]);
            try {
                frames[1].setSelected(true);
            } catch (PropertyVetoException exc) {
                logger.error(exc.getMessage(), exc);
            }

            moveToBack(selectedFrame);

            try {
                selectedFrame.setSelected(false);
            } catch (PropertyVetoException exc) {
                logger.error(exc.getMessage(), exc);
            }
        }
    }

    public void previous() {
        JInternalFrame[] frames = getAllFrames();

        if (frames.length > 1) {
            JInternalFrame selectedFrame = getSelectedFrame();

            moveToFront(frames[frames.length - 1]);
            try {
                frames[frames.length - 1].setSelected(true);
            } catch (PropertyVetoException exc) {
                logger.error(exc.getMessage(), exc);
            }

            try {
                selectedFrame.setSelected(false);
            } catch (PropertyVetoException exc) {
                logger.error(exc.getMessage(), exc);
            }
        }
    }

    public void cascade() {
        JInternalFrame[] frames = getAllFrames();

        manager.reset();

        int x = 0;
        int y = 0;

        int height = (getBounds().height - 5) - frames.length * OFFSET;
        int width = (getBounds().width - 5) - frames.length * OFFSET;

        for (int i = frames.length - 1; i >= 0; i--) {
            frames[i].setSize(width, height);
            frames[i].setLocation(x, y);

            x += OFFSET;
            y += OFFSET;
        }
    }

    public void tile(int mode) {
        Component[] frames = getAllFrames();
        manager.reset();

        if (mode == HORIZONTAL) {
            int y = 0;
            int height = getBounds().height / frames.length;

            for (int i = 0; i < frames.length; i++) {
                frames[i].setSize(getBounds().width, height);
                frames[i].setLocation(0, y);

                y += height;
            }
        } else if (mode == VERTICAL) {
            int x = 0;
            int width = getBounds().width / frames.length;

            for (int i = 0; i < frames.length; i++) {
                frames[i].setSize(width, getBounds().height);
                frames[i].setLocation(x, 0);

                x += width;
            }
        }
    }
}
