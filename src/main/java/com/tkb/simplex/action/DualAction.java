package com.tkb.simplex.action;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import com.tkb.simplex.math.LinearProblem;
import com.tkb.simplex.util.Clipboard;
import com.tkb.simplex.gui.StatusBar;

/**
 * An action to create dual linear problems.
 *
 * @author Akis Papadopoulos
 */
public class DualAction extends Action {

    private JTextArea output;

    private StatusBar status;

    public DualAction(JTextArea output, StatusBar status) {
        super("dual");

        this.output = output;
        this.status = status;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object data = Clipboard.getClipboard();

        if (data != null && data instanceof LinearProblem) {
            LinearProblem prime = (LinearProblem) data;
            LinearProblem dual = prime.dual();

            output.setText(dual.toString());
            output.append("\n" + actions.getString("proccess.completed"));

            JLabel label = status.getLabel("status.messager");

            if (label != null) {
                label.setText(actions.getString("dual.computed"));
            }
        }
    }
}
