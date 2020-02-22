package chapter3;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduleExecutorServiceDemo {
    public static void main(String[] args){
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(10);
        // 在同一个线程中表现的时间
        ses.scheduleWithFixedDelay(new Runnable(){
            public void run(){
                try{
                    Thread.sleep(8000);
                    System.out.println(System.currentTimeMillis()/1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
             }
        },0,2, TimeUnit.SECONDS);
    }
}
