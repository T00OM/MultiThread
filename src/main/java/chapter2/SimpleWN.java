package chapter2;
public class SimpleWN {
    final static Object object = new Object();
    public class T1 extends Thread{
        public void run(){
            synchronized(object){
                System.out.println(System.currentTimeMillis()+":t1 start!");
                try{
                    System.out.println(System.currentTimeMillis()+":t1 wait for object");
                    object.wait();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis()+":t1 end!");
            }
        }
    }
    public static class T2 extends Thread{
        public void run(){
            synchronized (object){
                System.out.println(System.currentTimeMillis()+":t2 start! notify one thread");
                object.notify();
                System.out.println(System.currentTimeMillis()+":t2 end!");
                try{
                    //证明只有在t2释放锁后，t1重新获得锁后，才能正常执行
                    //并不是在被通知（notify）之后立即执行后面的代码
                    Thread.sleep(1000);
                }catch(InterruptedException e){
                }
            }
        }
    }
    public static void main(String[] args){
        //如果t2线程先获得锁，那t1线程会永远处于wait状态（先notify再wait）
        Thread t1 = new SimpleWN().new T1();
        Thread t2 = new T2();
        t1.start();
        t2.start();
    }
}
