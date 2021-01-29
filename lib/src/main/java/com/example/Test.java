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
        List<String> list = new LinkedList<String>();
        list.add("平均分");
        list.add("7.2");
        list.add("3.1");

        Com c = new Com();
        Collections.sort(list,c);

        for(int i = 0;i<list.size();i++)
            System.out.println(list.get(i));
    }
}

class Com implements Comparator{
    public int compare(Object arg0, Object arg1) {
        String d0=(String)arg0;
        String d1=(String)arg1;

        return d1.compareTo(d0);
    }
}
