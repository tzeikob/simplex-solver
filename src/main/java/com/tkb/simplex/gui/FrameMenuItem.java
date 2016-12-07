package com.tkb.simplex.gui;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;

/**
 * A generic menu items.
 *
 * @author Akis Papadopoulos
 */
public class FrameMenuItem extends JCheckBoxMenuItem {

    private JInternalFrame frame;

    public FrameMenuItem(JInternalFrame frame) {
        super(frame.getTitle());

        this.frame = frame;
    }

    public JInternalFrame getFrame() {
        return frame;
    }
}
