package com.diana.photo_shop;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class CannyDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSlider treshold1;
    private JSlider treshold2;
    private JCheckBox l2GradientCheckBox;
    private JComboBox apertureSizeCombobox;
    private DisplayPanel displayPanel;
    private BufferedImage initImage;

    public CannyDialog(DisplayPanel displayPanel){
        this();
        this.displayPanel = displayPanel;
        this.initImage= displayPanel.getBufferedImage();
        apertureSizeCombobox.addItemListener(e->onChanged());
        treshold1.addChangeListener(e->onChanged());
        treshold2.addChangeListener(e->onChanged());
        apertureSizeCombobox.addItemListener(e->onChanged());
        l2GradientCheckBox.addActionListener(e->onChanged());
        onChanged();
        pack();
        setVisible(true);
    }

    private void onChanged(){
        displayPanel.setBufferedImage(
                OpenCVFilters.canny(
                        initImage,
                        treshold1.getValue(),
                        treshold2.getValue(),
                        Integer.parseInt(apertureSizeCombobox.getSelectedItem().toString()),
                        l2GradientCheckBox.isSelected()));
    }

    public CannyDialog() {
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
        displayPanel.setBufferedImage(initImage);
        dispose();
    }

    public static void main(String[] args) {
        CannyDialog dialog = new CannyDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
