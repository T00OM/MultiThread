package chapter4;

import java.util.concurrent.atomic.AtomicReference;

public class TestAtomicReference {
    static AtomicReference<Integer> money = new AtomicReference<>();
    public static void main(String[] args){
        money.set(19);
        for(int i=0;i<3;i++){
            new Thread(){
                public void run(){
                    while(true){
                        while(true){
                            Integer m  = money.get();
                            if(m < 20){
                                if(money.compareAndSet(m,m+20)){
                                    System.out.println("<20,success,still have:"+money.get());
                                    break;
                                }
                            }else{
                                break;
                            }
                        }
                    }
                }
            }.start();
            new Thread(){
                public void run(){
                    for(int i=0;i<100;i++){
                        while(true){
                            Integer m = money.get();
                            if(m>10){
                                System.out.println(">10");
                                if(money.compareAndSet(m,m-10)){
                                    System.out.println("success use 10, still have:"+money.get());
                                    break;
                                }
                            }else{
                                System.out.println("no enough money");
                            }
                        }
                    }
                    try{
                        sleep(100);
                    }catch(InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
