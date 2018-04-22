package com.diana.photo_shop;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class PaleteDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox sizeComboBox;

    private DisplayPanel displayPanel;
    private BufferedImage initImage;

    public PaleteDialog(DisplayPanel displayPanel){
        this();
        this.displayPanel = displayPanel;
        this.initImage= displayPanel.getBufferedImage();
        sizeComboBox.addItemListener(e -> {
            System.out.println("palete changed: "+e.getItem());
            if(e.getStateChange()==ItemEvent.SELECTED) {
                displayPanel.setBufferedImage(
                        OpenCVFilters.palette(
                                initImage,
                                Integer.parseInt(
                                        e.getItem().toString()
                                )
                        )
                );
            }
        });
        displayPanel.setBufferedImage(OpenCVFilters.palette(initImage, 8));
        pack();
        setVisible(true);
    }

    public PaleteDialog() {
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
        PaleteDialog dialog = new PaleteDialog(new DisplayPanel());
        System.exit(0);
    }
}
