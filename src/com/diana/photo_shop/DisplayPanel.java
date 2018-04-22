package com.diana.photo_shop;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DisplayPanel extends JButton {
    private BufferedImage bufferedImage = null;
    private double scale=1;
    @Override
    public void paint(Graphics g) {
        g.clearRect(0,0,getWidth(),getHeight());
        if(bufferedImage ==null)return;
        double scale=getWidth()/(double) bufferedImage.getWidth();
        if(scale* bufferedImage.getHeight()>getHeight())scale=getHeight()/(double) bufferedImage.getHeight();

        final int w=(int)(scale* bufferedImage.getWidth());
        final int h= (int)(scale* bufferedImage.getHeight());
        g.drawImage(bufferedImage,
                (getWidth()-w)/2,(getHeight()-h)/2,(getWidth()-w)/2+w,(getHeight()-h)/2+h,
                0,0, bufferedImage.getWidth(), bufferedImage.getHeight(),
                null);
        g.dispose();
    }

    public void setBufferedImage(BufferedImage bufferedImage){
        this.bufferedImage = bufferedImage;
        updateUI();
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setScale(double scale){
        this.scale=scale;
        Dimension s=getParent().getSize();
        s.height*=scale;
        s.width*=scale;
        if(s.height>s.width/(double) bufferedImage.getWidth()* bufferedImage.getHeight())
            s.height=(int) (s.width/(double) bufferedImage.getWidth()* bufferedImage.getHeight());
        if(s.width>s.height/(double) bufferedImage.getHeight()* bufferedImage.getWidth())
            s.width=(int)(s.height/(double) bufferedImage.getHeight()* bufferedImage.getWidth());
        setPreferredSize(s);
        updateUI();
    }

    public double getScale(){
        return scale;
    }
}
