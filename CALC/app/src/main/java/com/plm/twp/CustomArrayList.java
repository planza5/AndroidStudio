package com.plm.twp;

import java.util.ArrayList;
import java.util.List;

public class CustomArrayList<T> {
    List<String> stringArray = new ArrayList();
    List<Integer> intArray = new ArrayList();

    public void add(String ns, int n) {
        stringArray.add(ns);
        intArray.add(n);
    }

    public Integer getValue(String text,Integer index){
        index=6;
        return 3;
    }
}
