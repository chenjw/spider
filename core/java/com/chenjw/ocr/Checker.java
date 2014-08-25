package com.chenjw.ocr;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;

import com.chenjw.img.ImagePreproceser;
import com.chenjw.img.TrainSample;
import com.chenjw.img.WordImage;
import com.chenjw.ocr.OCR;

public class Checker {


    public static void main(String[] args) throws Exception {
        Map<String,Integer> count=new HashMap<String,Integer>();
        OCR ocr=new OCR();
        int successNum=0;
        int allNum=0;
        int idx=0;
        for (File f : new File("/home/chenjw/test/img/sample/").listFiles()) {
            if (f.isFile() && f.getName().endsWith(".jpg") && !f.getName().endsWith(".jpg.jpg")) {
                String want="";
                String got="";
                //
                File out=ImagePreproceser.prepare(f);
                List<WordImage> wordImages= ImagePreproceser.split(out);
                boolean success=true;
                for(WordImage wordImage:wordImages){
                    Integer ccc=count.get(wordImage.getWord());
                    if(ccc==null){
                        ccc=1;
                    }
                    else{
                        ccc++;
                        
                    }
                    count.put(wordImage.getWord(),ccc);
                    File temp=new File("/home/chenjw/test/img/sample/o/temp.jpg");
                    ImageIO.write(wordImage.getImage(), "jpg", temp);
                    String r=ocr.recognizeText(temp, "jpg");
                    temp.renameTo(new File("/home/chenjw/test/img/sample/o/"+wordImage.getWord()+"_"+r+"_"+idx+".jpg"));
                    
                    idx++;
                    if(!StringUtils.equalsIgnoreCase(r, wordImage.getWord())){
                        success=false;
                        
                    }
                    else{
                        successNum++;
                    }
                    want+=wordImage.getWord();
                    got+=r;
                    allNum++;
                }
                if(success){
                    //successNum++;
                }
                //allNum++;
                System.out.println("file : "+out.getName()+" ,want : "+want+" ,got : "+got);
            }
        }
        for(Entry<String,Integer> entry:count.entrySet()){
            System.out.println(entry.getKey()+" = "+entry.getValue());
        }
       System.out.println("成功率 : "+successNum*100D/allNum+"%");
    }
}
