package com.dimotim.photo_shop_prog;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ShowPanel extends JButton {
    private BufferedImage image = null;
    @Override
    public void paint(Graphics g) {
        g.clearRect(0,0,getWidth(),getHeight());
        if(image==null)return;
        g.drawImage(image,
                0,0,image.getWidth(null),image.getHeight(null),
                0,0,image.getWidth(null),image.getHeight(null),
                null);
        g.dispose();
    }
    public void setImage(BufferedImage image){

        this.image=image;
        setPreferredSize(new Dimension(image.getWidth(null),image.getHeight(null)));
        updateUI();
    }

    public BufferedImage getImage() {
        return image;
    }
}
