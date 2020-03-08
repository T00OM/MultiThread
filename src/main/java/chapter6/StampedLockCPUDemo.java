package chapter6;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.StampedLock;

public class StampedLockCPUDemo {

    static Thread[] holdCPUThreads = new Thread[3];
    static final StampedLock lock = new StampedLock();

    public static void main(String[] args) throws InterruptedException {
        new Thread() {
            public void run() {
                long readLong = lock.writeLock();
                LockSupport.parkNanos(5000000000000L);
                lock.unlockWrite(readLong);
            }
        }.start();
        Thread.sleep(400);
        /*
            StampedLock内部使用类似CAS操作的死循环，当挂起线程时，使用的Unsafe.park(),park()在遇到线程中断时，不会抛出异常，直接返回，造成一直循环。
            这里的读线程显然得不到锁，会处在WAITING的状态
         */
        for(int i = 0; i < 3; ++i) {
            holdCPUThreads[i] = new Thread(new HoldCPUReadThread());
            holdCPUThreads[i].start();
        }
        Thread.sleep(10000);
        for(int i = 0; i < 3; ++i) {
            holdCPUThreads[i].interrupt();
        }
    }
    private static class HoldCPUReadThread implements Runnable {
        @Override
        public void run() {
            long lockr = lock.readLock();
            System.out.println(Thread.currentThread().getName() + "获得读锁");
            lock.unlockRead(lockr);
        }
    }
}
