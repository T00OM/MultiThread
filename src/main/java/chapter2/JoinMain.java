package chapter2;

public class JoinMain {

    public volatile static int i = 0;
    public static class AddThread extends Thread{
        public void run(){
            for(i=0;i<100000;i++);
        }
    }
    public static void main(String[] args) throws InterruptedException{
        AddThread addThread = new AddThread();
        addThread.start();
        //主线程等待addThread线程完成后再继续
        //addThread.join();
        System.out.println(i);
    }
}
