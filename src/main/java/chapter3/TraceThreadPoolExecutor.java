package chapter3;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TraceThreadPoolExecutor extends ThreadPoolExecutor {
    public static class DivTask implements Runnable{
        int a,b;
        public DivTask(int a,int b){
            this.a=a;
            this.b=b;
        }
        public void run(){
           double re=a/b;
           System.out.println(re);
        }
    }
    public TraceThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
                                   long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue){
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }
    public void execute(Runnable task){
        super.execute(wrap(task,clientTrack(),Thread.currentThread().getName()));
    }
    public Exception clientTrack(){
        return new Exception("client stack trace");
    }
    public Runnable wrap(final Runnable task,final Exception clientTrace,String name){
        return new Runnable(){
            public void run(){
                try{
                    task.run();
                }catch(Exception e){
                    clientTrace.printStackTrace();
                    e.printStackTrace();
                }
            }
        };
    }
    public static void main(String[] args){
        ThreadPoolExecutor pools = new TraceThreadPoolExecutor(
                0,Integer.MAX_VALUE,
                0L,TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>()
        );
        for(int i=0;i<5;i++)
            pools.execute(new DivTask(100,i));
    }
}
