package com.chenjw.ocr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class OCR {


    public String recognizeText(File imageFile, String imageFormat) throws Exception {

        File outputFile = new File(imageFile.getParentFile(), "output.txt");
        String result = null;
        List<String> cmd = new ArrayList<String>();

        cmd.add("tesseract");

        cmd.add(imageFile.getAbsolutePath());
        cmd.add("output");
        cmd.add("-l");
        cmd.add("lang");
        cmd.add("-psm");
        cmd.add("10");

        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(imageFile.getParentFile());

        System.out.println(imageFile.getParentFile() + " " + cmd);
        pb.command(cmd);
        pb.redirectErrorStream(true);
        Process process = pb.start();
        //tesseract.exe 1.jpg 1 -l chi_sim
        int w = process.waitFor();



        if (w == 0) {

            result = FileUtils.readFileToString(outputFile);

            result = StringUtils.left(result, 1);
        } else {
            String msg;
            switch (w) {
                case 1:
                    msg = "Errors accessing files. There may be spaces in your image's filename.";
                    break;
                case 29:
                    msg = "Cannot recognize the image or its selected region.";
                    break;
                case 31:
                    msg = "Unsupported image format.";
                    break;
                default:
                    msg = "Errors occurred.";
            }


        }
        System.out.println("result="+result);

        return result;
    }
}
