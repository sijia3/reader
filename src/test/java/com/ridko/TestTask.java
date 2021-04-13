package com.ridko;

import lombok.Getter;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author sijia3
 * @date 2021/1/14 15:04
 */
@Getter
public class TestTask implements Callable<String> {

    private long time;

    private String taskName;

    public TestTask(long time, String taskName) {
        this.time = time;
        this.taskName = taskName;
    }


    @Override
    public String call() {
        try {
            TimeUnit.MILLISECONDS.sleep(time);
            System.out.println(taskName+"执行完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return taskName;
    }
}
