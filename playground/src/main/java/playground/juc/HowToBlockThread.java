package playground.juc;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.LockSupport;

public class HowToBlockThread {
    private Object lock = new Object();
    
    private Thread thread1 = new Thread() {
        public void run() {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("mm:ss");
            
            try {
                synchronized (lock) {
                    lock.wait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            for (int i = 0; i < 5; i++) {
                System.out.println(df.format(LocalTime.now()) + " thread run "
                        + (i + 1) + " times");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    };
    
    private Thread thread2 = new Thread() {
        public void run() {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("mm:ss");
            
            LockSupport.park();
            
            for (int i = 0; i < 5; i++) {
                System.out.println(df.format(LocalTime.now()) + " thread run "
                        + (i + 1) + " times");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
    };
    
    public void blockWithOld() {
        System.out.println("block with wait/notify");
        thread1.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        synchronized (lock) {
            lock.notify();
        }
    }
    
    public void blockWithJdk5() {
        System.out.println("block with LockSupport.park");
        thread2.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        LockSupport.unpark(thread2);
    }
    
    public static void main(String[] args) throws Exception {
        HowToBlockThread how = new HowToBlockThread();
        /* may be always blocked when lock.notify called first */
        // how.blockWithOld();
        how.blockWithJdk5(); //
    }
    
}
