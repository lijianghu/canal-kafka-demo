package com.example.canalkafkademo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 2020-1-9 01:37:10
 * lijianghu
 */
public class ThreadTeset {
    static ThreadFactory threadFactory = new NameTreadFactory();
    private static ThreadPoolExecutor exec = new ThreadPoolExecutor(
            10,
            20,
            200,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(10),
            threadFactory,
            new ThreadPoolExecutor.CallerRunsPolicy());

    static class MyTest1 implements Callable {
        int id;
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        MyTest1(int id){
            this.id = id;
        }

        public String call() throws Exception {
            Thread.sleep(2000);
            System.out.println("threadName:"+Thread.currentThread().getName()+",ID:"+this.id);
            return "success";
        }
    }

    public static void main(String[] args) throws Exception {
        List<Future> list = new ArrayList<Future>();
        for(int i =0;i<100;i++){
            MyTest1 myTest1 = new MyTest1(i);
            Future submit = exec.submit(myTest1);
            list.add(submit);
        }
        for(Future future:list){
            future.get();
        }
        System.out.println("cuccess!");
    }
    static class NameTreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "my-thread-" + mThreadNum.getAndIncrement());
            System.out.println(t.getName() + " has been created");
            return t;
        }
    }
}
