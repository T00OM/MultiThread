package chapter6;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureException {
    public static Integer calc(Integer i) {
        return i / 0;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFuture<Void> completableFuture = CompletableFuture.supplyAsync(()->calc(40))
                .exceptionally(ex->{ex.printStackTrace();return 0;})
                .thenApply((i)->Integer.toString(i))
                .thenAccept(System.out::println);
        completableFuture.get();
    }
}
