package chapter2;

/*
在主线程中中断t1线程，中断为发送中断信号，需要被终端线程自己处理
sleep方法会抛出中断异常，处理之后，会清除中断信号
 */
public class InterruptAndSleep {

    public static class MyThread implements Runnable{
        @Override
        public void run(){
            while(true){
                System.out.println("hello");
                System.out.println(Thread.currentThread().isInterrupted());
                if(Thread.currentThread().isInterrupted()){
                    System.out.println("this thread is interrupted");
                    //很显然interrupt方法只是发生中断信号
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    System.out.println("中断");
                    e.printStackTrace();
                    System.out.println(Thread.currentThread().isInterrupted());
                    if(true)
                        break;
                }
            }
        }
    }
    public static void main(String[] args){
        Thread t1 = new Thread(new MyThread());
        t1.start();
        t1.interrupt();
    }
}
