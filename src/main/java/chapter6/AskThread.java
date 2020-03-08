package chapter6;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AskThread implements Runnable {

    private CompletableFuture<Integer> re = null;
    public AskThread(CompletableFuture<Integer> re){
        this.re = re;
    }
    @Override
    public void run() {
        try{
            int myRe = 0;
            myRe = re.get() * re.get();
            System.out.println(Thread.currentThread().getName() + " result " + myRe);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws InterruptedException {
        CompletableFuture<Integer> completableFuture = new CompletableFuture<>();
        new Thread(new AskThread(completableFuture)).start();
        Thread.sleep(2000);
        // CompletableFuture可以设置RealData的值
        completableFuture.complete(50);
    }
}
