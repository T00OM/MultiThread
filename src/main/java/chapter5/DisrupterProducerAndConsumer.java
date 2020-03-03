package chapter5;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;

public class DisrupterProducerAndConsumer {

    public static void main(String[] args) throws InterruptedException {
        Disruptor<PCData> disruptor = new Disruptor<PCData>(new DataFactory(),
                1024,
                Executors.newCachedThreadPool(), ProducerType.MULTI,
                new BlockingWaitStrategy()
        );
        // 三个消费者
        disruptor.handleEventsWithWorkerPool(new Consumer(), new Consumer(), new Consumer());
        disruptor.start();

        // 一个在主线程的生产者
        RingBuffer<PCData> ringBuffer = disruptor.getRingBuffer();
        Producer producer = new Producer(ringBuffer);
        ByteBuffer byteBuffer = ByteBuffer.allocate(4);
        for(int i = 0; true; i += 2){
            byteBuffer.putInt(0,i);
            producer.pushData(byteBuffer);
            Thread.sleep(1000);
        }
    }

    public static class PCData {
        private int i;

        public int getInt() {
            return i;
        }

        public void setInt(int i) {
            this.i = i;
        }
    }

    public static class Consumer implements WorkHandler<PCData> {
        @Override
        public void onEvent(PCData pcData) throws Exception {
            System.out.println(Thread.currentThread().getId() + " consume : " + pcData.getInt());
        }
    }

    // Disrupter会预先分配空间
    public static class DataFactory implements EventFactory<PCData> {
        @Override
        public PCData newInstance() {
            return new PCData();
        }
    }

    public static class Producer {

        private final RingBuffer<PCData> ringBuffer;

        public Producer(RingBuffer<PCData> ringBuffer) {
            this.ringBuffer = ringBuffer;
        }

        public void pushData(ByteBuffer bb) {
            long sequence = ringBuffer.next();
            try {
                PCData pcData = ringBuffer.get(sequence);
                pcData.setInt(bb.getInt(0));
                System.out.println(Thread.currentThread().getId() + " produce : " + pcData.getInt());
            } finally {
                ringBuffer.publish(sequence);
            }

        }
    }
}
