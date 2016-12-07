package com.tkb.simplex.action;

import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import jxl.read.biff.BiffException;
import com.tkb.simplex.math.LinearProblem;
import com.tkb.simplex.util.Clipboard;
import com.tkb.simplex.io.ExcelFileFilter;
import com.tkb.simplex.io.ExcelFileView;
import com.tkb.simplex.gui.SpreadSheetDialog;
import com.tkb.simplex.gui.StatusBar;
import org.apache.log4j.Logger;

/**
 * An action to import linear problem from excel spreadsheet files.
 *
 * @author Akis Papadopoulos
 */
public class ImportExcelAction extends Action {
    
    private static final Logger logger = Logger.getLogger(ImportExcelAction.class);

    private JComponent parent;

    private JTextArea output, problemOutput;

    private StatusBar status;

    private JFileChooser filechooser;

    public ImportExcelAction(JComponent parent, JTextArea output, JTextArea problemOutput, StatusBar status) {
        super("import");

        this.parent = parent;
        this.output = output;
        this.problemOutput = problemOutput;
        this.status = status;

        filechooser = new JFileChooser();
        filechooser.setAcceptAllFileFilterUsed(false);
        filechooser.setFileFilter(new ExcelFileFilter());
        filechooser.setFileView(new ExcelFileView());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        File file = null;

        int answer = filechooser.showOpenDialog(parent);

        if (answer == JFileChooser.APPROVE_OPTION) {
            file = filechooser.getSelectedFile();
        }

        filechooser.setSelectedFile(null);

        try {
            if (file != null) {
                SpreadSheetDialog dialog = new SpreadSheetDialog(null, file, true);

                LinearProblem problem = dialog.call();

                if (problem != null) {
                    Clipboard.setClipboard(problem);

                    output.setText(problem.toString());
                    output.append("\n" + actions.getString("proccess.completed"));

                    problemOutput.setText(problem.toString());

                    JLabel label = status.getLabel("status.messager");

                    if (label != null) {
                        label.setText(actions.getString("import.completed").replaceFirst("#", file.getName()));
                    }

                    label = status.getLabel("status.clipboard");

                    if (label != null) {
                        label.setEnabled(true);
                    }
                } else {
                    JLabel label = status.getLabel("status.messager");

                    if (label != null) {
                        label.setText(actions.getString("import.canceled").replaceFirst("#", file.getName()));
                    }
                }
            }
        } catch (BiffException exc) {
            String message = dialogs.getString("biff.exception.message").replaceFirst("#", file.getName());
            String title = dialogs.getString("opening.document.title");

            JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
            
            logger.error(message, exc);
        } catch (FileNotFoundException exc) {
            String message = dialogs.getString("file.not.found.exception.message").replaceFirst("#", file.getName());
            String title = dialogs.getString("opening.document.title");

            JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
            
            logger.error(message, exc);
        } catch (ArrayIndexOutOfBoundsException exc) {
            String message = dialogs.getString("array.index.out.of.bounds.exception.message").replaceFirst("#", file.getName());
            String title = dialogs.getString("opening.document.title");

            JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
            
            logger.error(message, exc);
        } catch (IOException exc) {
            String message = dialogs.getString("ioexception.message").replaceFirst("#", file.getName());
            String title = dialogs.getString("opening.document.title");

            JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
            
            logger.error(message, exc);
        } catch (Exception exc) {
            String message = dialogs.getString("exception.message");
            String title = dialogs.getString("opening.document.title");

            JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
            
            logger.error(message, exc);
        }
    }
}
