package com.example;

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
        String[] split = "yeah|7.jpg|6.jpeg|".split("\\|");

        System.out.println(split.length);

        for(int i = 0;i<split.length;i++)
            System.out.println(i+" "+split[i]);
    }
}
