package playground.jcu;

public class HowToStopThread {

    Thread thread1 = new Thread() {
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    Thread.sleep(1000);
                    System.out.println("Thread 1 runing");
                }
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    // interupted
                }
            }
        };
    };

    /**
     * <pre>
     * 1. 即刻抛出ThreadDeath异常，在线程的run()方法内，任何一点都有可能抛出ThreadDeath Error，包括在catch或finally语句中。
       2. 释放该线程所持有的所有的锁
     * </pre>
     */
    public void stopWrong() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        thread1.stop();
    }

    Thread thread2 = new Thread() {
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    Thread.sleep(1000);
                    System.out.println("Thread 2 runing");
                }
            } catch (Exception e) {
                if (e instanceof InterruptedException) {
                    // interupted
                }
            }
        };
    };
    private volatile Thread need2Stop = null;

    public void stopRight() {
        Thread tmp = need2Stop;
        need2Stop = null;
        if (tmp != null) {
            thread2.interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // TODO Auto-generated method stub
        HowToStopThread how = new HowToStopThread();
        how.thread1.start();
        
        Thread.sleep(3000);
        how.stopWrong();
        
        how.thread2.start();
        how.need2Stop = how.thread2;
        
        Thread.sleep(3000);
        how.stopRight();
    }

}
