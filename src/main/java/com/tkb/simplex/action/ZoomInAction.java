package com.tkb.simplex.action;

import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import com.tkb.simplex.gui.Canvas;

/**
 * An action to zoom in into the viewport.
 *
 * @author Akis Papadopoulos
 */
public class ZoomInAction extends Action {

    private Canvas canvas;

    public ZoomInAction(Canvas canvas) {
        super("zoom.in");

        this.canvas = canvas;

        KeyStroke accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_UP, ActionEvent.CTRL_MASK);
        putValue(ACCELERATOR_KEY, accelerator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        canvas.zoom(2.0);
    }
}
