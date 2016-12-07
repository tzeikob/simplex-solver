package com.tkb.simplex.action;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import jxl.Workbook;
import jxl.write.WritableWorkbook;
import jxl.write.WritableSheet;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import com.tkb.simplex.math.LinearProblem;
import com.tkb.simplex.util.Clipboard;
import com.tkb.simplex.io.ExcelFileFilter;
import com.tkb.simplex.io.ExcelFileView;
import org.apache.log4j.Logger;

/**
 * An action to export a linear problem into excel spreadsheet files.
 *
 * @author Akis Papadopoulos
 */
public class ExportExcelAction extends Action {
    
    private static final Logger logger = Logger.getLogger(ExportExcelAction.class);

    private JComponent parent;

    private JFileChooser filechooser;

    public ExportExcelAction(JComponent parent) {
        super("export");

        this.parent = parent;

        filechooser = new JFileChooser();
        filechooser.setAcceptAllFileFilterUsed(false);
        filechooser.setFileFilter(new ExcelFileFilter());
        filechooser.setFileView(new ExcelFileView());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object object = Clipboard.getClipboard();

        if (object != null && object instanceof LinearProblem) {
            LinearProblem problem = (LinearProblem) object;

            File file = null;

            int answer = filechooser.showSaveDialog(parent);

            if (answer == JFileChooser.APPROVE_OPTION) {
                file = filechooser.getSelectedFile();

                try {
                    int m = problem.size()[0];
                    int n = problem.size()[1];

                    double minmax = problem.minmax();

                    double[][] A = problem.A();
                    double[][] c = problem.c();
                    double[][] b = problem.b();

                    double[][] eqin = problem.eqin();

                    WritableWorkbook book = Workbook.createWorkbook(file);

                    WritableSheet sheet = book.createSheet("Linear Problem", 0);

                    sheet.addCell(new Label(0, 0, "Optimization"));

                    if (minmax == -1) {
                        sheet.addCell(new Label(0, 1, "min"));
                    } else {
                        sheet.addCell(new Label(0, 1, "max"));
                    }

                    sheet.addCell(new Label(0, 3, "Objective function"));

                    for (int i = 0; i < n; i++) {
                        sheet.addCell(new Label(i + 1, 3, "x" + (i + 1)));
                        sheet.addCell(new Number(i + 1, 4, c[0][i]));
                    }

                    sheet.addCell(new Label(0, 6, "Subject to"));

                    for (int i = 0; i < m; i++) {
                        for (int j = 0; j < n; j++) {
                            sheet.addCell(new Number(j + 1, i + 6, A[i][j]));

                            if (eqin[i][0] == -1) {
                                sheet.addCell(new Label(n + 1, i + 6, "<="));
                            } else {
                                sheet.addCell(new Label(n + 1, i + 6, ">="));
                            }

                            sheet.addCell(new Number(n + 2, i + 6, b[i][0]));
                        }
                    }

                    book.write();
                    book.close();
                } catch (RowsExceededException exc) {
                    String title = dialogs.getString("saving.document.title");
                    String message = exc.getMessage();

                    JOptionPane.showInternalMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
                    
                    logger.error(message, exc);
                } catch (WriteException exc) {
                    String title = dialogs.getString("saving.document.title");
                    String message = exc.getMessage();

                    JOptionPane.showInternalMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
                    
                    logger.error(message, exc);
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
