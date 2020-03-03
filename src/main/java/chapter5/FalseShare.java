package chapter5;

public class FalseShare implements Runnable {

    public static VolatileLong[] volatileLongs = new VolatileLong[8];

    static {
        for (int i = 0; i < volatileLongs.length; i++) {
            volatileLongs[i] = new VolatileLong();
        }
    }

    private final int arrayIndex;

    public FalseShare(int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    @Override
    public void run() {
        for(int i = 0; i < 100000000L; i++){
            volatileLongs[arrayIndex].value = i;
        }
    }

    public static class VolatileLong {
        public volatile long value;
        // 在JDK 1.8中，自动优化不使用的字段，添加PADDING失败，时间没有差距
//        private long l1, l2, l3, l4, l5, l6, l7;
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        for(int i = 0; i < 8; i++){
            Thread thread = new Thread(new FalseShare(i));
            thread.start();
            thread.join();
        }
        System.out.println("duration:" + (System.currentTimeMillis() - start));
    }

}
