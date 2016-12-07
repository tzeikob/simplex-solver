package com.tkb.simplex.action;

import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import com.tkb.simplex.gui.Canvas;
import com.tkb.simplex.gui.StatusBar;

/**
 * An action to capture the animation into GIF file.
 *
 * @author Akis Papadopoulos
 */
public class RecordAction extends Action {

    private Canvas canvas;

    private StatusBar status;

    public RecordAction(Canvas canvas, StatusBar status) {
        super("record");

        this.canvas = canvas;
        this.status = status;

        KeyStroke accelerator = KeyStroke.getKeyStroke(KeyEvent.VK_F9, ActionEvent.CTRL_MASK);
        putValue(ACCELERATOR_KEY, accelerator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (canvas.isRecording()) {
            canvas.record(false);

            putValue(SMALL_ICON, new ImageIcon(this.getClass().getResource("/images/recordoff.png")));
            putValue(SHORT_DESCRIPTION, menu.getString("record.tooltip"));

            JLabel label = status.getLabel("status.messager");

            if (label != null) {
                label.setText(actions.getString("recording.stopped"));
            }
        } else {
            canvas.record(true);

            putValue(SMALL_ICON, new ImageIcon(this.getClass().getResource("/images/recordon.png")));
            putValue(SHORT_DESCRIPTION, menu.getString("record.on.tooltip"));

            JLabel label = status.getLabel("status.messager");

            if (label != null) {
                label.setText(actions.getString("recording.started"));
            }
        }
    }
}
