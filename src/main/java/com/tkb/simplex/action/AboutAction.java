package com.tkb.simplex.action;

import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import com.tkb.simplex.gui.AboutDialog;

/**
 * An action to show up the about dialog.
 *
 * @author Akis Papadopoulos
 */
public class AboutAction extends Action {

    private JFrame parent;

    public AboutAction(JFrame parent) {
        super("about");

        this.parent = parent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AboutDialog dialog = new AboutDialog(parent, true);
        dialog.setVisible(true);
    }
}
