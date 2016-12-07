package com.tkb.simplex.action;

import javax.swing.ImageIcon;
import java.awt.event.ActionEvent;
import com.tkb.simplex.model.Animation;

/**
 * An action to repeat the animation.
 *
 * @author Akis Papadopoulos
 */
public class RepeatAction extends Action {

    public RepeatAction() {
        super("repeat");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        boolean state = Animation.isRepeating();

        Animation.setRepeating(!state);

        if (Animation.isRepeating()) {
            putValue(SMALL_ICON, new ImageIcon(this.getClass().getResource("/images/" + menu.getString("repeat.on.icon"))));
            putValue(SHORT_DESCRIPTION, menu.getString("repeat.on.tooltip"));
        } else {
            putValue(SMALL_ICON, new ImageIcon(this.getClass().getResource("/images/" + menu.getString("repeat.icon"))));
            putValue(SHORT_DESCRIPTION, menu.getString("repeat.tooltip"));
        }
    }
}
