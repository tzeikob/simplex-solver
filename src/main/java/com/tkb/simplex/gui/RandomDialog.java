package com.tkb.simplex.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
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
 * A dialog to create random linear problems.
 *
 * @author Akis Papadopoulos
 */
public class RandomDialog extends JDialog implements WindowListener, ActionListener {

    private static final Logger logger = Logger.getLogger(RandomDialog.class);

    private static ResourceBundle vocab;

    private static ResourceBundle dialogs;

    private JTextField mdim, ndim;

    private JTextField Alower, Aupper;

    private JTextField clower, cupper;

    private JTextField blower, bupper;

    private JComboBox minmaxcombo, eqincombo;

    private JButton random, cancel;

    private LinearProblem problem;

    static {
        try {
            vocab = ResourceBundle.getBundle("bundle/vocab", Locale.getDefault());
            dialogs = ResourceBundle.getBundle("bundle/dialogs", Locale.getDefault());
        } catch (MissingResourceException exc) {
            logger.error("Missing resource bundle file", exc);
        }
    }

    public RandomDialog(JFrame parent, boolean modal) {
        super(parent, vocab.getString("random.linear.problem"), modal);

        problem = null;

        JPanel generalpanel = new JPanel(new BorderLayout(2, 2));

        JPanel dimpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dimpanel.setBorder(BorderFactory.createTitledBorder(vocab.getString("dimensions")));

        dimpanel.add(new JLabel("m:"));
        mdim = new JTextField(2);
        dimpanel.add(mdim);

        dimpanel.add(new JLabel("n:"));
        ndim = new JTextField(2);
        dimpanel.add(ndim);
        generalpanel.add(dimpanel, BorderLayout.NORTH);

        JPanel cardspanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        cardspanel.setBorder(BorderFactory.createTitledBorder(vocab.getString("cards")));

        cardspanel.add(new JLabel("minmax:"));
        minmaxcombo = new JComboBox();
        minmaxcombo.addItem("min");
        minmaxcombo.addItem("max");
        minmaxcombo.addItem("min|max");
        cardspanel.add(minmaxcombo);

        cardspanel.add(new JLabel("eqin:"));
        eqincombo = new JComboBox();
        eqincombo.addItem("<=");
        eqincombo.addItem(">=");
        eqincombo.addItem("<=, >=");
        cardspanel.add(eqincombo);
        generalpanel.add(cardspanel, BorderLayout.CENTER);

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

        JPanel bpanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bpanel.setBorder(BorderFactory.createTitledBorder("b"));
        bpanel.add(new JLabel(vocab.getString("lower") + ":"));
        blower = new JTextField(3);
        bpanel.add(blower);
        bpanel.add(new JLabel(vocab.getString("upper") + ":"));
        bupper = new JTextField(3);
        bpanel.add(bupper);
        rangespanel.add(bpanel);
        generalpanel.add(rangespanel, BorderLayout.SOUTH);

        add(generalpanel, BorderLayout.CENTER);

        JPanel buttonspanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        cancel = new JButton(vocab.getString("cancel"));
        cancel.addActionListener(this);
        cancel.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JButton.WHEN_FOCUSED);
        buttonspanel.add(cancel);

        random = new JButton(vocab.getString("random"));
        random.addActionListener(this);
        random.registerKeyboardAction(this, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JButton.WHEN_FOCUSED);
        buttonspanel.add(random);
        add(buttonspanel, BorderLayout.SOUTH);

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
            if (commander == random) {
                int m, n;
                int minmaxcard;
                int eqincard;
                double Al, Au, cl, cu, bl, bu;

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

                    String choice = (String) minmaxcombo.getSelectedItem();

                    if (choice.equalsIgnoreCase("min")) {
                        minmaxcard = -1;
                    } else if (choice.equalsIgnoreCase("max")) {
                        minmaxcard = 1;
                    } else {
                        minmaxcard = 0;
                    }

                    choice = (String) eqincombo.getSelectedItem();

                    if (choice.equalsIgnoreCase("<=")) {
                        eqincard = 4;
                    } else if (choice.equalsIgnoreCase(">=")) {
                        eqincard = 1;
                    } else {
                        eqincard = 5;
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

                    invalider = blower;
                    bl = Double.parseDouble(blower.getText());
                    invalider = bupper;
                    bu = Double.parseDouble(bupper.getText());

                    if (bl > bu) {
                        throw new IllegalArgumentException();
                    }

                    problem = LinearProblem.random(m, n, minmaxcard, Al, Au, cl, cu, bl, bu, eqincard);

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
            } else {
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
