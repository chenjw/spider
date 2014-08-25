package com.chenjw.ocr;


import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.xerces.impl.dv.util.Base64;

public class ReadFile {
    private File file=new File("/home/chenjw/test/img/verifycode.txt");
    private File output=new File("/home/chenjw/test/img/sample/");
    int i=0;
    public void parse() throws IOException{
        for(Object o:FileUtils.readLines(file)){
            
            String line=(String)o;
            if(!StringUtils.contains(line, "verifyCode,")){
                continue;
            }
            String temp=StringUtils.substringAfter(line, "verifyCode,");
            String code=StringUtils.substringBefore(temp, ",");
            String base64=StringUtils.substringAfter(temp, ",");
            base64=StringUtils.replace(base64, "<br/>", "\n");
            byte[] bytes=Base64.decode(base64);
            try{
                FileUtils.writeByteArrayToFile(new File(output,code+"_"+i+".jpg"), bytes);
            }
            catch(Exception e){
               
            }
            i++;
        }
    }
    
    
    public static void main(String[] args) throws Exception {
        ReadFile m = new ReadFile();
        m.parse();
    }
}
