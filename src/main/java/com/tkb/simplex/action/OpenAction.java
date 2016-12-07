package com.tkb.simplex.action;

import javax.swing.KeyStroke;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import com.tkb.simplex.gui.Desktop;
import com.tkb.simplex.gui.Editor;
import com.tkb.simplex.io.TextFileFilter;
import com.tkb.simplex.io.TextFileView;
import org.apache.log4j.Logger;

/**
 * An action to open a linear problem from text file.
 *
 * @author Akis Papadopoulos
 */
public class OpenAction extends Action {

    private static final Logger logger = Logger.getLogger(OpenAction.class);

    private Desktop desktop;

    private JFileChooser filechooser;

    public OpenAction(Desktop desktop) {
        super("open");

        this.desktop = desktop;

        filechooser = new JFileChooser();
        filechooser.setAcceptAllFileFilterUsed(false);
        filechooser.setFileFilter(new TextFileFilter());
        filechooser.setFileView(new TextFileView());

        KeyStroke accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK);
        putValue(ACCELERATOR_KEY, accelerator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = null;

        int answer = filechooser.showOpenDialog(desktop);

        if (answer == JFileChooser.APPROVE_OPTION) {
            file = filechooser.getSelectedFile();
        }

        filechooser.setSelectedFile(null);

        try {
            if (file != null) {
                Editor editor = new Editor(file);

                desktop.add(editor);
            }
        } catch (FileNotFoundException exc) {
            String message = dialogs.getString("file.not.found.exception.message").replaceFirst("#", file.getName());
            String title = dialogs.getString("opening.document.title");

            JOptionPane.showMessageDialog(desktop, message, title, JOptionPane.ERROR_MESSAGE);

            logger.error(message, exc);
        } catch (ArrayIndexOutOfBoundsException exc) {
            String message = dialogs.getString("array.index.out.of.bounds.exception.message").replaceFirst("#", file.getName());
            String title = dialogs.getString("opening.document.title");

            JOptionPane.showMessageDialog(desktop, message, title, JOptionPane.ERROR_MESSAGE);

            logger.error(message, exc);
        } catch (IOException exc) {
            String message = dialogs.getString("ioexception.message").replaceFirst("#", file.getName());
            String title = dialogs.getString("opening.document.title");

            JOptionPane.showMessageDialog(desktop, message, title, JOptionPane.ERROR_MESSAGE);

            logger.error(message, exc);
        } catch (Exception exc) {
            String message = dialogs.getString("exception.message");
            String title = dialogs.getString("opening.document.title");

            JOptionPane.showMessageDialog(desktop, message, title, JOptionPane.ERROR_MESSAGE);

            logger.error(message, exc);
        }
    }
}
