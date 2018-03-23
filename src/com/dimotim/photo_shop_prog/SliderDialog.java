package com.dimotim.photo_shop_prog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class SliderDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSlider stepSlider;

    private ShowPanel showPanel;
    private BufferedImage initImage;
    private ImageProcessor imageProcessor;
    public interface ImageProcessor{
        BufferedImage process(BufferedImage image, int step);
    }
    public SliderDialog(ShowPanel showPanel, ImageProcessor imageProcessor, int minStep, int maxStep, int initStep){
        this();
        stepSlider.setMinimum(minStep);
        stepSlider.setMaximum(maxStep);
        stepSlider.setValue(initStep);

        this.showPanel=showPanel;
        this.imageProcessor=imageProcessor;
        initImage=showPanel.getImage();
        setListeners();
        pack();
        setVisible(true);
    }

    private void setListeners(){
        stepSlider.addChangeListener(e -> showPanel.setImage(imageProcessor.process(initImage, stepSlider.getValue())));
    }

    public SliderDialog() {
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
        dispose();
    }

    private void onCancel() {
        showPanel.setImage(initImage);
        dispose();
    }

    public static void main(String[] args) {
        SliderDialog dialog = new SliderDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
