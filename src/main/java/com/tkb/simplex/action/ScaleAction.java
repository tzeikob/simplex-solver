package com.tkb.simplex.action;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import com.tkb.simplex.math.LinearProblem;
import com.tkb.simplex.util.Clipboard;
import com.tkb.simplex.gui.StatusBar;

/**
 * An action to scale a linear problem.
 *
 * @author Akis Papadopoulos
 */
public class ScaleAction extends Action {

    private JTextArea output, problemOutput;

    private StatusBar status;

    public ScaleAction(JTextArea output, JTextArea problemOutput, StatusBar status) {
        super("scale");

        this.output = output;
        this.problemOutput = problemOutput;
        this.status = status;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object data = Clipboard.getClipboard();

        if (data != null && data instanceof LinearProblem) {
            LinearProblem problem = (LinearProblem) data;
            problem.scale();

            Clipboard.setClipboard(problem);

            output.setText(problem.toString());
            output.append("\n" + actions.getString("proccess.completed"));

            problemOutput.setText(problem.toString());

            JLabel label = status.getLabel("status.messager");

            if (label != null) {
                label.setText(actions.getString("scaling.completed"));
            }
        }
    }
}
