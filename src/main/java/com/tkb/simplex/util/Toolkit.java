package com.tkb.simplex.util;

import java.io.File;

/**
 * A utilities toolkit module.
 *
 * @author Akiss Papadopoulos
 */
public final class Toolkit {

    public static final String TXT = "txt";

    public static final String RTF = "rtf";

    public static final String DOC = "doc";

    public static final String XLS = "xls";

    public static final String PNG = "png";

    public static final String GIF = "gif";

    public static final String JPEG = "jpeg";

    public static final String JPG = "jpg";

    public static final String BMP = "bmp";

    public static final String TIFF = "tiff";

    /**
     * A method extracting the extension of the given file.
     *
     * @param file the file.
     * @return the extension of the file.
     */
    public static String getFileExtension(File file) {
        String ext = null;

        String name = file.getName();

        int pos = name.lastIndexOf(".");

        if ((pos > 0) && (pos < name.length() - 1)) {
            ext = name.substring(pos + 1).toLowerCase();
        }

        return ext;
    }
}
