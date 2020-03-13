package chapter8;

import java.util.ArrayList;

public class UnsafeArrayList {

    static ArrayList al = new ArrayList();

    public static void main(String[] args) {
        Thread t1 = new Thread(new AddTask(), "t1");
        Thread t2 = new Thread(new AddTask(), "t2");
        t1.start();
        t2.start();
        Thread t3 = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10000);
                    } catch (Exception e) {
                    }
//
                    System.out.println("hello");
                }
            }
        }, "t3");
        t3.start();
    }

    static class AddTask implements Runnable {
        public void run() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
            for (int i = 0; i < 1000000; i++) {
                al.add(new Object());
            }
        }
    }
}
