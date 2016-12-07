package com.tkb.simplex.action;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import org.apache.log4j.Logger;

/**
 * An action to close all the opened frames.
 *
 * @author Akis Papadopoulos
 */
public class CloseAllAction extends Action {
    
    private static final Logger logger = Logger.getLogger(CloseAllAction.class);

    private JDesktopPane desktop;

    public CloseAllAction(JDesktopPane desktop) {
        super("close.all");

        this.desktop = desktop;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JInternalFrame[] frames = desktop.getAllFrames();

        for (int i = 0; i < frames.length; i++) {
            if (frames[i].isIcon()) {
                try {
                    frames[i].setIcon(false);
                } catch (PropertyVetoException exc) {
                    logger.error(exc.getMessage(), exc);
                }

                frames[i].doDefaultCloseAction();
            } else {
                frames[i].doDefaultCloseAction();
            }
        }
    }
}
