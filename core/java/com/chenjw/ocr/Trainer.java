package com.chenjw.ocr;

import java.io.File;
import java.util.List;

import com.chenjw.img.ImagePreproceser;
import com.chenjw.img.TrainSample;
import com.chenjw.img.WordImage;

public class Trainer {
    private TrainSample trainSample=new TrainSample();;
   

    public static void main(String[] args) throws Exception {
        Trainer m = new Trainer();
        for (File f : new File("/home/chenjw/test/img/sample/").listFiles()) {
            if (f.isFile() && f.getName().endsWith(".jpg") && !f.getName().endsWith(".jpg.jpg")) {
                //
                File out=ImagePreproceser.prepare(f);
                List<WordImage> wordImages= ImagePreproceser.split(out);
                for(WordImage wordImage:wordImages){
                    m.trainSample.train(wordImage);
                }
            }
        }
        m.trainSample.save();
    }
}
