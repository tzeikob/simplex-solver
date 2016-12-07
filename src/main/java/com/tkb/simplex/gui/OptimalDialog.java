package com.tkb.simplex.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.KeyStroke;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Locale;
import com.tkb.simplex.math.LinearProblem;
import org.apache.log4j.Logger;

/**
 * A dialog to create optimal linear problems.
 *
 * @author Akis Papadopoulos
 */
public class OptimalDialog extends JDialog implements WindowListener, ActionListener {

    private static final Logger logger = Logger.getLogger(OptimalDialog.class);

    private static ResourceBundle vocab;

    private static ResourceBundle dialogs;

    private JFrame parent;

    private JPanel container;

    private JPanel buttonspanel;

    private JTextField mdim, ndim;

    private JTextField Alower, Aupper;

    private JTextField clower, cupper;

    private JTextField[] centrefields;

    private JTextField radiusfield;

    private JButton cancel, next, optimal;

    private int m, n;

    private double Al, Au, cl, cu;

    private double[][] centre;

    private double radius;

    private LinearProblem problem;

    static {
        try {
            vocab = ResourceBundle.getBundle("bundle/vocab", Locale.getDefault());
            dialogs = ResourceBundle.getBundle("bundle/dialogs", Locale.getDefault());
        } catch (MissingResourceException exc) {
            logger.error("Missing resource bundle file", exc);
        }
    }

    public OptimalDialog(JFrame parent, boolean modal) {
        super(parent, vocab.getString("random.optimal.linear.problem"), modal);

        this.parent = parent;

        problem = null;

        container = new JPanel(new BorderLayout(2, 2));

        JPanel dimpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dimpanel.setBorder(BorderFactory.createTitledBorder(vocab.getString("dimensions")));

        dimpanel.add(new JLabel("m:"));
        mdim = new JTextField(2);
        dimpanel.add(mdim);

        dimpanel.add(new JLabel("n:"));
        ndim = new JTextField(2);
        dimpanel.add(ndim);
        container.add(dimpanel, BorderLayout.NORTH);

        JPanel rangespanel = new JPanel(new GridLayout(0, 1));
        rangespanel.setBorder(BorderFactory.createTitledBorder(vocab.getString("ranges")));

        JPanel Apanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        Apanel.setBorder(BorderFactory.createTitledBorder("A"));
        Apanel.add(new JLabel(vocab.getString("lower") + ":"));
        Alower = new JTextField(3);
        Apanel.add(Alower);
        Apanel.add(new JLabel(vocab.getString("upper") + ":"));
        Aupper = new JTextField(3);
        Apanel.add(Aupper);
        rangespanel.add(Apanel);

        JPanel cpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cpanel.setBorder(BorderFactory.createTitledBorder("c"));
        cpanel.add(new JLabel(vocab.getString("lower") + ":"));
        clower = new JTextField(3);
        cpanel.add(clower);
        cpanel.add(new JLabel(vocab.getString("upper") + ":"));
        cupper = new JTextField(3);
        cpanel.add(cupper);
        rangespanel.add(cpanel);
        container.add(rangespanel, BorderLayout.CENTER);

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

        optimal = new JButton(vocab.getString("optimal"));
        optimal.addActionListener(this);
        optimal.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JButton.WHEN_FOCUSED);

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
                JTextField invalider = mdim;
                try {
                    m = Integer.parseInt(mdim.getText());
                    if (m <= 0) {
                        throw new IllegalArgumentException();
                    }

                    invalider = ndim;
                    n = Integer.parseInt(ndim.getText());

                    if (n <= 0) {
                        throw new IllegalArgumentException();
                    }

                    invalider = Alower;
                    Al = Double.parseDouble(Alower.getText());
                    invalider = Aupper;
                    Au = Double.parseDouble(Aupper.getText());

                    if (Al > Au) {
                        throw new IllegalArgumentException();
                    }

                    invalider = clower;
                    cl = Double.parseDouble(clower.getText());
                    invalider = cupper;
                    cu = Double.parseDouble(cupper.getText());

                    if (cl > cu) {
                        throw new IllegalArgumentException();
                    }

                    JPanel extpanel = new JPanel(new BorderLayout(2, 2));
                    extpanel.setBorder(BorderFactory.createTitledBorder(vocab.getString("sphere")));

                    JPanel fieldspanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    fieldspanel.setBorder(BorderFactory.createTitledBorder(vocab.getString("centre")));

                    centrefields = new JTextField[n];

                    for (int i = 0; i < n; i++) {
                        fieldspanel.add(new JLabel("x" + (i + 1) + ":"));
                        centrefields[i] = new JTextField(3);
                        fieldspanel.add(centrefields[i]);
                    }

                    extpanel.add(fieldspanel, BorderLayout.NORTH);

                    JPanel radiuspanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
                    radiuspanel.setBorder(BorderFactory.createTitledBorder(vocab.getString("radius")));
                    radiuspanel.add(new JLabel("r:"));
                    radiusfield = new JTextField(3);
                    radiuspanel.add(radiusfield);
                    extpanel.add(radiuspanel, BorderLayout.CENTER);

                    container.removeAll();
                    container.add(extpanel, BorderLayout.CENTER);

                    buttonspanel.remove(next);
                    buttonspanel.add(optimal);

                    pack();
                    setLocationRelativeTo(parent);
                } catch (IllegalArgumentException exc) {
                    String title, message;
                    
                    if (!invalider.getText().equals("")) {
                        title = dialogs.getString("illegal.argument.exception.title");
                        message = dialogs.getString("illegal.argument.exception.message").replaceFirst("#", invalider.getText());
                    } else {
                        title = dialogs.getString("empty.argument.exception.title");
                        message = dialogs.getString("empty.argument.exception.message");
                    }

                    JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
                    
                    logger.error(message, exc);

                    problem = null;
                }
            } else if (commander == optimal) {
                JTextField invalider = centrefields[0];
                try {
                    centre = new double[1][n];

                    for (int i = 0; i < n; i++) {
                        invalider = centrefields[i];
                        centre[0][i] = Double.parseDouble(centrefields[i].getText());
                    }

                    invalider = radiusfield;
                    double radius = Double.parseDouble(radiusfield.getText());

                    if (radius <= 0) {
                        throw new IllegalArgumentException();
                    }

                    problem = LinearProblem.optimal(m, n, Al, Au, cl, cu, centre, radius);

                    setVisible(false);
                    dispose();
                } catch (IllegalArgumentException exc) {
                    String title, message;

                    if (!invalider.getText().equals("")) {
                        title = dialogs.getString("illegal.argument.exception.title");
                        message = dialogs.getString("illegal.argument.exception.message").replaceFirst("#", invalider.getText());
                    } else {
                        title = dialogs.getString("empty.argument.exception.title");
                        message = dialogs.getString("empty.argument.exception.message");
                    }

                    JOptionPane.showMessageDialog(this, message, title, JOptionPane.WARNING_MESSAGE);
                    
                    logger.error(message, exc);

                    problem = null;
                }
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
