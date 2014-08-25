package com.chenjw.ocr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TemplateFind {
    private int[][] template;
    private int     threshold;
    int             templateCenterX;
    int             templateCenterY;
    int templateCount;
    private int getTemplateValue(int x, int y) {

        if (x - templateCenterX < 0 || y - templateCenterY < 0) {
            return Color.WHITE.getRGB();
        }
        if (x - templateCenterX + template[0].length >= img.getWidth()
            || y - templateCenterY + template.length >= img.getHeight()) {
            return Color.WHITE.getRGB();
        }
        int r = 0;
        for (int j = 0; j < template.length; j++) {

            for (int i = 0; i < template[j].length; i++) {

                r += img.getRGB(x - templateCenterX + i, y - templateCenterY + j) * template[j][i];

            }
        }

        return r;

    }

    public class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public void makeGray() {
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int gray = getGray(i, j);
                img.setRGB(i, j, gray);
            }
        }
    }

    public void makeLine() {
        int[] temp = new int[img.getHeight()];
        for (int j = 0; j < img.getHeight(); j++) {
            temp[j] = 0;
        }
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
        for (int i = 0; i < img.getWidth(); i++) {

            for (int j = 0; j < img.getHeight(); j++) {
                int v =  getTemplateValue(i, j);
                //newImg.setRGB(i, j, v);
                //||(v<threshold/50 && temp[j]>0)
                if (Math.abs(v)> threshold  ) {
                    System.out.println(v);
                    newImg.setRGB(i, j, Color.BLACK.getRGB());
                    temp[j]=temp[j]+1;
                } else {
                    newImg.setRGB(i, j, Color.WHITE.getRGB());
                    temp[j]=0;
                }

            }

        }
        img = newImg;
    }

    private int getGray(int x, int y) {
        Object data = img.getRaster().getDataElements(x, y, null);//获取该点像素，并以object类型表示  
        int red = img.getColorModel().getRed(data);
        int blue = img.getColorModel().getBlue(data);
        int green = img.getColorModel().getGreen(data);

        int pixelGray = (int) (0.3 * red + 0.59 * green + 0.11 * blue);//计算每个坐标点的灰度
        int rgb = (pixelGray << 16) + (pixelGray << 8) + (pixelGray);

        return rgb;
    }

    private BufferedImage img;

    public TemplateFind(BufferedImage img, int[][] template, int threshold) {
        this.img = img;
        this.template = template;
        this.threshold = threshold;
        this.templateCenterX = template[0].length / 2;
        this.templateCenterY = template.length / 2;

    }

    public BufferedImage make() {
        makeGray();
        makeLine();
        return img;
    }

    public static void main(String[] args) throws IOException {
//        int[][] template = new int[][] {
//                //
//                { -1, -2, -3, -4, -3, -2, -1 },//
//                { 1, 2, 3, 4, 3, 2, 1 },//
//                { 1, 2, 3, 4, 3, 2, 1 },//
//                { -1, -2, -3, -4, -3, -2, -1 },//
//
//        };
        int[][] template = new int[][] {
                                        //
                                        { -1, -1, -1, -1, -1, -1, -1 },//
                                        { 2, 2,2,2,2,2, 2 },//
                                        { -1, -1, -1, -1, -1, -1, -1 },//

                                };
        BufferedImage img = ImageIO.read(new File("/home/chenjw/test/img/sample/7ndgh_197.jpg"));
        img = new TemplateFind(img, template, 55331645).make();
        //img=new TemplateFind(img,template,-83886079).make();
        ImageIO.write(img, "jpg", new File("/home/chenjw/test/img/sample/7ndgh_197.jpg.jpg"));
    }
}
