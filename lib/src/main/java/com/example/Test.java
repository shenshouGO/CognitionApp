package com.example;

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
        String url = "好\n和";
        System.out.println(url);
        try {
            url = java.net.URLEncoder.encode(url,"UTF-8");
            System.out.println(url);
            url = java.net.URLDecoder.decode(url,"UTF-8");
            System.out.println(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
