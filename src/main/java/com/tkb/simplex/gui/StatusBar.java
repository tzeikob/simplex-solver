package com.tkb.simplex.gui;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import java.awt.Color;

/**
 * A status bar component.
 *
 * @author Akis Papadopoulos
 */
public class StatusBar extends JPanel {

    private GridBagConstraints constraints;

    public StatusBar() {
        super(new GridBagLayout());

        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

        constraints = new GridBagConstraints();
        constraints.insets = new Insets(2, 1, 2, 1);
        constraints.weighty = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.anchor = GridBagConstraints.WEST;
    }

    public void addLabel(String name, String caption, ImageIcon icon, int align, double scaling, boolean enabled) {
        JLabel label = new JLabel(caption, icon, align);

        label.setName(name);
        label.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        label.setPreferredSize(new Dimension(0, 20));
        label.setEnabled(enabled);

        constraints.weightx = scaling;
        constraints.gridx = getComponentCount();

        add(label, constraints);
    }

    public JLabel getLabel(String name) {
        for (int i = 0; i < getComponentCount(); i++) {
            Component component = getComponent(i);

            if (component != null && component instanceof JLabel) {
                JLabel label = (JLabel) component;

                if (name != null && name.equalsIgnoreCase(label.getName())) {
                    return label;
                }
            }
        }

        return null;
    }

    public void removeLabel(String name) {
        for (int i = 0; i < getComponentCount(); i++) {
            Component component = getComponent(i);

            if (component != null && component instanceof JLabel) {
                JLabel label = (JLabel) component;

                if (name != null && name.equalsIgnoreCase(label.getName())) {
                    remove(label);
                }
            }
        }
    }
}
