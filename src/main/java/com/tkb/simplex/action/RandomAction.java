package com.tkb.simplex.action;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import com.tkb.simplex.math.LinearProblem;
import com.tkb.simplex.util.Clipboard;
import com.tkb.simplex.gui.RandomDialog;
import com.tkb.simplex.gui.StatusBar;

/**
 * An action to create random linear problems.
 *
 * @author Akis Papadopoulos
 */
public class RandomAction extends Action {

    private JFrame parent;

    private JTextArea output, problemOutput;

    private StatusBar status;

    public RandomAction(JFrame parent, JTextArea output, JTextArea problemOutput, StatusBar status) {
        super("random");

        this.parent = parent;
        this.output = output;
        this.problemOutput = problemOutput;
        this.status = status;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        RandomDialog dialog = new RandomDialog(parent, true);

        LinearProblem randomproblem = dialog.call();

        if (randomproblem != null) {
            Clipboard.setClipboard(randomproblem);

            output.setText(randomproblem.toString());
            output.append("\n" + actions.getString("proccess.completed"));

            problemOutput.setText(randomproblem.toString());

            JLabel label = status.getLabel("status.messager");

            if (label != null) {
                label.setText(actions.getString("randomization.completed"));
            }

            label = status.getLabel("status.clipboard");

            if (label != null) {
                label.setEnabled(true);
            }
        } else {
            JLabel label = status.getLabel("status.messager");

            if (label != null) {
                label.setText(actions.getString("randomization.canceled"));
            }
        }
    }
}
