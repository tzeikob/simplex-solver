package com.tkb.simplex.gui;

import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;

/**
 * A split pane component.
 *
 * @author Akis Papadopoulos
 */
public class SplitPane extends JSplitPane {

    private JComponent a, b;

    public SplitPane(JComponent a, JComponent b, int orientation, int divider, double resize) {
        super(orientation, a, b);

        this.a = a;
        this.b = b;

        setDividerLocation(divider);
        setResizeWeight(resize);
        setContinuousLayout(true);
        setOneTouchExpandable(true);
        setDividerSize(10);
        setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
    }

    public void swap() {
        remove(a);
        remove(b);

        if (getOrientation() == 1) {
            setLeftComponent(b);
            setRightComponent(a);
        } else {
            setTopComponent(b);
            setBottomComponent(a);
        }

        JComponent temp;
        temp = a;
        a = b;
        b = temp;
    }
}
