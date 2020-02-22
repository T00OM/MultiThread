package chapter2;

import java.util.ArrayList;

/*
抛出异常，两个线程访问到不一致的arrayList内部
 */
public class ArrayListMultiThread {

    static ArrayList<Integer> arrayList = new ArrayList<>();

    public static class MyThread implements Runnable{
        @Override
        public void run(){
            for(int i=0;i<10000;i++){
                arrayList.add(i);
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new MyThread());
        Thread t2 = new Thread(new MyThread());
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(ArrayListMultiThread.arrayList.size());
    }
}
