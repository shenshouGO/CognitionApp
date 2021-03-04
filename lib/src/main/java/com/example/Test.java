package com.example;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2020/12/30.
 */

public class Test {
    public static void main(String[] args) {
        try {
            InputStream is = new FileInputStream("f:/resource/" + "认知焦虑测评.txt");
            InputStreamReader reader = new InputStreamReader(is,"UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String line;
            String s="";
            while ((line = br.readLine()) != null) {
                s+=line+"\n";
            }

            br.close();
            reader.close();
            is.close();
            System.out.println(s);
            String[] split = s.split("\n");
            int[] ints = new int[split.length];
            for(int i = 0;i<ints.length;i++){
                System.out.println(i+" "+ints[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
