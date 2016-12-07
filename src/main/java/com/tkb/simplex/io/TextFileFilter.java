package com.tkb.simplex.io;

import com.tkb.simplex.util.Toolkit;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * A text file filter.
 *
 * @author Akis Papadopoulos
 */
public class TextFileFilter extends FileFilter {

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        String ext = Toolkit.getFileExtension(file);

        if (ext != null && ext.equals(Toolkit.TXT)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getDescription() {
        return "*.txt";
    }
}
