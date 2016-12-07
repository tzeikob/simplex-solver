package com.tkb.simplex.action;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import com.tkb.simplex.gui.Editor;
import org.apache.log4j.Logger;

/**
 * An action to save the selected linear problem.
 *
 * @author Akis Papadopoulos
 */
public class SaveAction extends Action {

    private static final Logger logger = Logger.getLogger(SaveAction.class);

    private JDesktopPane desktop;

    public SaveAction(JDesktopPane desktop) {
        super("save");

        this.desktop = desktop;

        KeyStroke accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK);
        putValue(ACCELERATOR_KEY, accelerator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Editor editor = (Editor) desktop.getSelectedFrame();

        if (editor != null) {
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
