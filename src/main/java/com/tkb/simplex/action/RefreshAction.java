package com.tkb.simplex.action;

import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import com.tkb.simplex.gui.Canvas;

/**
 * An action to refresh the graphics in the viewport.
 *
 * @author Akis Papadopoulos
 */
public class RefreshAction extends Action {

    private Canvas canvas;

    public RefreshAction(Canvas canvas) {
        super("refresh");

        this.canvas = canvas;

        KeyStroke accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_R, ActionEvent.CTRL_MASK);
        putValue(ACCELERATOR_KEY, accelerator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        canvas.refresh();
    }
}
