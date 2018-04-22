package com.diana.photo_shop;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame{
    private JPanel rootPanel;
    private DisplayPanel displayPanel;
    private BufferedImage originImage;

    public MainWindow(){
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(rootPanel);
        setTitle("Graphic Editor");
        initMenu();
        pack();
        setMinimumSize(getSize());
        setVisible(true);
    }

    private void initMenu(){
        JMenuBar menuBar=new JMenuBar();
        JMenu menuFile=new JMenu("Файл");
        JMenuItem openItem=new JMenuItem("Открыть");
        openItem.addActionListener(e -> {
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
            loadFromFile(fc.getSelectedFile());
        });
        menuFile.add(openItem);
        JMenuItem saveItem=new JMenuItem("Сохранить");
        saveItem.addActionListener(e -> {
            if(displayPanel.getBufferedImage()==null)return;
            JFileChooser fc=new JFileChooser();
            fc.setDialogTitle("Save");
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
            saveToFile(new File(path));
        });
        menuFile.add(saveItem);
        menuBar.add(menuFile);

        JMenu effectsMenu=new JMenu("Эффекты");
        JMenuItem revertItem=new JMenuItem("Отменить все эффекты");
        revertItem.addActionListener(e -> {
            if(originImage ==null)return;
            displayPanel.setBufferedImage(originImage);
        });
        effectsMenu.add(revertItem);
        JMenuItem scaleItem=new JMenuItem("Масштаб");
        scaleItem.addActionListener(e -> {
            BufferedImage image= displayPanel.getBufferedImage();
            if(image==null)return;
            new ScaleDialog(displayPanel);
        });
        effectsMenu.add(scaleItem);
        JMenuItem blackWhiteItem=new JMenuItem("Оттенки серого");
        blackWhiteItem.addActionListener(e -> {
            BufferedImage image= displayPanel.getBufferedImage();
            if(image==null)return;
            displayPanel.setBufferedImage(OpenCVFilters.gray(image));
        });
        effectsMenu.add(blackWhiteItem);
        JMenuItem paleteItem=new JMenuItem("Палитра");
        paleteItem.addActionListener(e -> {
            BufferedImage image= displayPanel.getBufferedImage();
            if(image==null)return;
            new PaleteDialog(displayPanel);
        });
        effectsMenu.add(paleteItem);
        JMenuItem pseudoColorItem=new JMenuItem("Псевдоцвет");
        pseudoColorItem.addActionListener(e->{
            BufferedImage image= displayPanel.getBufferedImage();
            if(image==null)return;
            displayPanel.setBufferedImage(OpenCVFilters.pseudoColor(image));
        });
        effectsMenu.add(pseudoColorItem);
        JMenuItem brightnessItem=new JMenuItem("Яркость");
        brightnessItem.addActionListener(e -> {
            Image image= displayPanel.getBufferedImage();
            if(image==null)return;
            new SliderDialog(displayPanel, OpenCVFilters::brightness,-200,200,0);
        });
        effectsMenu.add(brightnessItem);
        JMenuItem negativeItem=new JMenuItem("Негатив");
        negativeItem.addActionListener(e -> {
            if(displayPanel.getBufferedImage()==null)return;
            displayPanel.setBufferedImage(OpenCVFilters.negative(displayPanel.getBufferedImage()));
        });
        effectsMenu.add(negativeItem);
        JMenuItem contrastItem=new JMenuItem("Контрастность");
        contrastItem.addActionListener(e -> {
            Image image= displayPanel.getBufferedImage();
            if(image==null)return;
            new SliderDialog(displayPanel, OpenCVFilters::contrast,-50,300,0);
        });
        effectsMenu.add(contrastItem);
        JMenuItem blurItem=new JMenuItem("Размытость");
        blurItem.addActionListener(e->{
            BufferedImage image= displayPanel.getBufferedImage();
            if(image==null)return;
            new SliderDialog(displayPanel, OpenCVFilters::blur,3,100,0);
        });
        effectsMenu.add(blurItem);
        JMenuItem cannyItem=new JMenuItem("Детектор краёв");
        cannyItem.addActionListener(e->{
            BufferedImage image= displayPanel.getBufferedImage();
            if(image==null)return;
            new CannyDialog(displayPanel);
        });
        effectsMenu.add(cannyItem);
        menuBar.add(effectsMenu);
        setJMenuBar(menuBar);
    }

    private void saveToFile(File file) {
        if(displayPanel.getBufferedImage()==null)throw new RuntimeException("image doesn't exist");
        if(file.exists()) {
            JOptionPane.showMessageDialog(this, "File is already exists!");
            return;
        }
        BufferedImage image= displayPanel.getBufferedImage();
        try {
            file.createNewFile();
            ImageIO.write(image, "png",file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Saving error!");
        }
    }
    private void loadFromFile(File file){
        System.out.println(file.getAbsolutePath());
        ImageIcon image= new ImageIcon(file.getAbsolutePath());
        if(image.getImageLoadStatus()!=MediaTracker.COMPLETE){
            JOptionPane.showMessageDialog(this,"file load error");
            return;
        }
        displayPanel.setBufferedImage(OpenCVFilters.id(image.getImage()));
        originImage = displayPanel.getBufferedImage();
        displayPanel.setScale(1);
    }
}