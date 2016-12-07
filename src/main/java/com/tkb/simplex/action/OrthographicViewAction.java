package com.tkb.simplex.action;

import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import com.tkb.simplex.gui.Canvas;
import com.tkb.simplex.gui.StatusBar;

/**
 * An action to set the viewport to orthographic mode.
 *
 * @author Akis Papadopoulos
 */
public class OrthographicViewAction extends Action {

    private Canvas canvas;

    private StatusBar status;

    public OrthographicViewAction(Canvas canvas, StatusBar status) {
        super("orthographic");

        this.canvas = canvas;
        this.status = status;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        canvas.setOrthographic();

        JLabel label = status.getLabel("status.viewport");

        if (label != null) {
            label.setText(vocab.getString("orthographic"));
        }
    }
}
