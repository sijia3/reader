package com.ridko.module.reader.reflections;

/**
 * @author sijia3
 * @date 2021/1/20 18:11
 */
@Deprecated
public class Test {
    public static void main(String[] args) {
        String s = "33";
        Object o = s;
        System.out.println(o.getClass());
        Class<String> stringClass = String.class;
        stringClass.cast(s);

        String ss = s;
        s = "33";
        StringBuilder dd = new StringBuilder("dd");
        dd.append("dd");
        dd.toString();
//        ss = dd

        return;
    }
}
