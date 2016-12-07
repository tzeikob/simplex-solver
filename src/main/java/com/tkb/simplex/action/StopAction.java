package com.tkb.simplex.action;

import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import com.tkb.simplex.model.AbstractObject;
import com.tkb.simplex.model.Animation;
import com.tkb.simplex.gui.Canvas;
import com.tkb.simplex.gui.StatusBar;

/**
 * An action to stop the animation.
 *
 * @author Akis Papadopoulos
 */
public class StopAction extends Action {

    private Canvas canvas;

    private StatusBar status;

    public StopAction(Canvas canvas, StatusBar status) {
        super("stop");

        this.canvas = canvas;
        this.status = status;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractObject scene = canvas.getScene();

        if (scene != null && scene instanceof Animation) {
            Animation animation = (Animation) scene;

            animation.stop();

            JLabel label = status.getLabel("status.animation");

            if (label != null) {
                label.setText(actions.getString("animation.stopped"));
            }
        }
    }
}
