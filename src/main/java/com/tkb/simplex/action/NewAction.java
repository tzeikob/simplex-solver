package com.tkb.simplex.action;

import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import com.tkb.simplex.gui.Desktop;
import com.tkb.simplex.gui.Editor;

/**
 * An action to open a new frame for linear problem.
 *
 * @author Akis Papadopoulos
 */
public class NewAction extends Action {

    private Desktop desktop;

    public NewAction(Desktop desktop) {
        super("new");

        this.desktop = desktop;

        KeyStroke accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK);
        putValue(ACCELERATOR_KEY, accelerator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Editor editor = new Editor();
        desktop.add(editor);
    }
}
