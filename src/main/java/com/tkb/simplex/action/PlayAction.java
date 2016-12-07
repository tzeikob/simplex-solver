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
 * An action to play the animation.
 *
 * @author Akis Papadopoulos
 */
public class PlayAction extends Action {

    private Canvas canvas;

    private StatusBar status;

    public PlayAction(Canvas canvas, StatusBar status) {
        super("play");

        this.canvas = canvas;
        this.status = status;

        KeyStroke accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, ActionEvent.CTRL_MASK);
        putValue(ACCELERATOR_KEY, accelerator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AbstractObject scene = canvas.getScene();

        if (scene != null && scene instanceof Animation) {
            Animation animation = (Animation) scene;

            animation.play();

            JLabel label = status.getLabel("status.animation");

            if (label != null) {
                label.setText(actions.getString("animation.running"));
            }
        }
    }
}
