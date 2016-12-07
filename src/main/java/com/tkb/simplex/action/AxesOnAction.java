package com.tkb.simplex.action;

import java.awt.event.ActionEvent;
import com.tkb.simplex.gui.Canvas;

/**
 * An action to set the axes on and off.
 *
 * @author Akis Papadopoulos
 */
public class AxesOnAction extends Action {

    private Canvas canvas;

    public AxesOnAction(Canvas canvas) {
        super("axes.on");

        this.canvas = canvas;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean state = canvas.isAxesOn();
        canvas.setAxesOn(!state);
    }
}
