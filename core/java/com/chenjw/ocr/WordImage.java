package com.chenjw.ocr;

import java.awt.image.BufferedImage;

public class WordImage {
    private BufferedImage image;
    private String word;
    public BufferedImage getImage() {
        return image;
    }
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    
}
