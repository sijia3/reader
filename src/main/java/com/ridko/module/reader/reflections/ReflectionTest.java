package com.ridko.module.reader.reflections;

import com.ridko.common.domain.BaseDevMsg;
import com.ridko.module.reader.AbstractReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sijia3
 * @date 2021/1/8 13:45
 */
@Deprecated
public class ReflectionTest {

    private static Map<String, com.ridko.module.producer.factory.ProducerFactory> factoryMap = new HashMap<>();

    private static Map<String, Class<AbstractReader>> readerClassMap = new HashMap<>();

    public static void main(String[] args) throws Exception {
//        BaseDevMsg baseDevMsg = SingleEnum.INSTANCE.getBaseDevMsg();
//        List<? extends Object> objects = new ArrayList<>();
//        List<Object> objects1 = new ArrayList<>();
//        List<? extends Object> objects2 = new ArrayList<>();
//
//        Object[] objects3 = new Object[1];
//        Integer[] integers = new Integer[2];
//        objects3[0] = integers[0];
//        integers = (Integer[]) objects3;
//
//        Object obj = new Object();
//        Integer in = new Integer("");
//        obj = in;
//        in = (Integer) obj;
//        System.out.println((Integer) obj);
//
//
//        List<Object> objects4 = new ArrayList<>();
//        objects4.add("32");
//        objects4.add(2);
//        List<? extends Object> integers1 = new ArrayList<>();
//        integers1 = objects4;
////        integers1.add("33");
//        Object o = (Object) integers1.get(0);
//        if (o instanceof Integer) {
//
//        }
//
//
//        List<? super Number> list = new ArrayList<>();
//        List<Integer> integerList = new ArrayList<>();
//        List<String> stringList = new ArrayList<>();
//
//        List<Object> objectList = new ArrayList<>();
//        objectList.add("22");
//
//        List<? super Number> al = objectList;
//        al = integerList;
//        al = stringList;
//        al.add(new Integer(2));
//        al.add("");
//
//        List<Number> numbers = new ArrayList<>();
//        numbers.add(new Integer(""));
//
//        list = objectList;
////        Number object2 = (Number)list.get(0);
//
//        list.add(new Integer(1));     // ??
//        list.add(new Number() {
//            @Override
//            public int intValue() {
//                return 0;
//            }
//
//            @Override
//            public long longValue() {
//                return 0;
//            }
//
//            @Override
//            public float floatValue() {
//                return 0;
//            }
//
//            @Override
//            public double doubleValue() {
//                return 0;
//            }
//        });
//        list.add(new String("dd"));
//
//
//        Object object1 = list.get(0);
//
//
//        Test<String> test = new Test<>();
//        test.setObjects("w33");
//        String object = test.getObject();
//        System.out.println(object);
    }

    private static String convert2Key(String brand, String functionType) {
        return brand + functionType;
    }


    static enum SingleEnum {
        INSTANCE;

        private BaseDevMsg devMsg = new BaseDevMsg();

        public BaseDevMsg getBaseDevMsg() {
            return devMsg;
        }
    }

    static class Test<E> {
        private Object[] objects;

        public Test() {
            objects = new Object[14];
        }

        public void setObjects(E object) {
            objects[0] = object;
        }

        public E getObject() {
            return (E)objects[0];
        }
    }


}
