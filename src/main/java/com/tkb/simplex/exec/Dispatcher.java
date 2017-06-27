package com.tkb.simplex.exec;

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.Dimension;
import java.awt.Toolkit;
import com.tkb.simplex.gui.MainFrame;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * An entry frame dispatcher.
 *
 * @author Akis Papadopoulos
 */
public class Dispatcher {

    private static final Logger logger;

    static {
        // Setting up the debug log output file
        System.setProperty("log.file", "debug.log");

        logger = Logger.getLogger(Dispatcher.class);

        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        System.setProperty("sun.awt.noerasebackground", "true");

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        try {
            MetalLookAndFeel.setCurrentTheme(new DefaultMetalTheme());

            String laf = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(laf);
        } catch (Exception exc) {
            JOptionPane.showMessageDialog(null, exc.toString(),
                    exc.getClass().getSimpleName(), JOptionPane.ERROR_MESSAGE);

            logger.error(exc.getMessage(), exc);

            System.exit(0);
        }
    }

    public static void main(String[] args) {
        // Printing the license notice
        try (BufferedReader br = new BufferedReader(new FileReader("LICENSE"))) {
            String line = null;
            
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException exc) {
            System.err.println("Error: Application aborted, missing LICENSE file.");
            System.exit(1);
        }

        final JFrame frame = new MainFrame("Simplex Solver", new Dimension(800, 600));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                logger.info("Starting up the application");

                frame.setVisible(true);
            }
        });
    }
}
