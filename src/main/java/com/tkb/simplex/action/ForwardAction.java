package com.tkb.simplex.action;

import java.awt.event.ActionEvent;
import com.tkb.simplex.model.AbstractObject;
import com.tkb.simplex.model.Animation;
import com.tkb.simplex.gui.Canvas;

/**
 * An action to move the animation one step forward.
 *
 * @author Akis Papadopoulos
 */
public class ForwardAction extends Action {

    private Canvas canvas;

    public ForwardAction(Canvas canvas) {
        super("forward");

        this.canvas = canvas;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractObject scene = canvas.getScene();

        if (scene != null && scene instanceof Animation) {
            Animation animation = (Animation) scene;
            animation.forward();
        }
    }
}
