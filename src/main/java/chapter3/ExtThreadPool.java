package chapter3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExtThreadPool {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(Runtime.getRuntime().availableProcessors());
        ExecutorService es = new ThreadPoolExecutor(
                5, 5, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>()
        ) {
            @Override
            protected void beforeExecute(Thread t, Runnable r) {
                System.out.println("before: " + ((MyTask) r).name);
            }

            protected void afterExecute(Runnable r, Throwable t) {
                System.out.println("after: " + ((MyTask) r).name);
            }

            @Override
            protected void terminated() {
                System.out.println("thread pool exit");
            }
        };
        for (int i = 0; i < 5; i++) {
            MyTask task = new MyTask("task-getm-" + i);
            es.execute(task);
            Thread.sleep(10);
        }
        es.shutdown();
    }

    public static class MyTask implements Runnable {
        public String name;

        public MyTask(String name) {
            this.name = name;
        }

        public void run() {
            System.out.println("working " + ":thread id:" + Thread.currentThread().getId() +
                    ",Task name=" + name);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
