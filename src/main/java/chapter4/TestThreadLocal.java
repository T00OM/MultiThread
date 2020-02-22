package chapter4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestThreadLocal {

//    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final ThreadLocal<SimpleDateFormat> t1 = new ThreadLocal<SimpleDateFormat>();
    public static class ParseDate implements Runnable{
        int i = 0;
        public ParseDate(int i){
            this.i = i;
        }
        public void run(){
           try{
//               Date t = sdf.parse("2015-03-29 19:29:" + i % 60);
               if(t1.get()==null) {
                   t1.set(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//                   将会打印十行，说明在线程池中并行运行的十个线程会设置十个format对象,并且十个线程都不会退出,
//                   其他的九十个线程可复用之前设置的十个format对象。
                   System.out.println("create");
               }
               Date t = t1.get().parse("2015-03-29 19:29:" + i % 60);
               System.out.println(i + ":" + t);
           }catch(ParseException e){
               e.printStackTrace();
           }
        }
    }
    public static void main(String[] args){
        ExecutorService es = Executors.newFixedThreadPool(10);
        for(int i=0;i<100;i++){
            es.execute(new ParseDate(i));
        }
    }
}

