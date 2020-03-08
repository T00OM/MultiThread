package chapter6;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class CompletableFutureStream {

    public static Integer calc(Integer pars) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pars * pars;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(()->calc(40))
                .thenApply((i)->Integer.toString(i))
                .thenApply(new Function<String, String>() {
                    @Override
                    public String apply(String s) {
                        return "\"" + s + "\"";
                    }
                })
                .thenAccept(System.out::println);
        completableFuture.get();
    }
}
