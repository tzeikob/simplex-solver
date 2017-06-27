package com.tkb.simplex.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Locale;
import javax.swing.JTextArea;
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

        text.append("Simplex solver is an open source experimental project, a desktop application for 2D/3D linear\n")
                .append("problem solving using the Simplex algorithm geometry.\n\n")
                .append("The repository is available on https://github.com/tzeikob/simplex-solver.\n\n");

        try (BufferedReader br = new BufferedReader(new FileReader("LICENSE"))) {
            String line = null;
            while ((line = br.readLine()) != null) {
                text.append(line).append('\n');
            }
        } catch (IOException exc) {
            text.append("Missing LICENSE file\n");
        }

        text.append("\nContact information, iakopap@gmail.com\n");

        JTextArea textArea = new JTextArea(text.toString());
        textArea.addMouseListener(this);
        add(textArea);
        textArea.setEditable(false);

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
