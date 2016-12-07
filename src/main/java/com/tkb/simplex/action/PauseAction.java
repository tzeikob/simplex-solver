package com.tkb.simplex.action;

import javax.swing.JLabel;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import com.tkb.simplex.model.AbstractObject;
import com.tkb.simplex.model.Animation;
import com.tkb.simplex.gui.Canvas;
import com.tkb.simplex.gui.StatusBar;

/**
 * An action to pause the animation.
 *
 * @author Akis Papapdopoulos
 */
public class PauseAction extends Action {

    private Canvas canvas;

    private StatusBar status;

    public PauseAction(Canvas canvas, StatusBar status) {
        super("pause");

        this.canvas = canvas;
        this.status = status;

        KeyStroke accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, ActionEvent.CTRL_MASK);
        putValue(ACCELERATOR_KEY, accelerator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractObject scene = canvas.getScene();

        if (scene != null && scene instanceof Animation) {
            Animation animation = (Animation) scene;

            animation.pause();

            JLabel label = status.getLabel("status.animation");

            if (label != null) {
                label.setText(actions.getString("animation.paused"));
            }
        }
    }
}
