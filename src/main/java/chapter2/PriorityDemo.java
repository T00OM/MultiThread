package chapter2;

public class PriorityDemo {

    public static class HighPriority implements Runnable{
        static int count = 0;
        @Override
        public void run(){
            while(true){
                synchronized(PriorityDemo.class){
                    count++;
                    System.out.println("high finish");
                    if(count>50){
                        break;
                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public static class LowPriority implements Runnable {
        static int count = 0;
        @Override
        public void run() {
            while (true) {
                synchronized (PriorityDemo.class) {
                    count++;
                    System.out.println("low finish");
                    if (count > 50) {
                        break;
                    }
                }
            }
        }
    }
    public static void main(String[] args){
        Thread t1 = new Thread(new HighPriority());
        Thread t2 = new Thread(new LowPriority());
        t1.setPriority(Thread.MAX_PRIORITY);
        t2.setPriority(Thread.MIN_PRIORITY);
        t1.start();
        t2.start();
    }
}
