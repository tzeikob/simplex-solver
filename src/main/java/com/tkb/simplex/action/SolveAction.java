package com.tkb.simplex.action;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import com.tkb.simplex.math.LinearProblem;
import com.tkb.simplex.math.AbstractSolver;
import com.tkb.simplex.math.Basic2DSolver;
import com.tkb.simplex.math.Basic3DSolver;
import com.tkb.simplex.model.AbstractObject;
import com.tkb.simplex.model.Animation;
import com.tkb.simplex.gui.Canvas;
import com.tkb.simplex.util.Clipboard;
import com.tkb.simplex.gui.StatusBar;

/**
 * An action to solve a linear problem.
 *
 * @author Akis Papadopoulos
 */
public class SolveAction extends Action {

    private Canvas canvas;

    private JTextArea output, solutionOutput;

    private StatusBar status;

    public SolveAction(Canvas canvas, JTextArea output, JTextArea solutionOutput, StatusBar status) {
        super("solve");

        this.canvas = canvas;
        this.output = output;
        this.solutionOutput = solutionOutput;
        this.status = status;

        KeyStroke accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.CTRL_MASK);
        putValue(ACCELERATOR_KEY, accelerator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object data = Clipboard.getClipboard();

        if (data != null && data instanceof LinearProblem) {
            LinearProblem problem = (LinearProblem) data;

            AbstractSolver solver;

            if (problem.size()[1] == 2) {
                double minmax = problem.minmax();

                double[][] A = problem.A();
                double[][] c = problem.c();
                double[][] b = problem.b();

                double[][] eqin = problem.eqin();

                solver = new Basic2DSolver(minmax, A, c, b, eqin);

                solver.solve();

                output.setText(solver.toString());
                output.append("\n" + actions.getString("proccess.completed"));

                solutionOutput.setText(solver.toString());

                AbstractObject scene = canvas.getScene();

                if (scene != null && scene instanceof Animation) {
                    Animation animation = (Animation) scene;
                    animation.dispose();
                    canvas.setScene(null);
                }

                canvas.setScene(solver.build());

                JLabel label = status.getLabel("status.messager");

                if (label != null) {
                    label.setText(actions.getString("solve.completed"));
                }

                label = status.getLabel("status.animation");

                if (label != null) {
                    label.setEnabled(true);
                    label.setText(actions.getString("animation.ready"));
                }
            } else if (problem.size()[1] == 3) {
                double minmax = problem.minmax();

                double[][] A = problem.A();
                double[][] c = problem.c();
                double[][] b = problem.b();

                double[][] eqin = problem.eqin();

                solver = new Basic3DSolver(minmax, A, c, b, eqin);

                solver.solve();

                output.setText(solver.toString());
                output.append("\n" + actions.getString("proccess.completed"));

                solutionOutput.setText(solver.toString());

                AbstractObject scene = canvas.getScene();

                if (scene != null && scene instanceof Animation) {
                    Animation animation = (Animation) scene;
                    animation.dispose();
                    canvas.setScene(null);
                }

                canvas.setScene(solver.build());

                JLabel label = status.getLabel("status.messager");

                if (label != null) {
                    label.setText(actions.getString("solve.completed"));
                }

                label = status.getLabel("status.animation");

                if (label != null) {
                    label.setEnabled(true);
                    label.setText(actions.getString("animation.ready"));
                }
            } else {
                JLabel label = status.getLabel("status.messager");

                if (label != null) {
                    label.setText(actions.getString("solve.canceled"));
                }
            }
        }
    }
}
