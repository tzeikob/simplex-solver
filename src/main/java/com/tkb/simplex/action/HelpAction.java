package com.tkb.simplex.action;

import java.awt.event.ActionEvent;
import org.apache.log4j.Logger;

/**
 * An action to open the help.
 *
 * @author Akis Papadopoulos
 */
public class HelpAction extends Action {
    
    private static final Logger logger = Logger.getLogger(HelpAction.class);

    public HelpAction() {
        super("online.help");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Runtime runtime = Runtime.getRuntime();

            Process process = runtime.exec(new String[]{"cmd.exe", "/c", "help.chm"});
        } catch (Exception exc) {
            logger.error(exc.getMessage(), exc);
        }
    }
}
