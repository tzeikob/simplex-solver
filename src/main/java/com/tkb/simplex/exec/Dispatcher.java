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
        System.out.println("\nCopyright 2007 Akis Papadopoulos, https://github.com/tzeikob/simplex-solver\n");
        System.out.println("Licensed under the Apache License, Version 2.0 (the \"License\"); you may not use this");
        System.out.println("file except in compliance with the License. You may obtain a copy of the License at \n");
        System.out.println("http://www.apache.org/licenses/LICENSE-2.0 \n");
        System.out.println("Unless required by applicable law or agreed to in writing, software distributed under the");
        System.out.println("License is distributed on an \"AS IS\" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,");
        System.out.println("either express or implied. See the License for the specific language governing permissions");
        System.out.println("and limitations under the License.\n");

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
