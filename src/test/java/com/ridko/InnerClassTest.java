package com.ridko;

import com.ridko.common.domain.BaseDevMsg;

/**
 * @author sijia3
 * @date 2021/1/11 17:14
 */
public class InnerClassTest {

    private static BaseDevMsg devMsg = new BaseDevMsg();

    public void add(String str) {
        devMsg.setBrand(str);
    }

    public void inner() {
        new InnerClass().addOut();
    }

    class InnerClass {

        public void addOut() {
            InnerClassTest.this.add("laji");
        }
    }

    public static void main(String[] args) {
        InnerClassTest innerClassTest = new InnerClassTest();
        innerClassTest.inner();
        return;

    }
}
