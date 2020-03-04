package chapter5;

import java.util.concurrent.*;

public class MyFutureTask {
    public static class RealData implements Callable<String> {
        private String data;

        @Override
        public String call() {
            data = "real data";
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return data;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(new RealData());
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.submit(futureTask);

        // futureTask.get() 会等待结果
        System.out.println("data: " + futureTask.get());
    }
}
