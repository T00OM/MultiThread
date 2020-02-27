package chapter3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReenterLockCondition implements Runnable {

    public static ReentrantLock lock = new ReentrantLock();
    public static Condition condition = lock.newCondition();
    public void run(){
        try{
            lock.lock();
            condition.await();
            System.out.println("Thread is going on");
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
    public static void main(String[] args) throws InterruptedException{
        ReenterLockCondition r1 =new ReenterLockCondition();
        Thread t1 = new Thread(r1);
        t1.start();
        Thread.sleep(2000);
        lock.lock();
        System.out.println("notify thread");
        condition.signal();
        lock.unlock();
        System.out.println("test");
    }


}
