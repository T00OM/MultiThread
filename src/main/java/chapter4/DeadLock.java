package chapter4;

public class DeadLock implements Runnable{

    private Object object;
    static Object fork1 = new Object();
    static Object fork2 = new Object();

    public DeadLock(Object object){
        this.object = object;
        if(object == fork1)
            Thread.currentThread().setName("哲学家A");
        else if(object == fork2)
            Thread.currentThread().setName("哲学家B");
    }

    @Override
    public void run() {
        if(object == fork1){
            synchronized (fork1){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (fork2){
                }
            }
        }else{
            synchronized (fork2){
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                synchronized (fork1){
                }
            }
        }
    }

    public static void main(String[] args) {
        new Thread(new DeadLock(fork1)).start();
        new Thread(new DeadLock(fork2)).start();
    }
}
