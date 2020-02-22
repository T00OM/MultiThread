package chapter2;

/*
这里使用wait和notify实现suspend和resume功能
suspend和resume出现问题的主要原因在于不释放锁，导致其他线程不能进入临界区
wait和notify可以避免这个问题

原本suspend和resume只需要依赖于线程，不需要依赖额外的一个对象。
*/
public class GoodSuspend {
    public static Object u = new Object();
    public static class ChangeObjectThread extends Thread{
        volatile boolean suspendme = false;
        public void suspendMe(){
            suspendme = true;
        }
        public void resumeMe(){
            suspendme = false;
            synchronized(this){
                notify();
            }
        }
        public void run(){
            while(true){
                synchronized (this) {
                    while (suspendme) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                synchronized (u){
                    System.out.println("in ChangeObjectThread");
                }
                Thread.yield();
            }
        }
        public static class ReadObjectThread extends Thread{
            public void run(){
                while(true){
                    synchronized (u){
                        System.out.println("in ReadObjectThread");
                    }
                }
            }
        }
        public static void main(String[] args) throws InterruptedException{
            ChangeObjectThread t1 = new ChangeObjectThread();
            ReadObjectThread t2 = new ReadObjectThread();
            t1.start();
            t2.start();
            Thread.sleep(1000);
            t1.suspendMe();
            System.out.println("suspend t1 2 sec");
            Thread.sleep(2000);
            System.out.println("resume t1");
            t1.resumeMe();
        }

    }
}
