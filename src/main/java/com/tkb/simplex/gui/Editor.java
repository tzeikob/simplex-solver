package com.tkb.simplex.gui;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.InternalFrameEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Locale;
import org.apache.log4j.Logger;

/**
 * An editor internal frame component.
 *
 * @author Akis Papadopoulos
 */
public class Editor extends JInternalFrame implements InternalFrameListener {

    private static final Logger logger = Logger.getLogger(Editor.class);

    private static ResourceBundle dialogs;

    protected static int newest = 0;

    protected static int opened = 0;

    protected static int activated = 0;

    protected static int iconified = 0;

    private static Font font = new Font("Sylfaen", Font.PLAIN, 15);

    private JTextArea editor;

    private File file;

    static {
        try {
            dialogs = ResourceBundle.getBundle("bundle/dialogs", Locale.getDefault());
        } catch (MissingResourceException exc) {
            logger.error("Missing resource bundle file", exc);
        }
    }

    public Editor() {
        super("document #" + (newest + 1), true, true, true, true);

        editor = new JTextArea();
        editor.setFont(font);

        add(new JScrollPane(editor), BorderLayout.CENTER);

        file = new File("data/" + "document #" + (newest + 1) + ".txt");

        addInternalFrameListener(this);
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);

        setFrameIcon(new ImageIcon(this.getClass().getResource("/images/doc.png")));
        getLayeredPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        setBounds(0, 0, 400, 300);

        newest++;
    }

    public Editor(File file) throws IOException {
        super(file.getName(), true, true, true, true);

        editor = new JTextArea();
        editor.setFont(font);

        add(new JScrollPane(editor), BorderLayout.CENTER);

        Reader input = new FileReader(file);

        char[] buffer = new char[4096];
        int nch;

        while ((nch = input.read(buffer, 0, buffer.length)) != -1) {
            editor.append(new String(buffer, 0, nch));
        }

        input.close();

        this.file = file;

        addInternalFrameListener(this);
        setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);

        setFrameIcon(new ImageIcon(this.getClass().getResource("/images/doc.png")));
        getLayeredPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        setBounds(0, 0, 400, 300);
    }

    public String getText() {
        return editor.getText();
    }

    public File getFile() {
        return file;
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        String title = dialogs.getString("closing.document.title");
        String message = dialogs.getString("saving.document.query").replaceFirst("#", file.getName());

        int answer = JOptionPane.showInternalConfirmDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (answer == JOptionPane.YES_OPTION) {
            try {
                String text = editor.getText();
                char[] buffer = text.toCharArray();

                Writer output = new FileWriter(file);
                output.write(buffer, 0, buffer.length);

                output.flush();
                output.close();
            } catch (IOException exc) {
                title = dialogs.getString("saving.document.title");
                message = dialogs.getString("ioexception.message").replaceFirst("#", file.getName());

                JOptionPane.showInternalMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
                
                logger.error(message, exc);
            } catch (Exception exc) {
                title = dialogs.getString("saving.document.title");
                message = dialogs.getString("exception.message");

                JOptionPane.showInternalMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
                
                logger.error(message, exc);
            }

            dispose();
        } else if (answer == JOptionPane.NO_OPTION) {
            dispose();
        }
    }

    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
        opened++;
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
        opened--;
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
        activated++;
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
        activated--;
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
        iconified++;
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
        iconified--;
    }
}
