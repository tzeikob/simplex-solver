package com.tkb.simplex.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Locale;
import java.io.File;
import java.io.IOException;
import jxl.Workbook;
import jxl.Sheet;
import jxl.Cell;
import jxl.read.biff.BiffException;
import com.tkb.simplex.math.LinearProblem;
import org.apache.log4j.Logger;

/**
 * A spreadsheet dialog to import linear problems.
 *
 * @author Akis Papadopoulos
 */
public class SpreadSheetDialog extends JDialog implements WindowListener, ActionListener {

    private static final Logger logger = Logger.getLogger(SpreadSheetDialog.class);

    private static ResourceBundle vocab;

    private static ResourceBundle dialogs;

    private JTabbedPane container;

    private JTable[] tables;

    private JPanel buttonspanel;

    private JButton cancel, next, finish;

    private int m, n;

    private double minmax;

    private double[][] A;

    private double[][] c;

    private double[][] b;

    private double[][] eqin;

    private LinearProblem problem;

    static {
        try {
            vocab = ResourceBundle.getBundle("bundle/vocab", Locale.getDefault());
            dialogs = ResourceBundle.getBundle("bundle/dialogs", Locale.getDefault());
        } catch (MissingResourceException exc) {
            logger.error("Missing resource bundle file", exc);
        }
    }

    public SpreadSheetDialog(JFrame parent, File file, boolean modal) throws IOException, BiffException {
        super(parent, dialogs.getString("import.step1.message"), modal);

        problem = null;

        container = new JTabbedPane();
        container.setTabPlacement(JTabbedPane.BOTTOM);

        if (file != null) {
            Workbook book = Workbook.getWorkbook(file);

            int nos = book.getNumberOfSheets();
            tables = new JTable[nos];

            for (int i = 0; i < nos; i++) {
                Sheet sheet = book.getSheet(i);
                String name = sheet.getName();

                int rows = sheet.getRows();
                int columns = sheet.getColumns();

                tables[i] = new JTable(rows + 50, columns + 50);
                tables[i].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                tables[i].setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
                tables[i].setCellSelectionEnabled(true);
                tables[i].setSelectionBackground(Color.orange);

                for (int r = 0; r < rows; r++) {
                    for (int c = 0; c < columns; c++) {
                        Cell cell = sheet.getCell(c, r);
                        String content = cell.getContents();
                        tables[i].setValueAt(content, r, c);
                    }
                }

                container.add(name, new JScrollPane(tables[i]));
            }

            book.close();
        }

        add(container, BorderLayout.CENTER);

        buttonspanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cancel = new JButton(vocab.getString("cancel"));
        cancel.addActionListener(this);
        cancel.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JButton.WHEN_FOCUSED);
        buttonspanel.add(cancel);

        next = new JButton(vocab.getString("next"));
        next.addActionListener(this);
        next.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JButton.WHEN_FOCUSED);
        buttonspanel.add(next);
        add(buttonspanel, BorderLayout.SOUTH);

        finish = new JButton(vocab.getString("finish"));
        finish.addActionListener(this);
        finish.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JButton.WHEN_FOCUSED);

        pack();
        setLocationRelativeTo(parent);
        setResizable(false);

        addWindowListener(this);
    }

    public LinearProblem call() {
        setVisible(true);

        return problem;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object object = e.getSource();

        if (object != null && object instanceof JButton) {
            JButton commander = (JButton) object;

            if (commander == next) {
                try {
                    int index = container.getSelectedIndex();
                    if (index != -1) {
                        JTable table = tables[index];

                        if (minmax != -1 && minmax != 1) {
                            int row = table.getSelectedRow();
                            int col = table.getSelectedColumn();

                            if (row < 0 || col < 0) {
                                throw new IllegalArgumentException("no.selection.exception");
                            }

                            String content = (String) table.getValueAt(row, col);

                            if (content != null) {
                                if (content.equalsIgnoreCase("max")) {
                                    minmax = 1;
                                } else if (content.equalsIgnoreCase("min")) {
                                    minmax = -1;
                                } else {
                                    throw new IllegalArgumentException("minmax.format.exception");
                                }
                            } else {
                                throw new IllegalArgumentException("minmax.format.exception");
                            }

                            setTitle(dialogs.getString("import.step2.message"));
                        } else if (c == null) {
                            int[] rows = table.getSelectedRows();
                            int[] cols = table.getSelectedColumns();

                            double[][] array = new double[1][rows.length * cols.length];

                            int k = 0;
                            for (int i = 0; i < rows.length; i++) {
                                for (int j = 0; j < cols.length; j++) {
                                    String content = (String) table.getValueAt(rows[i], cols[j]);

                                    if (content != null) {
                                        array[0][k++] = Double.parseDouble(content);
                                    } else {
                                        throw new NumberFormatException();
                                    }
                                }
                            }

                            n = array[0].length;
                            c = array;

                            setTitle(dialogs.getString("import.step3.message"));
                        } else if (A == null) {
                            int[] rows = table.getSelectedRows();
                            int[] cols = table.getSelectedColumns();

                            if (cols.length != n) {
                                throw new IllegalArgumentException("array.dimensions.not.matched.exception");
                            }

                            double[][] array = new double[rows.length][cols.length];

                            for (int i = 0; i < rows.length; i++) {
                                for (int j = 0; j < cols.length; j++) {
                                    String content = (String) table.getValueAt(rows[i], cols[j]);

                                    if (content != null) {
                                        array[i][j] = Double.parseDouble(content);
                                    } else {
                                        throw new NumberFormatException();
                                    }
                                }
                            }

                            m = array.length;
                            A = array;

                            setTitle(dialogs.getString("import.step4.message"));
                        } else if (eqin == null) {
                            int[] rows = table.getSelectedRows();
                            int[] cols = table.getSelectedColumns();

                            if (rows.length * cols.length != m) {
                                throw new IllegalArgumentException("array.dimensions.not.matched.exception");
                            }

                            double[][] array = new double[rows.length * cols.length][1];

                            int k = 0;
                            for (int i = 0; i < rows.length; i++) {
                                for (int j = 0; j < cols.length; j++) {
                                    String content = (String) table.getValueAt(rows[i], cols[j]);

                                    if (content != null) {
                                        if (content.equals("<=")) {
                                            array[k++][0] = -1;
                                        } else if (content.equals(">=")) {
                                            array[k++][0] = 1;
                                        } else {
                                            throw new IllegalArgumentException("EqinFormatException");
                                        }
                                    } else {
                                        throw new IllegalArgumentException("EqinFormatException");
                                    }
                                }
                            }

                            eqin = array;

                            setTitle(dialogs.getString("import.step5.message"));
                        } else if (b == null) {
                            int[] rows = table.getSelectedRows();
                            int[] cols = table.getSelectedColumns();

                            if (rows.length * cols.length != m) {
                                throw new IllegalArgumentException("array.dimensions.not.matched.exception");
                            }

                            double[][] array = new double[rows.length * cols.length][1];

                            int k = 0;

                            for (int i = 0; i < rows.length; i++) {
                                for (int j = 0; j < cols.length; j++) {
                                    String content = (String) table.getValueAt(rows[i], cols[j]);

                                    if (content != null) {
                                        array[k++][0] = Double.parseDouble(content);
                                    } else {
                                        throw new NumberFormatException();
                                    }
                                }
                            }

                            b = array;

                            setTitle(dialogs.getString("import.finish.message"));

                            buttonspanel.remove(next);
                            buttonspanel.add(finish);
                            buttonspanel.revalidate();
                        }
                    }
                } catch (NumberFormatException exc) {
                    String title, message;
                    title = dialogs.getString("number.format.exception.title");
                    message = dialogs.getString("number.format.exception.message");

                    JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
                    
                    logger.error(message, exc);
                } catch (IllegalArgumentException exc) {
                    String title, message;
                    title = dialogs.getString(exc.getMessage() + ".title");
                    message = dialogs.getString(exc.getMessage() + ".message");

                    JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
                    
                    logger.error(message, exc);
                }
            } else if (commander == finish) {
                problem = new LinearProblem(minmax, A, c, b, eqin);

                setVisible(false);
                dispose();
            } else if (commander == cancel) {
                problem = null;

                setVisible(false);
                dispose();
            }
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        problem = null;
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }
}
