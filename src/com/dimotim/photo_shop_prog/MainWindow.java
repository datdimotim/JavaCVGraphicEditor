package com.dimotim.photo_shop_prog;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class MainWindow extends JFrame{
    private JPanel rootPanel;
    private JButton loadButton;
    private JButton saveButton;
    private JButton revertButton;
    private ShowPanel showPanel;
    private JButton grayButton;
    private JButton contrastButton;
    private JButton scaleButton;
    private JButton brightnessButton;
    private JButton autoBrightnessButton;
    private JButton palleteButton;
    private BufferedImage initImage;

    public MainWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setListeners();
        setContentPane(rootPanel);
        pack();
        setResizable(false);
        setVisible(true);
    }
    private void setListeners(){
        loadButton.addActionListener(e -> {
            JFileChooser fc=new JFileChooser();
            fc.setMultiSelectionEnabled(false);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
            fc.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".png")||f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return ".png";
                }
            });
            fc.showOpenDialog(this);
            if(fc.getSelectedFile()==null)return;
            onFileToLoadSelected(fc.getSelectedFile());
        });

        saveButton.addActionListener(e -> {
            if(showPanel.getImage()==null)return;
            JFileChooser fc=new JFileChooser();
            fc.setMultiSelectionEnabled(false);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fc.setApproveButtonText("save");
            fc.removeChoosableFileFilter(fc.getChoosableFileFilters()[0]);
            fc.addChoosableFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".png")||f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return ".png";
                }
            });
            fc.showOpenDialog(this);
            if(fc.getSelectedFile()==null)return;
            String path=fc.getSelectedFile().getAbsolutePath();
            if(!path.toLowerCase().endsWith(".png"))path+=".png";
            onFileToSaveSelected(new File(path));
        });

        scaleButton.addActionListener(e -> {
            BufferedImage image=showPanel.getImage();
            if(image==null)return;
            String ratioStr= JOptionPane.showInputDialog(null, "set ratio:");
            if(ratioStr==null)return;
            double ratio;
            try { ratio = Double.parseDouble(ratioStr); }
            catch (NumberFormatException ex){
                JOptionPane.showMessageDialog(this,"input error");
                return;
            }
            int max= Math.max(image.getHeight(null), image.getWidth(null));
            if(ratio*max<=5||ratio*max>5000){
                JOptionPane.showMessageDialog(this,"image will too large or too small");
                return;
            }
            if(showPanel.getImage()==null)return;
            showPanel.setImage(CVEffects.resize(image,ratio));
        });
        grayButton.addActionListener(e -> {
            BufferedImage image=showPanel.getImage();
            if(image==null)return;
            showPanel.setImage(CVEffects.gray(image));
        });

        palleteButton.addActionListener(e -> {
            BufferedImage image=showPanel.getImage();
            if(image==null)return;
            new PaleteDialog(showPanel);
        });

        contrastButton.addActionListener(e -> {
            Image image=showPanel.getImage();
            if(image==null)return;
            new SliderDialog(showPanel,CVEffects::contrast,-50,300,0);
        });

        brightnessButton.addActionListener(e -> {
            Image image=showPanel.getImage();
            if(image==null)return;
            new SliderDialog(showPanel,CVEffects::brightness,-200,200,0);
        });

        autoBrightnessButton.addActionListener(e -> {
            if(showPanel.getImage()==null)return;
            showPanel.setImage(CVEffects.autoBrightness(showPanel.getImage()));
        });

        revertButton.addActionListener(e -> {
            showPanel.setImage(initImage);
        });
    }
    private void onFileToSaveSelected(File file) {
        if(showPanel.getImage()==null)return;
        if(file.exists()) {
            JOptionPane.showMessageDialog(this, "File is already exists!");
            return;
        }
        BufferedImage image=showPanel.getImage();
        try {
            file.createNewFile();
            ImageIO.write(image, "png",file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Saving error!");
        }
    }
    private void onFileToLoadSelected(File file){
        System.out.println(file.getAbsolutePath());
        ImageIcon image= new ImageIcon(file.getAbsolutePath());
        if(image.getImageLoadStatus()!=MediaTracker.COMPLETE){
            JOptionPane.showMessageDialog(this,"file load error");
            return;
        }
        showPanel.setImage(CVEffects.id(image.getImage()));
        initImage=showPanel.getImage();
    }

}

