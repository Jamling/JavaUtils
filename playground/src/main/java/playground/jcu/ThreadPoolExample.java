package playground.jcu;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author jamling
 * @date 2019/12/04
 */
public class ThreadPoolExample {

    public void test1() throws Exception {
        // section 1: java.util.concurrent.Executor;
        /**点击方法，进入声明，会发现 {@link ThreadPoolExecutor} 这个类用来创建各种线程池*/
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Runnable runnable = new Runnable() {

            @Override public void run() {
                int t = 1 + new Random().nextInt(9);

                System.out.println("Run the runnable will cost " + t + " seconds");

                try {
                    Thread.sleep(t * 1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println("Run the runnable finished");
            }
        };
        executorService.execute(runnable);

        Callable<String> callback = new Callable<String>() {

            @Override public String call() throws Exception {
                int t = 1 + new Random().nextInt(9);

                System.out.println("Run the callback will cost " + t + " seconds");

                Thread.sleep(t * 1000);
                System.out.println("Run the callback finished");
                return "Down";
            }
        };
        Future<String> task = executorService.submit(callback);

        Thread.sleep(6000);
        System.out.println("callback finished ? " + task.isDone());
        if (!task.isDone()) {
            task.cancel(true);
        } else {
            String result = task.get();
            System.out.println(result);
        }
        executorService.shutdown();
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        ThreadPoolExample example = new ThreadPoolExample();
        example.test1();
    }

}
