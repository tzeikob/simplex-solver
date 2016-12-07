package com.tkb.simplex.action;

import static com.tkb.simplex.action.Action.dialogs;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;

/**
 * An action to open the help.
 *
 * @author Akis Papadopoulos
 */
public class HelpAction extends Action {

    private static final Logger logger = Logger.getLogger(HelpAction.class);

    private JFrame parent;

    private String os;

    private Runtime rt;

    public HelpAction(JFrame parent) {
        super("online.help");

        this.parent = parent;

        os = System.getProperty("os.name").toLowerCase();
        rt = Runtime.getRuntime();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String url = dialogs.getString("opening.help.url");

            if (os.indexOf("win") >= 0) {
                rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.indexOf("mac") >= 0) {
                rt.exec("open " + url);
            } else if (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0) {
                String[] browsers = {"epiphany", "firefox", "mozilla", "konqueror",
                    "netscape", "opera", "links", "lynx"};

                StringBuffer cmd = new StringBuffer();

                for (int i = 0; i < browsers.length; i++) {
                    cmd.append((i == 0 ? "" : " || ") + browsers[i] + " \"" + url + "\" ");
                }

                rt.exec(new String[]{"sh", "-c", cmd.toString()});
            } else {
                throw new Exception();
            }
        } catch (Exception exc) {
            String title = dialogs.getString("opening.help.url.title");
            String message = dialogs.getString("opening.help.url.exception.message");

            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);

            logger.error(message, exc);
        }
    }
}
