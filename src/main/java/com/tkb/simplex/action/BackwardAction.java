package com.tkb.simplex.action;

import java.awt.event.ActionEvent;
import com.tkb.simplex.model.AbstractObject;
import com.tkb.simplex.model.Animation;
import com.tkb.simplex.gui.Canvas;

/**
 * A action to move the animation one step backward.
 *
 * @author Akis Papadopoulos
 */
public class BackwardAction extends Action {

    private Canvas canvas;

    public BackwardAction(Canvas canvas) {
        super("backward");

        this.canvas = canvas;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractObject scene = canvas.getScene();

        if (scene != null && scene instanceof Animation) {
            Animation animation = (Animation) scene;
            animation.backward();
        }
    }
}
