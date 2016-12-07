package com.tkb.simplex.action;

import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import com.tkb.simplex.gui.Canvas;
import com.tkb.simplex.gui.StatusBar;

/**
 * An action to set the viewport to perspective mode.
 *
 * @author Akis Papadopoulos
 */
public class PerspectiveViewAction extends Action {

    private Canvas canvas;

    private StatusBar status;

    public PerspectiveViewAction(Canvas canvas, StatusBar status) {
        super("perspective");

        this.canvas = canvas;
        this.status = status;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        canvas.setPerspective();

        JLabel label = status.getLabel("status.viewport");

        if (label != null) {
            label.setText(vocab.getString("perspective"));
        }
    }
}
