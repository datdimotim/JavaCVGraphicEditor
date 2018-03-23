package com.dimotim.photo_shop_prog;

import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class PaleteDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JComboBox sizeComboBox;

    private ShowPanel showPanel;
    private BufferedImage initImage;

    public PaleteDialog(ShowPanel showPanel){
        this();
        this.showPanel=showPanel;
        this.initImage=showPanel.getImage();
        sizeComboBox.addItemListener(e -> {
            if(e.getStateChange()==ItemEvent.SELECTED) {
                showPanel.setImage(
                        CVEffects.palette(
                                initImage,
                                Integer.parseInt(
                                        e.getItem().toString()
                                )
                        )
                );
            }
        });
        showPanel.setImage(CVEffects.palette(initImage, 8));
        pack();
        setVisible(true);
    }

    public PaleteDialog() {
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
        PaleteDialog dialog = new PaleteDialog(new ShowPanel());
        System.exit(0);
    }
}
