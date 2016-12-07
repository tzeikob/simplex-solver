package com.tkb.simplex.action;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.io.IOException;
import com.tkb.simplex.gui.Editor;
import com.tkb.simplex.io.TextFileFilter;
import com.tkb.simplex.io.TextFileView;
import org.apache.log4j.Logger;

/**
 * An action to save the selected linear problem into one of the supported file
 * types.
 *
 * @author Akis Papadopoulos
 */
public class SaveAsAction extends Action {

    private static final Logger logger = Logger.getLogger(SaveAllAction.class);

    private JDesktopPane desktop;

    private JFileChooser filechooser;

    public SaveAsAction(JDesktopPane desktop) {
        super("save.as");

        this.desktop = desktop;

        filechooser = new JFileChooser();
        filechooser.setAcceptAllFileFilterUsed(false);
        filechooser.setFileFilter(new TextFileFilter());
        filechooser.setFileView(new TextFileView());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Editor editor = (Editor) desktop.getSelectedFrame();

        if (editor != null) {
            JComponent parent = editor;

            if (editor.isIcon()) {
                parent = desktop;
            }

            File file = null;

            int answer = filechooser.showSaveDialog(desktop);

            if (answer == JFileChooser.APPROVE_OPTION) {
                file = filechooser.getSelectedFile();

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
}
