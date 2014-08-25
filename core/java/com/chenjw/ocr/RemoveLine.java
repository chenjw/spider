package com.chenjw.ocr;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.imageio.ImageIO;

import com.chenjw.ocr.ImageFilter;

public class RemoveLine {

    public static class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public void makeGray() {
        int width = img.getWidth();  
        int height = img.getHeight();  
          
        BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_BINARY);  
        for(int i= 0 ; i < width ; i++){  
            for(int j = 0 ; j < height; j++){  
            int rgb = img.getRGB(i, j);  
            grayImage.setRGB(i, j, rgb);  
            }  
        }  
        img=grayImage;
//        for (int i = 0; i < img.getWidth(); i++) {
//            for (int j = 0; j < img.getHeight(); j++) {
//                int gray = getGray(i, j);
//                img.setRGB(i, j, gray);
//            }
//        }
    }

    private boolean checkValue(int v) {
        if(v==-1){
            System.out.println(v);
            return true;
        }
        else{
            return false;
        }

    }

    private ArrayList<Point> copy(ArrayList<Point> s) {
       
        ArrayList<Point> c = (ArrayList<Point>) s.clone();
        return c;
    }

    private List<List<Point>> saved=new ArrayList<List<Point>>();
    
    private void checkEnd(List<Point> s) {
        if (s != null && s.size() > 1) {
            saved.add(s);
            System.out.println("aaaaaaaaaaaaaaa"+s.size());
        }
    }

    private void find(int[][] a,ArrayList<Point> s, int i, int j) {
        //System.out.println(i+" "+j+" ");
        if (0 <= i && i < img.getWidth() && 0 <= j && j < img.getHeight()) {
            int v = a[i][j];
            if (checkValue(v)) {
                ArrayList<Point> n;
                if (s == null) {
                    n = new ArrayList<Point>();
                } else {
                    n = copy(s);
                }
                n.add(new Point(i, j));
                find(a,n, i + 1, j - 1);
                
                find(a,n, i + 1, j);
                find(a,n, i + 1, j + 1);
                return;
            } else {
                checkEnd(s);
                find(a,null, i + 1, j);
                return;
            }
        } else {
            checkEnd(s);
        }

    }

    int threshold = 10;  
    int offset = 11;  

    private int a(int i,int j){
   
            if(i<0 || j<0){
                return 0;
            }
            int v=img.getRGB(i, j);
            if(checkValue(v)){
                return 1;
            }
            else{
                return 0;
            }
        
    }
    
    void genLine(int [] b,int n)  
    {  
        if (n < offset)  
        {  
            b[n] = -1;  
            genLine(b,n+1);  
        }  
        if (n == img.getWidth())  
        {  
            for (int i = 0; i < img.getWidth(); i++)  
            {  
                System.out.print(b[i]+" ");  
            }  
            System.out.println();
        }  
        if (n==offset)  
        {  
            for (int j = 0; j < img.getHeight(); j++)  
            {  
                if (a(offset,j) == 1)  
                {  
                    b[offset] = j;  
                    genLine(b,n+1);  
                }  
            }  
        }  
        if (n > 0 && n <img.getWidth())  
        {  
            int hasMore = 0;  
            if (a(n,b[n-1]) == 1){  
                b[n] = b[n-1];  
                hasMore = 1;  
                genLine(b,n+1);  
            }  
            else  
            {  
                if (b[n-1] > 0 && a(n,b[n-1]-1) == 1){  
                    b[n] = b[n-1]-1;  
                    hasMore = 1;  
                    genLine(b,n+1);  
                }  
                if (b[n-1] < img.getHeight()-1 && a(n,b[n-1]+1) == 1){  
                    b[n] = b[n-1]+1;  
                    hasMore = 1;  
                    genLine(b,n+1);  
                }  
            }  
            if (n - offset > threshold && hasMore == 0)  
            {  
                for (int i = 0; i < n; i++)  
                {  
                    System.out.print(b[i]+" ");  
                }  
                System.out.println(); 
            }  
        }  
    }  
    
    public void makeLine() {
        
        int[][] a=new int[img.getWidth()][img.getHeight()];
        for(int i=0;i<img.getWidth();i++){
            for(int j=0;j<img.getHeight();j++){
                int v=img.getRGB(i, j);
                if(checkValue(v)){
                    a[i][j]=1;
                }
                else{
                    a[i][j]=0;
                }
            }
        }
        int [] n=new int[img.getWidth()];
        for(int i=0;i<n.length;i++){
            n[i]=-1;
        }
//        genLine(n,0);
        for (int j = 0; j < img.getHeight(); j++) {
            find(a,null, 0, j);
        }
        for(List<Point> pp:saved){
            for(Point p:pp){
                img.setRGB(p.x, p.y, Color.RED.getRGB());
            }
        }

    }

    private int getGray(int x, int y) {
        Object data = img.getRaster().getDataElements(x, y, null);//获取该点像素，并以object类型表示  
        int red = img.getColorModel().getRed(data);
        int blue = img.getColorModel().getBlue(data);
        int green = img.getColorModel().getGreen(data);
        

    int pixelGray=(int)(0.3*red+0.59*green+0.11*blue);//计算每个坐标点的灰度
    int rgb=(pixelGray<<16)+(pixelGray<<8)+(pixelGray);
        

        return rgb;
    }

    private BufferedImage img;

    public RemoveLine(BufferedImage img) {
        this.img = img;
    }

    public BufferedImage make() {
        makeGray();
        makeLine();
        return img;
    }

    public static void main(String[] args) throws IOException {
        BufferedImage img = ImageIO.read(new File("/home/chenjw/test/img/sample/7ndgh_197.jpg"));
        //img=new BufferedImage(10,10,BufferedImage.TYPE_INT_RGB);
        img = new RemoveLine(img).make();

        ImageIO.write(img, "jpg", new File("/home/chenjw/test/img/sample/7ndgh_197.jpg.jpg"));
    }
}
