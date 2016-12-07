package com.tkb.simplex.action;

import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import com.tkb.simplex.gui.Canvas;

/**
 * An action to zoom out into the viewport.
 *
 * @author Akis Papadopoulos
 */
public class ZoomOutAction extends Action {

    private Canvas canvas;

    public ZoomOutAction(Canvas canvas) {
        super("zoom.out");

        this.canvas = canvas;

        KeyStroke accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, ActionEvent.CTRL_MASK);
        putValue(ACCELERATOR_KEY, accelerator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        canvas.zoom(0.5);
    }
}
