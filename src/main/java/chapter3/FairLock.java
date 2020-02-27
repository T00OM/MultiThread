package chapter3;


import java.util.concurrent.locks.ReentrantLock;

public class FairLock implements Runnable {
    public static ReentrantLock fairLock = new ReentrantLock(true);
    public void run(){
        while(true){
            try{
                fairLock.lock();
                System.out.println(Thread.currentThread().getId()+"get lock");
            }finally{
                fairLock.unlock();
            }
        }
    }
    public static void main(String[] args) throws InterruptedException{
        FairLock r1 =new FairLock();
        FairLock r2= new FairLock();
        Thread t1 = new Thread(r1,"Thread_t1");
        Thread t2 = new Thread(r2,"thread_t1");
        t1.start();
        t2.start();
    }

}
