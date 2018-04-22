package com.diana.photo_shop;

import javax.swing.*;
import java.awt.event.*;

public class ScaleDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSlider scaleSlider;

    public ScaleDialog(DisplayPanel displayPanel){
        this();
        double initScale= displayPanel.getScale();
        scaleSlider.setValue((int) (displayPanel.getScale()*100));
        scaleSlider.addChangeListener(e-> displayPanel.setScale(scaleSlider.getValue()/(double)100));
        buttonCancel.addActionListener(e-> displayPanel.setScale(initScale));
        pack();
        setVisible(true);
    }

    public ScaleDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        contentPane.registerKeyboardAction(e -> onCancel(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        dispose();
    }

    private void onCancel() {
        dispose();
    }

    public static void main(String[] args) {
        ScaleDialog dialog = new ScaleDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
