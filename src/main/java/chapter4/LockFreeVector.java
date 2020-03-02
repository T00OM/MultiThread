package chapter4;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/**
 * 这个程序总体来说并不难
 * 出现的问题还是在于没有及时反应过来JAVA的引用传递。
 */
public class LockFreeVector<E> {

    // 表示二维数据
    private AtomicReferenceArray<AtomicReferenceArray<E>> buckets;
    // 描述二维数据
    private AtomicReference<Descripter<E>> descripter;

    public LockFreeVector() {
        buckets = new AtomicReferenceArray<AtomicReferenceArray<E>>(30);
        descripter = new AtomicReference<Descripter<E>>(new Descripter<E>(null, 0, 0));
        buckets.set(0, new AtomicReferenceArray<E>(8));
    }

    public static void main(String[] args) throws InterruptedException {
        LockFreeVector<Integer> lockFreeVector = new LockFreeVector<>();
        CountDownLatch countDownLatch = new CountDownLatch(20);

        ExecutorService executorService = new ThreadPoolExecutor(20, 20, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 20; i++) {
            executorService.execute(() -> {
                for (int j = 0; j < 8; j++) {
                    lockFreeVector.push_back(j);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        lockFreeVector.show();
    }

    public void push_back(E e) {
        Descripter<E> presentDescripter;
        Descripter<E> newDescripter = null;

        do {
            presentDescripter = descripter.get();
            presentDescripter.operate();
            int index = presentDescripter.index;
            int pos = presentDescripter.pos;

            // 判断是否应该扩容
            if (presentDescripter.pos == 8 << presentDescripter.index) {
                index++;
                pos=0;
                //扩容
                buckets.compareAndSet(presentDescripter.index + 1, null, new AtomicReferenceArray<E>(8 << (presentDescripter.index + 1)));
            }
            AtomicReferenceArray<E> af = buckets.get(index);
            newDescripter = new Descripter<E>(new WriteDescripter<E>(af, pos, null, e),
                    index, pos + 1
            );
        } while (!descripter.compareAndSet(presentDescripter, newDescripter));
        descripter.get().operate();
    }

    public void show() {
        Descripter<E> presentDescripter = descripter.get();
        for (int i = 0; i <= presentDescripter.index; i++) {
            for (int j = 0; j < 8 << i; j++) {
                System.out.print(buckets.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }

    private class Descripter<E> {

        public int index;
        public int pos;
        volatile WriteDescripter<E> writeDescripter;

        public Descripter(WriteDescripter<E> writeDescripter, int index, int pos) {
            this.writeDescripter = writeDescripter;
            this.index = index;
            this.pos = pos;
        }

        public void operate() {
            WriteDescripter<E> temp = writeDescripter;
            if (writeDescripter != null) {
                temp.operate();
                writeDescripter = null;
            }
        }
    }

    private class WriteDescripter<E> {
        public AtomicReferenceArray<E> atomicReferenceArray;
        public int index;
        public E expectValue;
        public E newValue;

        public WriteDescripter(AtomicReferenceArray atomicReferenceArray, int index, E expectValue, E newValue) {
            this.atomicReferenceArray = atomicReferenceArray;
            this.index = index;
            this.expectValue = expectValue;
            this.newValue = newValue;
        }

        public void operate() {
            atomicReferenceArray.compareAndSet(index, expectValue, newValue);
        }
    }
}
