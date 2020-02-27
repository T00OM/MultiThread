package chapter3;

import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DaemonExecutor {

    /*
        程序确实无法正常退出，应该是线程池没有退出
     */
    public static void main(String[] args) {

        ExecutorService es = Executors.newFixedThreadPool(3);
//      将普通线程池转为Deamon线程池
        MoreExecutors.getExitingExecutorService((ThreadPoolExecutor) es);
        es.execute(()->{
            System.out.println("hello");
        });
//        es.shutdown();
    }
}
