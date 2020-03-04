package chapter5;

import sun.applet.resources.MsgAppletViewer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Pipeline {
    public static class MSG {
        public int i;
        public int j;
    }

    public static class Add implements Runnable {
        public volatile static boolean flag = true;
        public static BlockingQueue<MSG> blockingQueue = new LinkedBlockingQueue<>();

        @Override
        public void run() {
            while(flag) {
                try {
                    MSG msg = blockingQueue.take();
                    msg.i = msg.i + msg.j;
                    Multiply.blockingQueue.offer(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static class Multiply implements Runnable {
        public volatile static boolean flag = true;
        public static BlockingQueue<MSG> blockingQueue = new LinkedBlockingQueue<>();

        @Override
        public void run() {
            while(flag) {
                try {
                    MSG msg = blockingQueue.take();
                    msg.i = msg.i * msg.i;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final long start = System.currentTimeMillis();
        Add add = new Add();
        Multiply multiply = new Multiply();
        Thread thread1 = new Thread(add);
        Thread thread2 = new Thread(multiply);
        for(int i = 1; i <= 1000; i++) {
            for(int j = 1; j < 1000; j++) {
                MSG msg = new MSG();
                msg.i = i;
                msg.j = i;
                Add.blockingQueue.offer(msg);
//                msg.i = (msg.i + msg.j) * (msg.i + msg.j);

            }
        }
        if(Add.blockingQueue.size() == 0)
            Add.flag = false;
        if(Multiply.blockingQueue.size() == 0)
            Multiply.flag = false;
        thread1.join();
        thread2.join();
        System.out.println(System.currentTimeMillis()-start);
    }
}
