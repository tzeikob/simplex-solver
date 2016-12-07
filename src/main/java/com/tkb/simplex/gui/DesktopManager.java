package com.tkb.simplex.gui;

import javax.swing.DefaultDesktopManager;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.JComponent;
import java.awt.Insets;
import java.awt.Dimension;

/**
 * A desktop manager component.
 *
 * @author Akis Papadopoulos
 */
public class DesktopManager extends DefaultDesktopManager {

    private JDesktopPane desktop;

    public DesktopManager(JDesktopPane desktop) {
        this.desktop = desktop;
    }

    public void reset() {
        JScrollPane scrollpane = null;

        if (desktop.getParent() instanceof JViewport) {
            JViewport viewport = (JViewport) desktop.getParent();

            if (viewport.getParent() instanceof JScrollPane) {
                scrollpane = (JScrollPane) viewport.getParent();
            }
        }

        if (scrollpane != null) {
            Insets scrollInsets = scrollpane.getBorder().getBorderInsets(scrollpane);

            Dimension d = scrollpane.getVisibleRect().getSize();

            if (scrollpane.getBorder() != null) {
                d.setSize(d.getWidth() - scrollInsets.left - scrollInsets.right, d.getHeight() - scrollInsets.top - scrollInsets.bottom);
            }

            d.setSize(d.getWidth() - 20, d.getHeight() - 20);

            int x = 0;
            int y = 0;

            Dimension dim = new Dimension(x, y);

            desktop.setMinimumSize(dim);
            desktop.setMaximumSize(dim);
            desktop.setPreferredSize(dim);

            scrollpane.invalidate();
            scrollpane.validate();
        }
    }

    protected void resize() {
        JScrollPane scrollpane = null;

        if (desktop.getParent() instanceof JViewport) {
            JViewport viewport = (JViewport) desktop.getParent();

            if (viewport.getParent() instanceof JScrollPane) {
                scrollpane = (JScrollPane) viewport.getParent();
            }
        }

        if (scrollpane != null) {
            Insets scrollInsets = scrollpane.getBorder().getBorderInsets(scrollpane);

            JInternalFrame[] frames = desktop.getAllFrames();

            int x = 0;
            int y = 0;

            for (int i = 0; i < frames.length; i++) {
                if (frames[i].getX() + frames[i].getWidth() > x) {
                    x = frames[i].getX() + frames[i].getWidth();
                }

                if (frames[i].getY() + frames[i].getHeight() > y) {
                    y = frames[i].getY() + frames[i].getHeight();
                }
            }

            Dimension d = scrollpane.getVisibleRect().getSize();

            if (scrollpane.getBorder() != null) {
                d.setSize(d.getWidth() - scrollInsets.left - scrollInsets.right, d.getHeight() - scrollInsets.top - scrollInsets.bottom);
            }

            if (x <= d.getWidth()) {
                x = ((int) d.getWidth()) - 20;
            }

            if (y <= d.getHeight()) {
                y = ((int) d.getHeight()) - 20;
            }

            Dimension dim = new Dimension(x, y);

            desktop.setMinimumSize(dim);
            desktop.setMaximumSize(dim);
            desktop.setPreferredSize(dim);

            scrollpane.invalidate();
            scrollpane.validate();
        }
    }

    @Override
    public void endResizingFrame(JComponent frame) {
        super.endResizingFrame(frame);

        resize();
    }

    @Override
    public void endDraggingFrame(JComponent frame) {
        super.endDraggingFrame(frame);

        resize();
    }
}
