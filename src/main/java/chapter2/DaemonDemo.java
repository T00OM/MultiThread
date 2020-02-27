package chapter2;

/*
一个java应用中只有守护线程时，java虚拟机会自动退出
 */
public class DaemonDemo{

    public static class MyThread implements Runnable{
        @Override
        public void run(){
            while(true){
                System.out.println("i am alive");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static void main(String[] args){
       Thread t1 = new Thread(new MyThread());
       t1.setDaemon(true);
       t1.start();
       try {
           Thread.sleep(200);
       } catch (InterruptedException e) {
           e.printStackTrace();
       }
    }
}
