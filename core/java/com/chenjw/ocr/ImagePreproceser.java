package com.chenjw.ocr;

import ij.IJ;

import java.awt.Graphics;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImagePreproceser {
 
    
    
    public static File prepare(File file) throws Exception {
        BufferedImage image = ImageIO.read(file);
        //
        //image=new ImageFilter(image).grayFilter();

        //image=new ImageFilter(image).removeBackgroud();
        //image=new ImageFilter(image).changeGrey();
        //image=new ImageFilter(image).median();
        //打开图片  

        //image=new ImageFilter(image).sharp();
        File out = new File("/home/chenjw/test/img/sample/" + file.getName() + ".jpg");
        try{
          
            ImageIO.write(image, "jpg", out);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        

        IJ.open(out.getAbsolutePath());
        //IJ.run("Remove Outliers...", "radius=2 threshold=50 which=Dark");
        // 锐化
        //IJ.run("Sharpen");  
        //二值  
        IJ.run("Make Binary");
        // 去掉干扰实线
        IJ.run("Remove Outliers...", "radius=1 threshold=100 which=Bright");
        // 去掉干扰虚线
        //IJ.run("Remove Outliers...", "radius=1 threshold=100 which=Dark");
        //
        //IJ.run("Smooth");  
        // 去掉噪点
        IJ.run("Despeckle");
        //IJ.run("Skeletonize");
        IJ.save(out.getAbsolutePath());
        //String maybe2 = new OCR().recognizeText(new  File("/home/chenjw/test/img/output/o.jpg"), "jpg");
        System.out.println("**********");
        //System.out.println(path+"="+maybe2+";");
        System.out.println("**********");
        return out;
    }

    private static BufferedImage ss(int ww,BufferedImage img){
        BufferedImage newImg=new BufferedImage(img.getWidth()-ww*2,img.getHeight(),img.getType());
        Graphics g=newImg.getGraphics();
        g.drawImage(img, 0, 0, img.getWidth()-ww*2, img.getHeight(), ww, 0,  img.getWidth()-ww,img.getHeight(), null);   
        g.dispose();
        return newImg;
    }

    public static List<WordImage> split(File f) throws FileNotFoundException, IOException {
        BufferedImage img = ImageIO.read(new FileInputStream(f));
        img=ss(5,img);
        int w = img.getWidth();
        int h = img.getHeight();
        List<WordImage> images=new ArrayList<WordImage>();
        for (int i = 0; i < 5; i++) {
            BufferedImage newImg = new BufferedImage(w / 5, h, img.getType());
            Graphics g1 = newImg.getGraphics();
            g1.drawImage(img, 0, 0, w / 5, h, i * w / 5, 0, (i + 1) * w / 5, h, null);
            g1.dispose();
            WordImage wordImage=new WordImage();
            wordImage.setImage(newImg);
            wordImage.setWord(String.valueOf(f.getName().charAt(i)));
            images.add(wordImage);
            //ImageIO.write(newImg, "jpg", new File("/home/chenjw/test/img/sample/o/"+String.valueOf(f.getName().charAt(i))+".jpg"));
        }
        return images;
    }


}
