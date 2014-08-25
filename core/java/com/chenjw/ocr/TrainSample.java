package com.chenjw.ocr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;

public class TrainSample {

    private int           width    = 16;

    private int           xIndex   = 0;

    private int           height   = 50;
    // 间隔
    private int intervalWidth=4;
    private List<String>  boxLines = new ArrayList<String>();
    private BufferedImage sampleImage;
    {
        sampleImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = sampleImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, sampleImage.getWidth(), sampleImage.getHeight());
        g.dispose();
    }

    private void checkResize(int addWigth) {
        int oldWidth=width;
        while (xIndex + addWigth+intervalWidth >= width) {
            int newWidth = width << 1;
            width = newWidth;
        }
        if(width!=oldWidth){
            BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = newImg.getGraphics();
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, newImg.getWidth(), newImg.getHeight());

            g.drawImage(sampleImage, 0, 0, sampleImage.getWidth(), sampleImage.getHeight(), 0, 0,
                sampleImage.getWidth(), sampleImage.getHeight(), null);

            g.dispose();

            sampleImage = newImg;
            
//            String img = "/home/chenjw/test/img/t1/"+newWidth+".jpg";
//            try {
//                ImageIO.write(sampleImage, "jpg", new File(img));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        
       

    }

    public void train(WordImage wordImage) {
        BufferedImage img=wordImage.getImage();
        String textWords=wordImage.getWord();
        int minX = 0;
        int maxX = img.getWidth()-1;
        int minY = 0;
        int maxY = img.getHeight()-1;
        for (int i = 0; i < img.getWidth(); i++) {
            boolean hasBlack=false;
            for (int j = 0; j < img.getHeight(); j++) {
                int color = img.getRGB(i, j);
                   if(color!=-1){
                       hasBlack=true;
                   }
            }
            if(hasBlack){
                break;
            }
            else{
                minX++;
            }
        }
        
        for (int i = img.getWidth()-1; i >=0; i--) {
            boolean hasBlack=false;
            for (int j = 0; j < img.getHeight(); j++) {
                int color = img.getRGB(i, j);
                   if(color!=-1){
                       hasBlack=true;
                   }
            }
            if(hasBlack){
                break;
            }
            else{
                maxX--;
            }
        }
        
        
        for (int j = 0; j < img.getHeight(); j++) {
            boolean hasBlack=false;
            for (int i = 0; i < img.getWidth(); i++) {
                int color = img.getRGB(i, j);
                   if(color!=-1){
                       hasBlack=true;
                   }
            }
            if(hasBlack){
                break;
            }
            else{
                minY++;
            }
        }
        
        for (int j = img.getHeight()-1; j >=0; j--) {
            boolean hasBlack=false;
            for (int i = 0; i < img.getWidth(); i++) {
                int color = img.getRGB(i, j);
                   if(color!=-1){
                       hasBlack=true;
                   }
            }
            if(hasBlack){
                break;
            }
            else{
                maxY--;
            }
        }
        
        
        
        
        int w = maxX - minX+1;
        int h = maxY - minY+1;
        checkResize(maxX - minX);
        Graphics g = sampleImage.getGraphics();
        
        g.drawImage(img, xIndex , (height-h)/2, w+xIndex, (height+h)/2, minX, minY, maxX, maxY, null);
        g.dispose();
        boxLines.add(textWords + " " +  xIndex + " " + (height-h)/2 + " " + (xIndex+w) + " " + (height+h)/2 + " 0");
        xIndex += w;
        xIndex+=intervalWidth;
    }

    public BufferedImage getSampleImage() {
        return sampleImage;
    }

    public void setSampleImage(BufferedImage sampleImage) {
        this.sampleImage = sampleImage;
    }

    public List<String> getBox() {
        return boxLines;
    }

    public void setBox(List<String> box) {
        this.boxLines = box;
    }

    public void save() {
        String img = "/home/chenjw/test/img/t1/lang.timesitalic.number.tif";
        String box = "/home/chenjw/test/img/t1/lang.timesitalic.number.box";
        try {
            ImageIO.write(sampleImage, "tif", new File(img));
            FileUtils.writeLines(new File(box), boxLines);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
