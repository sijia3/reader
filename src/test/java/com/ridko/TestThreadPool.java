package com.ridko;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author sijia3
 * @date 2021/1/14 15:01
 */
@Slf4j
public class TestThreadPool {
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    private static LinkedList<TestTask> readerConnTaskQueue = new LinkedList<>();

    public TestThreadPool() {
        new ReaderConnectThead().start();
    }

    public void addTask(TestTask task) {
        readerConnTaskQueue.offer(task);
    }

    /**
     * 线程池内部连接线程
     * 监听readerConnTaskQueue
     * 如果有，取出并提交
     */
    private class ReaderConnectThead extends Thread {
        @Override
        public void run() {
            while (true) {
                TestTask task ;
                try {
                    task = readerConnTaskQueue.pollFirst();
                    System.out.println("线程池取出task:"+ task.getTaskName());
                    Future<String> submit = executorService.submit(task);
                    String isConnect = submit.get();
                    System.out.println(isConnect);
                }catch (Exception e) {

                }
            }
        }
    }

    public static void main(String[] args) {
        TestThreadPool testThreadPool = new TestThreadPool();

        TestTask task = new TestTask(3000, "第一个任务");
        TestTask task1 = new TestTask(1000, "第二个任务");
        TestTask task2 = new TestTask(500, "第三个任务");
        TestTask task3 = new TestTask(800, "第四个任务");
        testThreadPool.addTask(task);
        testThreadPool.addTask(task1);
        testThreadPool.addTask(task2);
        testThreadPool.addTask(task3);
    }

}
