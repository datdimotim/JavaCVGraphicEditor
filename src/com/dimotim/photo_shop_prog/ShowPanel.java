package com.dimotim.photo_shop_prog;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ShowPanel extends JButton {
    private BufferedImage image = null;
    private double scale=1;
    @Override
    public void paint(Graphics g) {
        g.clearRect(0,0,getWidth(),getHeight());
        if(image==null)return;
        double scale=getWidth()/(double)image.getWidth();
        if(scale*image.getHeight()>getHeight())scale=getHeight()/(double)image.getHeight();

        final int w=(int)(scale*image.getWidth());
        final int h= (int)(scale*image.getHeight());
        g.drawImage(image,
                (getWidth()-w)/2,(getHeight()-h)/2,(getWidth()-w)/2+w,(getHeight()-h)/2+h,
                0,0,image.getWidth(),image.getHeight(),
                null);
        g.dispose();
    }

    public void setImage(BufferedImage image){
        this.image=image;
        updateUI();
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setScale(double scale){
        this.scale=scale;
        Dimension s=getParent().getSize();
        s.height*=scale;
        s.width*=scale;
        if(s.height>s.width/(double)image.getWidth()*image.getHeight())
            s.height=(int) (s.width/(double)image.getWidth()*image.getHeight());
        if(s.width>s.height/(double)image.getHeight()*image.getWidth())
            s.width=(int)(s.height/(double)image.getHeight()*image.getWidth());
        setPreferredSize(s);
        updateUI();
    }

    public double getScale(){
        return scale;
    }
}
