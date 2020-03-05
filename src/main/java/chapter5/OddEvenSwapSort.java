package chapter5;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OddEvenSwapSort {

    public static int[] array = {1, 23, 4, 1, 3, 1, 3, 4, 22, 26};
    public static boolean exchangeFlag = true;
    public static synchronized void setExchangeFlag(boolean flag){
        exchangeFlag = flag;
    }
    public static synchronized boolean getExchangeFlag(){
        return exchangeFlag;
    }

    /*
        使用多线程进行数据交换
     */
    public static class OddEventSwapTask implements Runnable {
        private int i;
        private CountDownLatch latch;
        public OddEventSwapTask(int i, CountDownLatch latch){
            this.i = i;
            this.latch = latch;
        }
        @Override
        public void run() {
            if(array[i] > array[i+1]) {
                int temp = array[i];
                array[i] = array[i+1];
                array[i+1] = temp;
                setExchangeFlag(true);
            }
            latch.countDown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int start = 0;
        ExecutorService executorService = Executors.newCachedThreadPool();
        while(getExchangeFlag() || start == 1){
            setExchangeFlag(false);
            CountDownLatch latch = new CountDownLatch(array.length/2-(array.length%2==0?start:0));
            for(int i = start; i + 1 < array.length; i++) {
                executorService.submit(new OddEventSwapTask(i, latch));
            }
            latch.await();
            if(start == 0)
                start = 1;
            else
                start = 0;
        }
        executorService.shutdown();
        System.out.println(Arrays.toString(array));
    }
}
