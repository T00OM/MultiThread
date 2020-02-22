package chapter3;

import java.util.concurrent.locks.ReentrantLock;

public class ReenterLock implements Runnable {

    /**
        如果Reentrantock对象的访问符为static，表明该重入锁为类锁。
        否则为对象锁
     */
    public static ReentrantLock lock = new ReentrantLock();
    public static int i=0;
    public void run(){
        for(int j=0;j<1000;j++){
            lock.lock();
            lock.lock();
            try{
                i++;
                Thread.sleep(1);
            }catch(InterruptedException e){
                e.printStackTrace();
            }finally{
                lock.unlock();
                lock.unlock();
            }
        }
    }
    public static void main(String[] args) throws InterruptedException{
        ReenterLock r1 = new ReenterLock();

        Thread t1 = new Thread(r1);
//        Thread t2 = new Thread(r1);

        ReenterLock r2 = new ReenterLock();
        Thread t2 = new Thread(r2);

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }

}
