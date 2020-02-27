package chapter4;

import java.util.concurrent.atomic.AtomicStampedReference;

public class AtomicStampedReferenceDemo {

    static AtomicStampedReference<Integer> money = new AtomicStampedReference<>(19,0);
    public static void main(String[] args){
        for(int i=0;i<3;i++){
            final int timestamp = money.getStamp();
            System.out.println("money:"+money.getStamp());
            new Thread(){
                public void run(){
                    while(true){
                        while(true){
                            Integer m = money.getReference();
                            if(m<20){
                                if(money.compareAndSet(m,m+20,timestamp,timestamp+1)){
                                    System.out.println("<20,success,"+money.getReference());
                                    break;
                                }
                            }else
                                break;
                        }
                    }
                }
            }.start();
            new Thread(){
                public void run(){
                    for(int i=0;i<100;i++){
                        while(true){
                            int timestamp = money.getStamp();
                            Integer m = money.getReference();
                            if(m>10){
                                System.out.println(">10");
                                if(money.compareAndSet(m,m-10,timestamp,timestamp+1)){
                                    System.out.println("success use 10"+money.getReference());
                                    break;
                                }
                            }else{
                                System.out.println("no encough");
                                break;
                            }
                        }
                        try{
                            Thread.sleep(100);
                        }catch(InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }
    }
}
