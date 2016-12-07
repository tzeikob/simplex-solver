package com.tkb.simplex.io;

import com.tkb.simplex.util.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.filechooser.FileView;
import java.io.File;

/**
 * A text file view.
 *
 * @author Akis Papadopoulos
 */
public class TextFileView extends FileView {

    public static final ImageIcon ICON = new ImageIcon(TextFileView.class.getResource("/images/doc.png"));

    @Override
    public String getName(File file) {
        return null;
    }

    @Override
    public String getDescription(File file) {
        return null;
    }

    @Override
    public Boolean isTraversable(File file) {
        return null;
    }

    @Override
    public String getTypeDescription(File file) {
        String ext = Toolkit.getFileExtension(file);

        if (ext != null && ext.equals(Toolkit.TXT)) {
            return "TXT text";
        } else {
            return null;
        }
    }

    @Override
    public Icon getIcon(File file) {
        String ext = Toolkit.getFileExtension(file);

        if (ext != null && ext.equals(Toolkit.TXT)) {
            return ICON;
        } else {
            return null;
        }
    }
}
