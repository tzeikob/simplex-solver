package com.tkb.simplex.util;

/**
 * A module to store generic data objects into the application's clipboard.
 *
 * @author Akis Papadopoulos
 */
public final class Clipboard {

    private static Object data;

    public static void setClipboard(Object object) {
        data = object;
    }

    public static Object getClipboard() {
        return data;
    }

    public static void clear() {
        data = null;
    }
}
