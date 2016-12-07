package com.tkb.simplex.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Locale;
import org.apache.log4j.Logger;

/**
 * An about dialog component.
 *
 * @author Akis Papadopoulos
 */
public class AboutDialog extends JDialog implements MouseListener {

    private static final Logger logger = Logger.getLogger(AboutDialog.class);

    private static ResourceBundle vocab;

    static {
        try {
            vocab = ResourceBundle.getBundle("bundle/vocab", Locale.getDefault());
        } catch (MissingResourceException exc) {
            logger.error("Missing resource bundle file", exc);
        }
    }

    public AboutDialog(JFrame parent, boolean modal) {
        super(parent, vocab.getString("about"), modal);

        StringBuilder text = new StringBuilder();

        text.append("Copyright 2007, Akis Papadopoulos iakopap@gmail.com");

        JLabel label = new JLabel(text.toString());
        label.addMouseListener(this);
        add(label);

        pack();

        setLocationRelativeTo(parent);
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        setVisible(false);
        dispose();
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }
}
