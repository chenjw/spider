package com.chenjw.spider.aliuser.impl;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import akka.actor.Actor;

import com.chenjw.spider.aliuser.model.AliUserInfo;
import com.chenjw.tools.BeanCopyUtils;
import com.csvreader.CsvReader;

public class StatActor {

    public void action() throws Exception {
        CsvReader csvReader = new CsvReader("/home/chenjw/test/2014-01-27.csv", ',',
            Charset.forName("UTF-8"));
        csvReader.readHeaders();
        Map<String, Integer> postMap = new HashMap<String, Integer>();
        while (csvReader.readRecord()) {
            List<String> list=new ArrayList<String>(Arrays.asList(csvReader.getValues()));
            AliUserInfo user = new AliUserInfo();
            BeanCopyUtils.copyProperties(user, list);
            String post = user.getPost();
            Integer postNum = postMap.get(post);
            if (postNum == null) {
                postNum = 0;
            }
            postNum++;
            postMap.put(post, postNum);
        }
        Set<Entry<String,Integer>> entrySet=postMap.entrySet();
        Entry<String,Integer>[] entrys=entrySet.toArray(new Entry[entrySet.size()]);
        Arrays.sort(entrys, new Comparator< Entry<String,Integer>>(){

            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o1.getValue()-o2.getValue();
            }});
        for(Entry<String,Integer> entry:entrys){
            System.out.println(entry.getKey()+" = "+entry.getValue());
        }
    }

}
