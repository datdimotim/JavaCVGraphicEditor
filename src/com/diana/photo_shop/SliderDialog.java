package com.diana.photo_shop;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class SliderDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JSlider stepSlider;

    private DisplayPanel displayPanel;
    private BufferedImage initImage;
    private ImageProcessor imageProcessor;
    public interface ImageProcessor{
        BufferedImage process(BufferedImage image, int step);
    }
    public SliderDialog(DisplayPanel displayPanel, ImageProcessor imageProcessor, int minStep, int maxStep, int initStep){
        this();
        stepSlider.setMinimum(minStep);
        stepSlider.setMaximum(maxStep);
        stepSlider.setValue(initStep);

        this.displayPanel = displayPanel;
        this.imageProcessor=imageProcessor;
        initImage= displayPanel.getBufferedImage();
        setListeners();
        pack();
        setVisible(true);
    }

    private void setListeners(){
        stepSlider.addChangeListener(e -> displayPanel.setBufferedImage(imageProcessor.process(initImage, stepSlider.getValue())));
    }

    public SliderDialog() {
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
        SliderDialog dialog = new SliderDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
