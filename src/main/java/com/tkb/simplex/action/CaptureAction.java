package com.tkb.simplex.action;

import javax.swing.JLabel;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import com.tkb.simplex.gui.Canvas;
import com.tkb.simplex.gui.StatusBar;

/**
 * An action to capture a screen shot of the graphics viewport.
 *
 * @author Akis Papadopoulos
 */
public class CaptureAction extends Action {

    private Canvas canvas;

    private StatusBar status;

    public CaptureAction(Canvas canvas, StatusBar status) {
        super("capture");

        this.canvas = canvas;
        this.status = status;

        KeyStroke accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_F8, ActionEvent.CTRL_MASK);
        putValue(ACCELERATOR_KEY, accelerator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean done = canvas.capture();

        if (done) {
            JLabel label = status.getLabel("status.messager");

            if (label != null) {
                label.setText(actions.getString("capture.completed"));
            }
        } else {
            JLabel label = status.getLabel("status.messager");

            if (label != null) {
                label.setText(actions.getString("capture.canceled"));
            }
        }
    }
}
