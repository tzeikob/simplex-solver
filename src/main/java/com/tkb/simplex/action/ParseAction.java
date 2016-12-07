package com.tkb.simplex.action;

import javax.swing.JDesktopPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import com.tkb.simplex.text.LinearProblemParser;
import com.tkb.simplex.math.LinearProblem;
import com.tkb.simplex.util.Clipboard;
import com.tkb.simplex.gui.Editor;
import com.tkb.simplex.gui.StatusBar;

/**
 * An action to fire up the parsing of linear problem.
 *
 * @author Akis Papadopoulos
 */
public class ParseAction extends Action {

    private static LinearProblemParser parser = new LinearProblemParser();

    private JDesktopPane desktop;

    private JTextArea output, problemOutput;

    private StatusBar status;

    public ParseAction(JDesktopPane desktop, JTextArea output, JTextArea problemOutput, StatusBar status) {
        super("parse");

        this.desktop = desktop;
        this.output = output;
        this.problemOutput = problemOutput;
        this.status = status;

        KeyStroke accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK);
        putValue(ACCELERATOR_KEY, accelerator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Editor editor = (Editor) desktop.getSelectedFrame();

        if (editor != null) {
            String text = editor.getText();

            LinearProblem lp = parser.parse(text);

            if (lp != null) {
                Clipboard.setClipboard(lp);

                output.setText(lp.toString());
                output.append("\n" + actions.getString("proccess.completed"));

                problemOutput.setText(lp.toString());

                JLabel label = status.getLabel("status.messager");

                if (label != null) {
                    label.setText(actions.getString("parsing.completed").replaceFirst("#", editor.getTitle()));
                }

                label = status.getLabel("status.clipboard");

                if (label != null) {
                    label.setEnabled(true);
                }
            } else {
                Clipboard.clear();

                output.setText(parser.getReport());
                output.append("\n" + actions.getString("proccess.completed"));

                problemOutput.setText(null);

                JLabel label = status.getLabel("status.messager");

                if (label != null) {
                    label.setText(actions.getString("parsing.failed").replaceFirst("#", editor.getTitle()));
                }

                label = status.getLabel("status.clipboard");

                if (label != null) {
                    label.setEnabled(false);
                }
            }
        }
    }
}
