package com.tkb.simplex.gui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
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

        text.append("\nCopyright 2007, Akis Papadopoulos\n\n")
                .append("Simplex solver is an open source experimental project, a desktop application for 2D/3D linear\n")
                .append("problem solving using the Simplex algorithm geometry.\n\n")
                .append("The repository is available on https://github.com/tzeikob/simplex-solver and is\n\n")
                .append("Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this\n")
                .append("file except in compliance with the License. You may obtain a copy of the License at \n\n")
                .append("http://www.apache.org/licenses/LICENSE-2.0 \n\n")
                .append("Unless required by applicable law or agreed to in writing, software distributed under the\n")
                .append("License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,\n")
                .append("either express or implied. See the License for the specific language governing permissions\n")
                .append("and limitations under the License.\n\n")
                .append("Contact information, iakopap@gmail.com\n");

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
