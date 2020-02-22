package chapter3;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterDemo {
//    每秒处理两个请求
    static RateLimiter limiter = RateLimiter.create(2);
    public static class Task implements Runnable{
        public void run(){
            System.out.println(System.currentTimeMillis());
        }
    }
    public static void main(String[] args) throws InterruptedException{
        for(int i=0;i<50;i++){
            limiter.acquire();
//            if(!limiter.tryAcquire())
//                continue;
            new Thread(new Task()).start();
        }
    }
}
