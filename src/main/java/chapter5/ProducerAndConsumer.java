package chapter5;

import java.text.MessageFormat;
import java.util.Random;
import java.util.concurrent.*;

public class ProducerAndConsumer {


    private static class Producer implements Runnable {
        public Producer(BlockingQueue<Integer> blockingQueue){
            this.blockingQueue = blockingQueue;
        }
        private BlockingQueue<Integer> blockingQueue;
        private volatile boolean isRunning = true;

        public void stop() {
            isRunning = false;
        }

        @Override
        public void run() {
            Random random = new Random();
            int data = random.nextInt();
            try {
                while (isRunning) {
                    System.out.println(Thread.currentThread().getId() + "put data : " + data);
                    if (!blockingQueue.offer(data, 2, TimeUnit.SECONDS)) {
                        System.err.println("failed to offer data: " + data);
                    }
                    Thread.sleep(400);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class Consumer implements Runnable{

        public Consumer(BlockingQueue<Integer> blockingQueue){
            this.blockingQueue = blockingQueue;
        }

        private BlockingQueue<Integer> blockingQueue;
        private volatile boolean isRunning = true;

        public void stop() {
            isRunning = false;
        }
        @Override
        public void run() {
            try{
                while(isRunning){
                    Integer i = blockingQueue.take();
                    if(null != i){
                        System.out.println(Thread.currentThread().getId() + "take data : " + i);
//                        System.out.println(MessageFormat.format("{0}*{1}={2}", i, i, i * i));
                    }
                    Thread.sleep(1000);
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingQueue<>();
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        Producer producer1 = new Producer(blockingQueue);
        Producer producer2 = new Producer(blockingQueue);
        Producer producer3 = new Producer(blockingQueue);
        Consumer consumer1 = new Consumer(blockingQueue);
        Consumer consumer2 = new Consumer(blockingQueue);
        Consumer consumer3 = new Consumer(blockingQueue);
        executorService.execute(producer1);
        executorService.execute(producer2);
        executorService.execute(producer3);
        executorService.execute(consumer1);
        executorService.execute(consumer2);
        executorService.execute(consumer3);
        Thread.sleep(10 * 1000);
        producer1.stop();
        producer2.stop();
        producer3.stop();
        Thread.sleep(10 * 1000);
        executorService.shutdown();
    }
}
