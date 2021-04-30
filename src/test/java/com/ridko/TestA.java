package com.ridko;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * @author sijia3
 * @date 2021/1/15 9:39
 */
public class TestA {

    // add A
    // add B
    // add C

    public int count = 5;

    private TestA() {
        count = 544;
    }

    private int getA() {
        return count;
    }

    @SneakyThrows
    public static void main(String[] args) {
//        TestA testA = TestA.class.newInstance();
//        System.out.println(testA.count);


//        Method getA = TestA.class.getDeclaredMethod("getA");
//        getA.setAccessible(true);

//        Object invoke = getA.invoke(testA);

        Constructor<TestA> constructor = TestA.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        TestA testA = constructor.newInstance();
        System.out.println(testA.count);
        return;
    }
}
