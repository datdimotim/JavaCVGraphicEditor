package com.dimotim.photo_shop_prog;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class MainWindow extends JFrame{
    private JPanel rootPanel;
    private ShowPanel showPanel;
    private BufferedImage initImage;

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
            if(showPanel.getImage()==null)return;
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
            if(initImage==null)return;
            showPanel.setImage(initImage);
        });
        effectsMenu.add(revertItem);
        JMenuItem scaleItem=new JMenuItem("Масштаб");
        scaleItem.addActionListener(e -> {
            BufferedImage image=showPanel.getImage();
            if(image==null)return;
            new ScaleDialog(showPanel);
        });
        effectsMenu.add(scaleItem);
        JMenuItem blackWhiteItem=new JMenuItem("Оттенки серого");
        blackWhiteItem.addActionListener(e -> {
            BufferedImage image=showPanel.getImage();
            if(image==null)return;
            showPanel.setImage(CVEffects.gray(image));
        });
        effectsMenu.add(blackWhiteItem);
        JMenuItem paleteItem=new JMenuItem("Палитра");
        paleteItem.addActionListener(e -> {
            BufferedImage image=showPanel.getImage();
            if(image==null)return;
            new PaleteDialog(showPanel);
        });
        effectsMenu.add(paleteItem);
        JMenuItem pseudoColorItem=new JMenuItem("Псевдоцвет");
        pseudoColorItem.addActionListener(e->{
            BufferedImage image=showPanel.getImage();
            if(image==null)return;
            showPanel.setImage(CVEffects.pseudoColor(image));
        });
        effectsMenu.add(pseudoColorItem);
        JMenuItem brightnessItem=new JMenuItem("Яркость");
        brightnessItem.addActionListener(e -> {
            Image image=showPanel.getImage();
            if(image==null)return;
            new SliderDialog(showPanel,CVEffects::brightness,-200,200,0);
        });
        effectsMenu.add(brightnessItem);
        JMenuItem autoBrightnessItem=new JMenuItem("Автояркость");
        autoBrightnessItem.addActionListener(e -> {
            if(showPanel.getImage()==null)return;
            showPanel.setImage(CVEffects.autoBrightness(showPanel.getImage()));
        });
        effectsMenu.add(autoBrightnessItem);
        JMenuItem contrastItem=new JMenuItem("Контрастность");
        contrastItem.addActionListener(e -> {
            Image image=showPanel.getImage();
            if(image==null)return;
            new SliderDialog(showPanel,CVEffects::contrast,-50,300,0);
        });
        effectsMenu.add(contrastItem);
        JMenuItem blurItem=new JMenuItem("Размытость");
        blurItem.addActionListener(e->{
            BufferedImage image=showPanel.getImage();
            if(image==null)return;
            new SliderDialog(showPanel,CVEffects::blur,3,100,0);
        });
        effectsMenu.add(blurItem);
        JMenuItem cannyItem=new JMenuItem("Детектор краёв");
        cannyItem.addActionListener(e->{
            BufferedImage image=showPanel.getImage();
            if(image==null)return;
            new CannyDialog(showPanel);
        });
        effectsMenu.add(cannyItem);
        menuBar.add(effectsMenu);
        setJMenuBar(menuBar);
    }

    private void saveToFile(File file) {
        if(showPanel.getImage()==null)throw new RuntimeException("image doesn't exist");
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
    private void loadFromFile(File file){
        System.out.println(file.getAbsolutePath());
        ImageIcon image= new ImageIcon(file.getAbsolutePath());
        if(image.getImageLoadStatus()!=MediaTracker.COMPLETE){
            JOptionPane.showMessageDialog(this,"file load error");
            return;
        }
        showPanel.setImage(CVEffects.id(image.getImage()));
        initImage=showPanel.getImage();
        showPanel.setScale(1);
    }
}

