package chapter6;

import org.omg.IOP.TAG_ALTERNATE_IIOP_ADDRESS;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class FasterAtomicClass {

    private static final int TASK_COUNT = 3;
    private static final int TARGET_COUNT = 10000000;

    private AtomicLong acount = new AtomicLong(0);
    private LongAdder longAdder = new LongAdder();
    private volatile long count = 0;
    static CountDownLatch cLsync = new CountDownLatch(TASK_COUNT);
    static CountDownLatch cLatomic = new CountDownLatch(TASK_COUNT);
    static CountDownLatch cLadder = new CountDownLatch(TASK_COUNT);

    private class Sync implements Runnable {
        public synchronized long inc() {
            return ++count;
        }
        public synchronized long getCount() {
            return count;
        }
        @Override
        public void run() {
            long begin = System.currentTimeMillis();
            long v = getCount();
            while(v < TARGET_COUNT) {
                v = inc();
            }
            System.out.println("sync time " + (System.currentTimeMillis() - begin) + "  " + count);
            cLsync.countDown();
        }
    }
    private class Atomic implements Runnable {
        @Override
        public void run() {
            long begin = System.currentTimeMillis();
            long v = acount.get();
            while(v < TARGET_COUNT){
                v = acount.incrementAndGet();
            }
            System.out.println("atomic time " + (System.currentTimeMillis() - begin) + "  " + acount);
            cLatomic.countDown();
        }
    }
    private class Adder implements Runnable {
        @Override
        public void run() {
            long begin = System.currentTimeMillis();
            long v = longAdder.sum();
            while(v < TARGET_COUNT){
                longAdder.increment();
                v = longAdder.sum();
            }
            System.out.println("adder time " + (System.currentTimeMillis() - begin) + "  " + longAdder.sum());
            cLadder.countDown();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        FasterAtomicClass fasterAtomicClass = new FasterAtomicClass();

        for(int i = 0; i < 3; i++) {
            executorService.submit(fasterAtomicClass.new Sync());
        }
        cLsync.await();

        for(int i = 0; i < 3; i++) {
            executorService.submit(fasterAtomicClass.new Atomic());
        }
        cLatomic.await();

        for(int i = 0; i < 3; i++) {
            executorService.submit(fasterAtomicClass.new Adder());
        }
        cLadder.await();
        executorService.shutdown();
    }
}
