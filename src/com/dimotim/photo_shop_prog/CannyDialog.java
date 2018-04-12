package com.dimotim.photo_shop_prog;

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
    private ShowPanel showPanel;
    private BufferedImage initImage;

    public CannyDialog(ShowPanel showPanel){
        this();
        this.showPanel=showPanel;
        this.initImage=showPanel.getImage();
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
        showPanel.setImage(
                CVEffects.canny(
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

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        showPanel.setImage(initImage);
        dispose();
    }

    public static void main(String[] args) {
        CannyDialog dialog = new CannyDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
