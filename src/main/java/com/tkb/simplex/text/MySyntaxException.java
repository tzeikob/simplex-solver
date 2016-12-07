package com.tkb.simplex.text;

import java.util.ResourceBundle;
import java.util.MissingResourceException;
import org.apache.log4j.Logger;

/**
 * A syntax exception.
 *
 * @author Akis Papadopoulos
 */
public class MySyntaxException extends Exception {

    private static final Logger logger = Logger.getLogger(MySyntaxException.class);

    public static final int DEFAULT_ERROR = 0;

    public static final int MISSING_OBJECT = 1;

    public static final int MISSING_Z = 2;

    public static final int MISSING_ST = 3;

    public static final int ON_OBJECTIVE_FUNCTION = 4;

    public static final int ON_SUBJECT = 5;

    protected static ResourceBundle syntax;

    protected static ResourceBundle vocab;

    protected int id;

    protected String message;

    static {
        try {
            syntax = ResourceBundle.getBundle("bundle/syntax");
            vocab = ResourceBundle.getBundle("bundle/vocab");
        } catch (MissingResourceException exc) {
            logger.error("Missing resource bundle file", exc);
        }
    }

    public MySyntaxException() {
        super();
    }

    public MySyntaxException(int id) {
        this.id = id;
        message = syntax.getString(String.valueOf(id));
    }

    @Override
    public String getMessage() {
        return vocab.getString("syntax.error") + ": " + message + "\n";
    }

    @Override
    public String toString() {
        return vocab.getString("syntax.error") + ": " + message + "\n";
    }
}
