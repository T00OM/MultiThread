package chapter2;

public class BadSuspend {
    public static Object u = new Object();
    static ChangeObjectThread t1 = new ChangeObjectThread("t1");
    static ChangeObjectThread t2 = new ChangeObjectThread("t2");
    public static class ChangeObjectThread extends Thread{
        public ChangeObjectThread(String name){
            super.setName(name);
        }
        public void run(){
            synchronized (u){
                System.out.println("in "+getName());
                Thread.currentThread().suspend();
            }
        }
    }
    //    如果有正确的运行顺序，则该程序能够正常退出
    public static void main(String[] args) throws InterruptedException{
       t1.start();
       Thread.sleep(100);
       /*
            t1挂起但是并不释放锁，t2阻塞。主线程执行两个线程的resume操作。但这个时候t2并没有挂起
            所以t2的resume操作发生在suspend之前
        */
       t2.start();
       t1.resume();
       t2.resume();
       t1.join();
       t2.join();
    }
}
