package com.tkb.simplex.io;

import com.tkb.simplex.util.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.Icon;
import javax.swing.filechooser.FileView;
import java.io.File;

/**
 * An Excel file view.
 *
 * @author Akis Papadopoulos
 */
public class ExcelFileView extends FileView {

    public static final ImageIcon ICON = new ImageIcon(ExcelFileView.class.getResource("/images/xls.png"));

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

        if (ext != null && ext.equals(Toolkit.XLS)) {
            return "XLS excel";
        } else {
            return null;
        }
    }

    @Override
    public Icon getIcon(File file) {
        String ext = Toolkit.getFileExtension(file);

        if (ext != null && ext.equals(Toolkit.XLS)) {
            return ICON;
        } else {
            return null;
        }
    }
}
