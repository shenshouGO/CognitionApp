package com.example;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2020/12/30.
 */

public class Test {
    public static void main(String[] args) {
        String string = "xxxxxxx@163.com";
//        String[] splits =string.split("\\.");
//        String s = string.substring(string.lastIndexOf('.')+1,string.length());
//        for (int i = 0;i<splits.length;i++){
//            System.out.println(splits[i]);
//        }
        System.out.println(isEmail(string));
    }

    public static boolean isEmail(String email) {
        Pattern emailPattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher matcher = emailPattern.matcher(email);
        if(matcher.find()){
            return true;
        }
        return false;
    }
}
