package com.tkb.simplex.action;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Locale;
import org.apache.log4j.Logger;

/**
 * An abstract action.
 *
 * @author Akis Papadopoulos
 */
public abstract class Action extends AbstractAction {

    private static final Logger logger = Logger.getLogger(Action.class);

    protected static ResourceBundle menu;

    protected static ResourceBundle dialogs;

    protected static ResourceBundle vocab;

    protected static ResourceBundle actions;

    static {
        try {
            menu = ResourceBundle.getBundle("bundle/menu", Locale.getDefault());
            dialogs = ResourceBundle.getBundle("bundle/dialogs", Locale.getDefault());
            vocab = ResourceBundle.getBundle("bundle/vocab", Locale.getDefault());
            actions = ResourceBundle.getBundle("bundle/actions", Locale.getDefault());
        } catch (MissingResourceException exc) {
            logger.error("Missing resource bundle file", exc);
        }
    }

    public Action(String nameid) {
        super();

        String name = menu.getString(nameid + ".name");
        putValue(NAME, name);

        ImageIcon icon = new ImageIcon(this.getClass().getResource("/images/" + menu.getString(nameid + ".icon")));
        putValue(SMALL_ICON, icon);

        String tooltip = menu.getString(nameid + ".tooltip");
        putValue(SHORT_DESCRIPTION, tooltip);

        char mnemonic = menu.getString(nameid + ".mnemonic").charAt(0);
        Integer mne = new Integer((Character.valueOf(mnemonic)).hashCode());
        putValue(MNEMONIC_KEY, mne);
    }
}
