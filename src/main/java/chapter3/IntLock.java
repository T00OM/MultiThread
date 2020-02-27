package chapter3;

import java.util.concurrent.locks.ReentrantLock;

/*
    对线程使用t2.interrupt方法，发送一个中断信号。由系统捕获到异常后，需要对这个信号做出什么响应本应由客户
    实现。
 */
public class IntLock implements Runnable {
    public static ReentrantLock lock1 = new ReentrantLock();
    public static ReentrantLock lock2 = new ReentrantLock();
    int lock;
    public IntLock(int lock){
        this.lock = lock;
    }
    public void run(){
        try{
            if(lock ==1){
                lock1.lockInterruptibly();
                try{
                    Thread.sleep(500);
                }catch(InterruptedException e){
                }
                lock2.lockInterruptibly();
            }else{
                lock2.lockInterruptibly();
                try{
                    Thread.sleep(500);
                }catch(InterruptedException e){
                }
                lock1.lockInterruptibly();
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally{
            if(lock1.isHeldByCurrentThread())
                lock1.unlock();
            if(lock2.isHeldByCurrentThread())
                lock2.unlock();
            System.out.println(Thread.currentThread().getId()+":线程退出");
        }
    }
    public static void main(String[] args) throws InterruptedException{
        IntLock r1= new IntLock(1);
        IntLock r2= new IntLock(2);
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
        Thread.sleep(1000);
        t2.interrupt();
    }
}
