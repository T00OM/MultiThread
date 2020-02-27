package chapter2;

public class BadLockOnInteger implements Runnable{
    public static Integer i = 0;
    static BadLockOnInteger badLockOnInteger = new BadLockOnInteger();
    public void run(){
        for(int j=0;j<10000;j++){
            synchronized (badLockOnInteger){
                i++;
                System.out.println("test");
            }
        }
    }
    public static void main(String[] args) throws InterruptedException{
        Thread t1 = new Thread(badLockOnInteger);
        Thread t2 = new Thread(badLockOnInteger);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(i);
    }
}
