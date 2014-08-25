package com.chenjw.ocr;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class Main {
    public static void main(String[] args) throws Exception{
        Trainer.main(args);

        File trainSh=new File("/home/chenjw/test/img/t1/train.sh");
        List<String> cmd = new ArrayList<String>();

        cmd.add("sh");
        cmd.add(trainSh.getAbsolutePath());
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(trainSh.getParentFile());
        pb.command(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        process.waitFor();


        
        Checker.main(args);
    }
}
