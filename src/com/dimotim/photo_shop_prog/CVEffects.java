package com.dimotim.photo_shop_prog;

import org.opencv.core.*;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;
import sun.misc.Cache;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import static org.opencv.core.CvType.*;
import static org.opencv.imgproc.Imgproc.*;

public class CVEffects {
    public static BufferedImage resize(BufferedImage image, double scale){
        Mat in=imageToMat(image);
        System.out.println(in.cols()+" "+in.rows());
        Mat out=new Mat((int)(in.rows()*scale),(int)(in.cols()*scale),CvType.CV_8UC3);
        Imgproc.resize(in,out,new Size((int)(in.cols()*scale),(int)(in.rows()*scale)));
        in.release();
        return matToImage(out);
    }

    public static BufferedImage canny(BufferedImage image, int treshold1, int treshold2, int apertureSize, boolean l2Gradient){
        Mat rgbImage=imageToMat(image);
        Mat edges=new Mat(image.getWidth(null),image.getHeight(null),CvType.CV_8UC3);
        Imgproc.Canny(rgbImage,edges,treshold1,treshold2,apertureSize,l2Gradient);
        return matToImage(edges);
    }

    private static Mat getGammaExpo(int step){
        Mat lut=new Mat(1, 256, CV_8UC1);
        for(int i=0;i<256;i++) {
            //double d = i+Math.sin(i*0.01255)*step*10;
            double d=i+step;
            if(d<0)d=0;
            if(d>255)d=255;
            lut.put(0,i,d);
        }
        return lut;
    }
    static public BufferedImage brightness(BufferedImage image,int step){
        Mat rgbImage=imageToMat(image);
        Mat hsvImage=new Mat();
        cvtColor(rgbImage,hsvImage,COLOR_RGB2HSV_FULL);
        ArrayList<Mat> channels=new ArrayList<>();
        channels.add(new Mat(image.getWidth(null),image.getHeight(null),CV_8UC1));
        channels.add(new Mat(image.getWidth(null),image.getHeight(null),CV_8UC1));
        channels.add(new Mat(image.getWidth(null),image.getHeight(null),CV_8UC1));
        Core.split(hsvImage,channels);
        Mat lut=getGammaExpo(step);
        Core.LUT(channels.get(2),lut,channels.get(2));;
        Mat res=new Mat(image.getHeight(null),image.getWidth(null),CV_8UC3);
        Core.merge(channels,rgbImage);
        for(Mat m:channels)m.release();
        cvtColor(rgbImage,res,COLOR_HSV2RGB_FULL);
        return matToImage(res);
    }

    static public BufferedImage negative(BufferedImage image){
        Mat rgbImage=imageToMat(image);
        ArrayList<Mat> channels=new ArrayList<>();
        channels.add(new Mat(image.getWidth(null),image.getHeight(null),CV_8UC1));
        channels.add(new Mat(image.getWidth(null),image.getHeight(null),CV_8UC1));
        channels.add(new Mat(image.getWidth(null),image.getHeight(null),CV_8UC1));
        Core.split(rgbImage,channels);
        Mat lut=new Mat(1, 256, CV_8UC1);
        for(int i=0;i<256;i++) {
            lut.put(0, i, 255-i);
        }
        for(Mat ch:channels)Core.LUT(ch,lut,ch);
        Core.merge(channels,rgbImage);
        for(Mat m:channels)m.release();
        return matToImage(rgbImage);
    }

    static public BufferedImage contrast(BufferedImage image,int step){
        ArrayList<Mat> channels=new ArrayList<>();
        Mat rgbImage=imageToMat(image);
        Core.split(rgbImage,channels);
        Mat lut=new Mat(1, 256, CV_8UC1);
        double contrastLevel = ((double)100 + (double) step) / 100;
        for(int i=0;i<256;i++) {
            double d=((((double)i)/255-0.5)*contrastLevel+0.5)*255;
            if(d>255)d=255;
            if(d<0)d=0;
            lut.put(0, i, d);
        }
        for(Mat ch:channels)Core.LUT(ch,lut,ch);
        Core.merge(channels,rgbImage);

        BufferedImage ret=matToImage(rgbImage);
        for(Mat m:channels)m.release();
        rgbImage.release();
        lut.release();
        return ret;
    }

    static public BufferedImage palette(BufferedImage image,int num){
        Mat rgbImage=imageToMat(image);
        ArrayList<Mat> channels=new ArrayList<>();
        channels.add(new Mat(image.getWidth(null),image.getHeight(null),CV_8UC1));
        channels.add(new Mat(image.getWidth(null),image.getHeight(null),CV_8UC1));
        channels.add(new Mat(image.getWidth(null),image.getHeight(null),CV_8UC1));
        Core.split(rgbImage,channels);
        Mat lut=new Mat(1, 256, CV_8UC1);
        int d;
        switch (num){
            case 8: d=256/2; break;
            case 27: d=256/3; break;
            case 64: d=256/4; break;
            case 125: d=256/5; break;
            default:throw new RuntimeException();
        }
        for(int i=0;i<256;i++) lut.put(0, i, (i+d/2)/d*d);
        for(Mat ch:channels)Core.LUT(ch,lut,ch);
        Mat res=new Mat();
        Core.merge(channels,res);
        for(Mat m:channels)m.release();
        return matToImage(res);
    }

    public static BufferedImage blur(BufferedImage image, int step){
        Mat img=imageToMat(image);
        Mat blur=new Mat();
        Imgproc.blur(img,blur,new Size(step,step));
        return matToImage(blur);
    }

    public static BufferedImage pseudoColor(BufferedImage image){
        Mat img=imageToMat(image);
        Mat gray=new Mat(img.rows(),img.cols(),CV_8UC1);
        Imgproc.cvtColor(img,gray,COLOR_BGR2GRAY);
        Mat white=Mat.zeros(img.rows(),img.cols(),CV_8U);
        white.setTo(new Scalar(255));
        ArrayList<Mat> ch=new ArrayList<>();
        ch.add(gray);
        ch.add(white);
        ch.add(white);
        Mat merged=new Mat();
        Core.merge(ch,merged);
        Mat pseudo=new Mat();
        Imgproc.cvtColor(merged,pseudo,COLOR_HSV2BGR_FULL);
        return matToImage(pseudo);
    }

    static public BufferedImage gray(BufferedImage image){
        Mat rgbImage=imageToMat(image);
        Mat gray=new Mat(image.getWidth(null),image.getHeight(null), CV_8U);
        Imgproc.cvtColor(rgbImage,gray,COLOR_BGR2GRAY);
        return matToImage(gray);
    }

    public static BufferedImage id(Image image){
        BufferedImage bi=new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_3BYTE_BGR);
        Graphics g =bi.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0,0,bi.getWidth(),bi.getHeight());
        g.drawImage(image,0,0,null);
        System.out.println(1);
        return bi;
    }

    private static Mat imageToMat(BufferedImage image){
        BufferedImage bi=new BufferedImage(image.getWidth(null),image.getHeight(null),BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(image,0,0,null);
        Mat rgbImage = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        rgbImage.put(0, 0, data);
        return rgbImage;
    }

    private static BufferedImage matToImage(Mat mat){
        MatOfByte mob=new MatOfByte();
        Highgui.imencode(".jpg", mat, mob);
        byte ba[]=mob.toArray();

        BufferedImage bi;
        try {
            bi= ImageIO.read(new ByteArrayInputStream(ba));
        } catch (IOException e) {
            throw new RuntimeException();
        }
        mat.release();
        return bi;
    }
}
