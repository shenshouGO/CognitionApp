package com.example;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by Administrator on 2020/12/30.
 */

public class Test {
    public static void main(String[] args) {
        String string = "http://59.110.215.154:8080/resource/1.jpg";
        String[] splits =string.split("\\.");
        String s = string.substring(string.lastIndexOf('.')+1,string.length());
//        for (int i = 0;i<splits.length;i++){
//            System.out.println(splits[i]);
//        }
        System.out.println(s);
    }
}
