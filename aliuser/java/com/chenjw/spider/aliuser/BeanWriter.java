package com.chenjw.spider.aliuser;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.chenjw.tools.BeanCopyUtils;
import com.chenjw.tools.beancopy.util.ReflectUtils;
import com.csvreader.CsvWriter;

public class BeanWriter {
    private Class<?>  beanClazz;

    private CsvWriter csvWriter;

    public BeanWriter(String csvFilePath) {
        File f = new File(csvFilePath);
        try {
            FileUtils.forceMkdir(f.getParentFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        csvWriter = new CsvWriter(csvFilePath, ',', Charset.forName("UTF-8"));
    }

    public void write(Object obj) {
        if (beanClazz == null) {
            beanClazz = obj.getClass();
            List<String> names = new ArrayList<String>();
            PropertyDescriptor[] getters = ReflectUtils.getBeanGetters(beanClazz);

            Arrays.sort(getters, new Comparator<PropertyDescriptor>() {

                @Override
                public int compare(PropertyDescriptor o1, PropertyDescriptor o2) {
                    return o1.getName().compareTo(o2.getName());
                }

            });
            for (PropertyDescriptor getter : getters) {
                names.add(getter.getName());
            }
            try {
                csvWriter.writeRecord(names.toArray(new String[names.size()]));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (beanClazz != obj.getClass()) {
            throw new RuntimeException("类型不对");
        }
        List<String> list = new ArrayList<String>();
        BeanCopyUtils.copyProperties(list, obj, String.class);
        try {
            csvWriter.writeRecord(list.toArray(new String[list.size()]));
            csvWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
