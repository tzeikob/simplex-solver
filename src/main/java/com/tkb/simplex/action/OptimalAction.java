package com.tkb.simplex.action;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import com.tkb.simplex.math.LinearProblem;
import com.tkb.simplex.util.Clipboard;
import com.tkb.simplex.gui.OptimalDialog;
import com.tkb.simplex.gui.StatusBar;

/**
 * An action to create optimal linear problems.
 *
 * @author Akis Papadopoulos
 */
public class OptimalAction extends Action {

    private JFrame parent;

    private JTextArea output, problemOutput;

    private StatusBar status;

    public OptimalAction(JFrame parent, JTextArea output, JTextArea problemOutput, StatusBar status) {
        super("optimal");

        this.parent = parent;
        this.output = output;
        this.problemOutput = problemOutput;
        this.status = status;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        OptimalDialog dialog = new OptimalDialog(parent, true);

        LinearProblem optimalproblem = dialog.call();

        if (optimalproblem != null) {
            Clipboard.setClipboard(optimalproblem);

            output.setText(optimalproblem.toString());
            output.append("\n" + actions.getString("proccess.completed"));

            problemOutput.setText(optimalproblem.toString());

            JLabel label = status.getLabel("status.messager");

            if (label != null) {
                label.setText(actions.getString("optimal.randomization.completed"));
            }

            label = status.getLabel("status.clipboard");

            if (label != null) {
                label.setEnabled(true);
            }
        } else {
            JLabel label = status.getLabel("status.messager");

            if (label != null) {
                label.setText(actions.getString("optimal.randomization.canceled"));
            }
        }
    }
}
