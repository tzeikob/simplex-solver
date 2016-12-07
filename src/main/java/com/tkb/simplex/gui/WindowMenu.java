package com.tkb.simplex.gui;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.event.MenuListener;
import javax.swing.event.MenuEvent;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Locale;
import java.beans.PropertyVetoException;
import org.apache.log4j.Logger;

/**
 * A window menu component.
 *
 * @author Akis Papadopoulos
 */
public class WindowMenu extends JMenu {

    private static final Logger logger = Logger.getLogger(WindowMenu.class);

    private static ResourceBundle menu;

    private Desktop desktop;

    private JMenuItem cascade, tilehorizontally, tilevertically, next, previous;

    static {
        try {
            menu = ResourceBundle.getBundle("bundle/menu", Locale.getDefault());
        } catch (MissingResourceException exc) {
            logger.error("Missing resource bundle file", exc);
        }
    }

    public WindowMenu(Desktop desktop) {
        super(menu.getString("window.name"));

        char mnemonic = menu.getString("window.mnemonic").charAt(0);
        setMnemonic(mnemonic);

        this.desktop = desktop;

        cascade = new JMenuItem(menu.getString("cascade.name"));
        cascade.setMnemonic(menu.getString("cascade.mnemonic").charAt(0));
        cascade.setToolTipText(menu.getString("cascade.tooltip"));
        cascade.setIcon(new ImageIcon(this.getClass().getResource("/images/" + menu.getString("cascade.icon"))));
        cascade.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowMenu.this.desktop.cascade();
            }
        });

        tilehorizontally = new JMenuItem(menu.getString("tile.horizontally.name"));
        tilehorizontally.setMnemonic(menu.getString("tile.horizontally.mnemonic").charAt(0));
        tilehorizontally.setToolTipText(menu.getString("tile.horizontally.tooltip"));
        tilehorizontally.setIcon(new ImageIcon(this.getClass().getResource("/images/" + menu.getString("tile.horizontally.icon"))));
        tilehorizontally.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowMenu.this.desktop.tile(Desktop.HORIZONTAL);
            }
        });

        tilevertically = new JMenuItem(menu.getString("tile.vertically.name"));
        tilevertically.setMnemonic(menu.getString("tile.vertically.mnemonic").charAt(0));
        tilevertically.setToolTipText(menu.getString("tile.vertically.tooltip"));
        tilevertically.setIcon(new ImageIcon(this.getClass().getResource("/images/" + menu.getString("tile.vertically.icon"))));
        tilevertically.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowMenu.this.desktop.tile(Desktop.VERTICAL);
            }
        });

        next = new JMenuItem(menu.getString("next.name"));
        next.setMnemonic(menu.getString("next.mnemonic").charAt(0));
        next.setToolTipText(menu.getString("next.tooltip"));
        next.setIcon(new ImageIcon(this.getClass().getResource("/images/" + menu.getString("next.icon"))));
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowMenu.this.desktop.next();
            }
        });

        previous = new JMenuItem(menu.getString("previous.name"));
        previous.setMnemonic(menu.getString("previous.mnemonic").charAt(0));
        previous.setToolTipText(menu.getString("previous.tooltip"));
        previous.setIcon(new ImageIcon(this.getClass().getResource("/images/" + menu.getString("previous.icon"))));
        previous.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowMenu.this.desktop.previous();
            }
        });

        addMenuListener(new MenuListener() {
            @Override
            public void menuCanceled(MenuEvent e) {
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                removeAll();
            }

            @Override
            public void menuSelected(MenuEvent e) {
                add(cascade);
                add(tilehorizontally);
                add(tilevertically);
                addSeparator();
                add(next);
                add(previous);

                JInternalFrame[] frames = WindowMenu.this.desktop.getAllFrames();

                cascade.setEnabled(frames.length > 0);
                tilehorizontally.setEnabled(frames.length > 0);
                tilevertically.setEnabled(frames.length > 0);
                next.setEnabled(frames.length > 1);
                previous.setEnabled(frames.length > 1);

                if (frames.length > 0) {
                    addSeparator();
                }

                for (int i = 0; i < frames.length; i++) {
                    FrameMenuItem menu = new FrameMenuItem(frames[i]);
                    menu.setState(i == 0);
                    menu.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            JInternalFrame frame = ((FrameMenuItem) e.getSource()).getFrame();
                            frame.moveToFront();
                            try {
                                frame.setSelected(true);
                            } catch (PropertyVetoException exc) {
                                logger.error(exc.getMessage(), exc);
                            }
                        }
                    });

                    add(menu);
                }
            }
        });
    }
}
