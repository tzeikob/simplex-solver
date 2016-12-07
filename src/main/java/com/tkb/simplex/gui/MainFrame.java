package com.tkb.simplex.gui;

import com.tkb.simplex.action.SolveAction;
import com.tkb.simplex.action.SaveAsAction;
import com.tkb.simplex.action.ScaleAction;
import com.tkb.simplex.action.SaveAllAction;
import com.tkb.simplex.action.RepeatAction;
import com.tkb.simplex.action.RefreshAction;
import com.tkb.simplex.action.RecordAction;
import com.tkb.simplex.action.RandomAction;
import com.tkb.simplex.action.SaveAction;
import com.tkb.simplex.action.PerspectiveViewAction;
import com.tkb.simplex.action.OrthographicViewAction;
import com.tkb.simplex.action.PlayAction;
import com.tkb.simplex.action.PauseAction;
import com.tkb.simplex.action.HelpAction;
import com.tkb.simplex.action.OptimalAction;
import com.tkb.simplex.action.OpenAction;
import com.tkb.simplex.action.NewAction;
import com.tkb.simplex.action.StopAction;
import com.tkb.simplex.action.ZoomOutAction;
import com.tkb.simplex.action.ZoomInAction;
import com.tkb.simplex.action.GridOnAction;
import com.tkb.simplex.action.ParseAction;
import com.tkb.simplex.action.ForwardAction;
import com.tkb.simplex.action.DualAction;
import com.tkb.simplex.action.CloseAllAction;
import com.tkb.simplex.action.CloseAction;
import com.tkb.simplex.action.BackwardAction;
import com.tkb.simplex.action.CaptureAction;
import com.tkb.simplex.action.AxesOnAction;
import com.tkb.simplex.action.Action;
import com.tkb.simplex.action.AboutAction;
import com.tkb.simplex.action.ExportExcelAction;
import com.tkb.simplex.action.ImportExcelAction;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JToolBar;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.JFormattedTextField;
import javax.swing.ImageIcon;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import java.awt.Component;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.io.Writer;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.MissingResourceException;
import java.util.Locale;
import com.tkb.simplex.model.Animation;
import org.apache.log4j.Logger;

/**
 * A main frame component.
 *
 * @author Akis Papadopoulos
 */
public class MainFrame extends JFrame implements WindowListener, MouseListener, KeyListener, ChangeListener {

    private static final Logger logger = Logger.getLogger(MainFrame.class);

    private static ResourceBundle menu;

    private static ResourceBundle dialogs;

    private static ResourceBundle vocab;

    private static Font font = new Font("Sylfaen", Font.PLAIN, 15);

    private JPanel container;

    private Desktop desktop;

    private Canvas canvas;

    private JTextArea output, problemOutput, solutionOutput;

    private SplitPane innersplitter;

    private SplitPane outersplitter;

    private StatusBar status;

    private JSpinner delayspinner;

    private Action newaction;

    private Action openaction;

    private Action closeaction;

    private Action closeallaction;

    private Action saveaction;

    private Action saveasaction;

    private Action saveallaction;

    private Action importaction;

    private Action exportaction;

    private Action parseaction;

    private Action randomaction;

    private Action optimalaction;

    private Action scaleaction;

    private Action dualaction;

    private Action solveaction;

    private Action playaction;

    private Action pauseaction;

    private Action stopaction;

    private Action forwardaction;

    private Action backwardaction;

    private Action repeataction;

    private Action captureaction;

    private Action recordaction;

    private Action orthoaction;

    private Action perspeaction;

    private Action axesonaction;

    private Action gridonaction;

    private Action zoominaction;

    private Action zoomoutaction;

    private Action refreshaction;

    private Action aboutaction;

    private Action helpaction;

    static {
        try {
            menu = ResourceBundle.getBundle("bundle/menu", Locale.getDefault());
            dialogs = ResourceBundle.getBundle("bundle/dialogs", Locale.getDefault());
            vocab = ResourceBundle.getBundle("bundle/vocab", Locale.getDefault());
        } catch (MissingResourceException exc) {
            logger.error("Missing resource bundle file", exc);
        }
    }

    public MainFrame(String title, Dimension size) {
        super(title);

        JPanel container = new JPanel(new BorderLayout(2, 2));
        container.setOpaque(true);
        setContentPane(container);

        status = createStatusBar();
        container.add(status, BorderLayout.SOUTH);

        desktop = new Desktop();
        JScrollPane dscrollpane = new JScrollPane();
        dscrollpane.getViewport().add(desktop);

        canvas = new Canvas(status);

        innersplitter = new SplitPane(dscrollpane, canvas, SplitPane.HORIZONTAL_SPLIT, 800, 0.8);

        JTabbedPane tabbedpane = new JTabbedPane();

        output = new JTextArea();
        output.setFont(font);
        output.setEditable(false);
        tabbedpane.add(vocab.getString("output"), new JScrollPane(output));

        problemOutput = new JTextArea();
        problemOutput.setFont(font);
        problemOutput.setEditable(false);
        tabbedpane.add(vocab.getString("problem"), new JScrollPane(problemOutput));

        solutionOutput = new JTextArea();
        solutionOutput.setFont(font);
        solutionOutput.setEditable(false);
        tabbedpane.add(vocab.getString("solution"), new JScrollPane(solutionOutput));

        outersplitter = new SplitPane(innersplitter, tabbedpane, SplitPane.VERTICAL_SPLIT, 500, 0.8);

        container.add(outersplitter, BorderLayout.CENTER);

        newaction = new NewAction(desktop);
        openaction = new OpenAction(desktop);
        closeaction = new CloseAction(desktop);
        closeallaction = new CloseAllAction(desktop);
        saveaction = new SaveAction(desktop);
        saveasaction = new SaveAsAction(desktop);
        saveallaction = new SaveAllAction(desktop);
        importaction = new ImportExcelAction(container, output, problemOutput, status);
        exportaction = new ExportExcelAction(container);

        parseaction = new ParseAction(desktop, output, problemOutput, status);
        randomaction = new RandomAction(this, output, problemOutput, status);
        optimalaction = new OptimalAction(this, output, problemOutput, status);
        scaleaction = new ScaleAction(output, problemOutput, status);
        dualaction = new DualAction(output, status);
        solveaction = new SolveAction(canvas, output, solutionOutput, status);

        playaction = new PlayAction(canvas, status);
        pauseaction = new PauseAction(canvas, status);
        stopaction = new StopAction(canvas, status);
        forwardaction = new ForwardAction(canvas);
        backwardaction = new BackwardAction(canvas);
        repeataction = new RepeatAction();
        captureaction = new CaptureAction(canvas, status);
        recordaction = new RecordAction(canvas, status);

        orthoaction = new OrthographicViewAction(canvas, status);
        perspeaction = new PerspectiveViewAction(canvas, status);
        axesonaction = new AxesOnAction(canvas);
        gridonaction = new GridOnAction(canvas);
        zoominaction = new ZoomInAction(canvas);
        zoomoutaction = new ZoomOutAction(canvas);
        refreshaction = new RefreshAction(canvas);

        aboutaction = new AboutAction(this);
        helpaction = new HelpAction();

        setJMenuBar(createMenuBar());

        JPanel toolbars = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolbars.add(createShortcutsToolBar());
        toolbars.add(createAnimationToolBar());
        container.add(toolbars, BorderLayout.NORTH);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        getLayeredPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        pack();
        setBounds(0, 0, size.width, size.height);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        addWindowListener(this);
        addKeyListener(this);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menubar = new JMenuBar();

        JMenu filemenu = new JMenu(menu.getString("file.name"));
        filemenu.setMnemonic(menu.getString("file.mnemonic").charAt(0));
        menubar.add(filemenu);

        JMenuItem item = new JMenuItem(newaction);
        item.setToolTipText(null);
        filemenu.add(item);

        item = new JMenuItem(openaction);
        item.setToolTipText(null);
        filemenu.add(item);

        item = new JMenuItem(closeaction);
        item.setToolTipText(null);
        filemenu.add(item);

        item = new JMenuItem(closeallaction);
        item.setToolTipText(null);
        filemenu.add(item);

        filemenu.addSeparator();
        item = new JMenuItem(saveaction);
        item.setToolTipText(null);
        filemenu.add(item);

        item = new JMenuItem(saveasaction);
        item.setToolTipText(null);
        filemenu.add(item);

        item = new JMenuItem(saveallaction);
        item.setToolTipText(null);
        filemenu.add(item);

        filemenu.addSeparator();
        item = new JMenuItem(importaction);
        item.setToolTipText(null);
        filemenu.add(item);

        item = new JMenuItem(exportaction);
        item.setToolTipText(null);
        filemenu.add(item);

        filemenu.addSeparator();
        item = new JMenuItem(menu.getString("exit.title"));
        item.setMnemonic(menu.getString("exit.mnemonic").charAt(0));
        item.setIcon(new ImageIcon(this.getClass().getResource("/images/" + menu.getString("exit.icon"))));
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });
        filemenu.add(item);

        JMenu buildmenu = new JMenu(menu.getString("build.name"));
        buildmenu.setMnemonic(menu.getString("build.mnemonic").charAt(0));
        menubar.add(buildmenu);

        item = new JMenuItem(parseaction);
        item.setToolTipText(null);
        buildmenu.add(item);

        item = new JMenuItem(randomaction);
        item.setToolTipText(null);
        buildmenu.add(item);

        item = new JMenuItem(optimalaction);
        item.setToolTipText(null);
        buildmenu.add(item);

        buildmenu.addSeparator();
        item = new JMenuItem(scaleaction);
        item.setToolTipText(null);
        buildmenu.add(item);

        item = new JMenuItem(dualaction);
        item.setToolTipText(null);
        buildmenu.add(item);

        buildmenu.addSeparator();
        item = new JMenuItem(solveaction);
        item.setToolTipText(null);
        buildmenu.add(item);

        JMenu animationmenu = new JMenu(menu.getString("animation.name"));
        animationmenu.setMnemonic(menu.getString("animation.mnemonic").charAt(0));
        menubar.add(animationmenu);

        item = new JMenuItem(playaction);
        item.setToolTipText(null);
        animationmenu.add(item);

        item = new JMenuItem(pauseaction);
        item.setToolTipText(null);
        animationmenu.add(item);

        item = new JMenuItem(stopaction);
        item.setToolTipText(null);
        animationmenu.add(item);

        animationmenu.addSeparator();
        item = new JMenuItem(backwardaction);
        item.setToolTipText(null);
        animationmenu.add(item);

        item = new JMenuItem(forwardaction);
        item.setToolTipText(null);
        animationmenu.add(item);

        animationmenu.addSeparator();
        item = new JMenuItem(repeataction);
        item.setToolTipText(null);
        animationmenu.add(item);

        animationmenu.addSeparator();
        item = new JMenuItem(captureaction);
        item.setToolTipText(null);
        animationmenu.add(item);

        animationmenu.addSeparator();
        item = new JMenuItem(recordaction);
        item.setToolTipText(null);
        animationmenu.add(item);

        JMenu viewportmenu = new JMenu(menu.getString("viewport.name"));
        viewportmenu.setMnemonic(menu.getString("viewport.mnemonic").charAt(0));
        menubar.add(viewportmenu);

        item = new JMenuItem(orthoaction);
        item.setToolTipText(null);
        viewportmenu.add(item);

        item = new JMenuItem(perspeaction);
        item.setToolTipText(null);
        viewportmenu.add(item);

        viewportmenu.addSeparator();
        item = new JMenuItem(zoominaction);
        item.setToolTipText(null);
        viewportmenu.add(item);

        item = new JMenuItem(zoomoutaction);
        item.setToolTipText(null);
        viewportmenu.add(item);

        viewportmenu.addSeparator();
        JMenu showmenu = new JMenu(menu.getString("show.name"));
        showmenu.setMnemonic(menu.getString("show.mnemonic").charAt(0));
        showmenu.setIcon(new ImageIcon(this.getClass().getResource("/images/" + menu.getString("show.icon"))));
        viewportmenu.add(showmenu);

        JCheckBoxMenuItem cbitem = new JCheckBoxMenuItem(axesonaction);
        cbitem.setSelected(true);
        cbitem.setToolTipText(null);
        showmenu.add(cbitem);

        cbitem = new JCheckBoxMenuItem(gridonaction);
        cbitem.setSelected(true);
        cbitem.setToolTipText(null);
        showmenu.add(cbitem);

        viewportmenu.addSeparator();
        item = new JMenuItem(refreshaction);
        item.setToolTipText(null);
        viewportmenu.add(item);

        WindowMenu windowmenu = new WindowMenu(desktop);
        menubar.add(windowmenu);

        JMenu helpmenu = new JMenu(menu.getString("help.name"));
        helpmenu.setMnemonic(menu.getString("help.mnemonic").charAt(0));
        menubar.add(helpmenu);

        item = new JMenuItem(helpaction);
        item.setToolTipText(null);
        helpmenu.add(item);

        item = new JMenuItem(aboutaction);
        item.setToolTipText(null);
        helpmenu.add(item);

        return menubar;
    }

    private JToolBar createShortcutsToolBar() {
        JToolBar toolbar = new JToolBar(vocab.getString("shortcuts"), JToolBar.HORIZONTAL);
        toolbar.setFloatable(true);
        toolbar.setRollover(true);

        JButton button = new JButton(newaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(openaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(saveaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(saveallaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(parseaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(solveaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(zoominaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(zoomoutaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(refreshaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(captureaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        return toolbar;
    }

    private JToolBar createAnimationToolBar() {
        JToolBar toolbar = new JToolBar(vocab.getString("animation"), JToolBar.HORIZONTAL);
        toolbar.setFloatable(true);
        toolbar.setRollover(true);

        JButton button = new JButton(backwardaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(playaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(recordaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(forwardaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(pauseaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(stopaction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        button = new JButton(repeataction);
        button.setRolloverEnabled(true);
        button.setFocusable(false);
        button.setText(null);
        button.addMouseListener(this);
        toolbar.add(button);

        SpinnerNumberModel model = new SpinnerNumberModel(1.5, 0.2, 10.0, 0.1);
        delayspinner = new JSpinner(model);
        JSpinner.NumberEditor numbereditor = new JSpinner.NumberEditor(delayspinner, "#.# sec");
        delayspinner.setEditor(numbereditor);
        JFormattedTextField editor = numbereditor.getTextField();
        editor.setColumns(5);
        editor.setHorizontalAlignment(JFormattedTextField.RIGHT);
        editor.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 3));
        editor.setFocusable(false);
        editor.setFont(editor.getFont().deriveFont(Font.PLAIN));

        delayspinner.setBorder(BorderFactory.createLineBorder(Color.gray));
        delayspinner.setToolTipText(vocab.getString("delay"));

        for (int i = 0; i < delayspinner.getComponentCount(); i++) {
            Component component = delayspinner.getComponent(i);

            if (component instanceof JButton) {
                JButton b = (JButton) component;
                b.setBorder(BorderFactory.createLineBorder(Color.gray));

                if (i == 0) {
                    b.setToolTipText(vocab.getString("increase"));
                } else if (i == 1) {
                    b.setToolTipText(vocab.getString("decrease"));
                }
            }
        }

        JPanel panel = new JPanel();
        panel.add(delayspinner);
        delayspinner.addChangeListener(this);
        toolbar.add(panel);

        return toolbar;
    }

    private StatusBar createStatusBar() {
        StatusBar statusbar = new StatusBar();
        statusbar.addLabel("status.messager", null, null, JLabel.LEFT, 0.72, true);
        statusbar.addLabel("status.clipboard", vocab.getString("clipboard"), null, JLabel.CENTER, 0.09, false);
        statusbar.addLabel("status.animation", vocab.getString("animation"), null, JLabel.CENTER, 0.11, false);
        statusbar.addLabel("status.viewport", vocab.getString("perspective"), null, JLabel.CENTER, 0.08, true);

        return statusbar;
    }

    private void exit() {
        if (desktop.getAllFrames().length > 0) {
            String title = dialogs.getString("exit.and.save.application.title");
            String message = dialogs.getString("exit.and.save.application.query");

            int answer = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (answer == JOptionPane.YES_OPTION) {
                JInternalFrame[] frames = desktop.getAllFrames();

                for (int i = 0; i < frames.length; i++) {
                    Editor editor = (Editor) frames[i];
                    JComponent parent = editor;

                    if (editor.isIcon()) {
                        parent = desktop;
                    }

                    File file = editor.getFile();

                    try {
                        String text = editor.getText();
                        char[] buffer = text.toCharArray();

                        Writer output = new FileWriter(file);
                        output.write(buffer, 0, buffer.length);

                        output.flush();
                        output.close();
                    } catch (IOException exc) {
                        title = dialogs.getString("saving.document.title");
                        message = dialogs.getString("ioexception.message").replaceFirst("#", file.getName());

                        JOptionPane.showInternalMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
                        
                        logger.error(message, exc);
                    } catch (Exception exc) {
                        title = dialogs.getString("saving.document.title");
                        message = dialogs.getString("exception.message");

                        JOptionPane.showInternalMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
                        
                        logger.error(message, exc);
                    }
                }

                System.exit(0);
            } else if (answer == JOptionPane.NO_OPTION) {
                System.exit(0);
            }
        } else {
            String title = dialogs.getString("exit.application.title");
            String message = dialogs.getString("exit.application.query");

            int answer = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (answer == JOptionPane.OK_OPTION) {
                System.exit(0);
            }
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        exit();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        Object object = e.getSource();

        if (object instanceof JButton) {
            String tooltip = ((JButton) object).getToolTipText();

            JLabel label = status.getLabel("status.messager");

            if (label != null) {
                label.setText(tooltip);
            }
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        Object object = e.getSource();

        if (object instanceof JButton) {
            JLabel label = status.getLabel("status.messager");

            if (label != null) {
                label.setText(null);
            }
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
        requestFocusInWindow();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CAPS_LOCK) {
            boolean enabled = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);

            JLabel label = status.getLabel("status.capslock");

            if (label != null) {
                label.setEnabled(enabled);
            }
        } else if (e.getKeyCode() == KeyEvent.VK_NUM_LOCK) {
            boolean enabled = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_NUM_LOCK);

            JLabel label = status.getLabel("status.numlock");

            if (label != null) {
                label.setEnabled(enabled);
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        SpinnerModel model = delayspinner.getModel();

        if (model instanceof SpinnerNumberModel) {
            SpinnerNumberModel nmodel = (SpinnerNumberModel) model;
            double delay = nmodel.getNumber().doubleValue();

            Animation.setDelay(delay);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
