package com.tkb.simplex.action;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import com.tkb.simplex.gui.Editor;
import org.apache.log4j.Logger;

/**
 * An action to save all the opened linear problems.
 *
 * @author Akis Papadopoulos
 */
public class SaveAllAction extends Action {

    private static final Logger logger = Logger.getLogger(SaveAllAction.class);

    private JDesktopPane desktop;

    public SaveAllAction(JDesktopPane desktop) {
        super("save.all");

        this.desktop = desktop;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JInternalFrame[] frames = desktop.getAllFrames();

        for (int i = 0; i < frames.length; i++) {
            Editor editor = (Editor) frames[i];

            JComponent parent = editor;

            if (editor.isIcon()) {
                parent = desktop;
            }

            File file = editor.getFile();

            try {
                String text = editor.getText();
                char[] buffer = text.toCharArray();

                Writer output = new FileWriter(file);
                output.write(buffer, 0, buffer.length);

                output.flush();
                output.close();
            } catch (IOException exc) {
                String title = dialogs.getString("saving.document.title");
                String message = dialogs.getString("ioexception.message").replaceFirst("#", file.getName());

                JOptionPane.showInternalMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);

                logger.error(message, exc);
            } catch (Exception exc) {
                String title = dialogs.getString("saving.document.title");
                String message = dialogs.getString("exception.message");

                JOptionPane.showInternalMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);

                logger.error(message, exc);
            }
        }
    }
}
