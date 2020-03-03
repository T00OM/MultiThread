package chapter5;

/**
 * 通过设计模式完成Future，异步
 */
public class MyFuture {
    public static class Client {
        public Data request() {
            FutureData data = new FutureData();
            /*
                耗时操作在新线程中完成
                这也是非阻塞的关键
             */
            new Thread(()->{
                RealData realData = new RealData();
                data.setData(realData);
            }).start();
            return data;
        }
    }
    public interface Data {
        Data getResult();
    }
    /*
        在构造方法中休眠
        模拟耗时操作
     */
    public static class RealData implements Data {
        public String string;

        public RealData(){
            try {
                string = "true data";
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Data getResult() {
            return new RealData();
        }
    }
    /*
        包装RealData
        需要传入RealData
     */
    public static class FutureData implements Data {
        private RealData realData = null;
        private volatile boolean flag = false;

        public synchronized void setData(RealData realData){
            if(flag)
                return;
            flag = true;
            this.realData = realData;
            notifyAll();
        }

        @Override
        public synchronized Data getResult() {
            if(!flag) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return realData;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        FutureData data = (FutureData)client.request();
        System.out.println(data.realData == null ? "null" : data.realData.string);
        Thread.sleep(2000);
        System.out.println(data.realData.string);
    }
}
