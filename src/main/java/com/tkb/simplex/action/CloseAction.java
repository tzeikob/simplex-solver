package com.tkb.simplex.action;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import java.awt.event.ActionEvent;
import java.beans.PropertyVetoException;
import org.apache.log4j.Logger;

/**
 * An action to close the selected frame.
 *
 * @author Akis Papadopoulos
 */
public class CloseAction extends Action {
    
    private static final Logger logger = Logger.getLogger(CloseAction.class);

    private JDesktopPane desktop;

    public CloseAction(JDesktopPane desktop) {
        super("close");

        this.desktop = desktop;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JInternalFrame frame = desktop.getSelectedFrame();

        if (frame != null) {
            if (frame.isIcon()) {
                try {
                    frame.setIcon(false);
                } catch (PropertyVetoException exc) {
                    logger.error(exc.getMessage(), exc);
                }

                frame.doDefaultCloseAction();
            } else {
                frame.doDefaultCloseAction();
            }
        }
    }
}
