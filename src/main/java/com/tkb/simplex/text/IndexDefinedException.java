package com.tkb.simplex.text;

/**
 * An index defined exception.
 *
 * @author Akis Papadopoulos
 */
public class IndexDefinedException extends MySyntaxException {

    public static final int IN_FUNCTION = 6;
    public static final int IN_OBJECTIVE = 7;
    public static final int IN_CONSTRAINT = 8;

    private String index;

    private String function;

    public IndexDefinedException() {
        super();
    }

    public IndexDefinedException(int id, String index, String function) {
        super(id);

        this.index = index;
        this.function = function;

        message = message.replaceFirst("#", index);
        message = message.replaceFirst("#", function);
    }
}
