package com.tkb.simplex.action;

import java.awt.event.ActionEvent;
import com.tkb.simplex.gui.Canvas;

/**
 * An action to set the grid on and off.
 *
 * @author Akis Papadopoulos
 */
public class GridOnAction extends Action {

    private Canvas canvas;

    public GridOnAction(Canvas canvas) {
        super("grid.on");

        this.canvas = canvas;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean state = canvas.isGridOn();
        canvas.setGridOn(!state);
    }
}
