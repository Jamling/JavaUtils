package playground.jcu;

import cn.ieclipse.common.ConsoleReader;

import java.io.IOException;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * <pre>
 * 方法\处理方式	抛出异常	返回特殊值	一直阻塞	超时退出
 * 插入方法	add(e)	offer(e)	put(e)	offer(e,time,unit)
 * 移除方法	remove()	poll()	take()	poll(time,unit)
 * 检查方法	element()	peek()	不可用	不可用
 * </pre>
 *
 * @author Jamling
 */
public class ProducerConsumer implements Runnable {

    /**
     * 消息对象
     */
    private static class Message implements Comparable<Message> {
        public long when;
        public int what;
        public Object obj;
        private Handler target;

        @Override public int compareTo(Message o) {
            System.out.println(this + " compare to " + o);
            return what - o.what;
        }

        @Override public String toString() {
            return String.format("msg(what=%d,when=%d)", what, when);
        }
    }

    /**
     * 消费者，参考Android中的Handler
     */
    private static abstract class Handler {
        /**
         * 消费消息事件
         *
         * @param m 消息
         */
        public abstract void handle(Message m);

        public void send(Message m) {
            m.target = this;
            queue.put(m);
            System.out.println(queue);
        }

        /**
         * 生产消息事件
         *
         * @param what 消息序号
         * @param when 消息执行时间
         */
        public void send(int what, long when) {
            Message m = new Message();
            m.target = this;
            m.when = when;
            m.what = what;
            queue.put(m);
            System.out.println(queue);
        }
    }

    /**
     * 这是一个无界的优先级阻塞列表，生产的消息将添加到些队列中。
     */
    static PriorityBlockingQueue<Message> queue = new PriorityBlockingQueue<>(3);

    @Override public void run() {

        Message m = null;
        try {
            while ((m = queue.take()) != null) {
                if (m != null) {
                    m.target.handle(m);
                }
            }

            System.out.println("Quit");
        } catch (InterruptedException e) {
            System.out.println("interrupted");
        }
    }

    public void quit() {
        // TODO
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new ProducerConsumer());
        thread.start(); // 启动消费者线程

        Handler handler = new Handler() {
            @Override public void handle(Message m) {
                System.out.println(m + " handle start, may cost " + m.when + " second");
                try {
                    Thread.sleep(m.when * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(m + " handle finish");
            }
        };

        // 接收输入后生产消息并添加到消息队列中
        ConsoleReader reader = new ConsoleReader("q", line -> {
            String[] a = line.split(",");
            int what = 0;
            long when = 0L;
            try {
                what = Integer.parseInt(a[0]);
                when = Long.parseLong(a[1]);
                handler.send(what, when);
            } catch (Exception e) {
                System.out.println("wrong format");
            }
        });
        try {
            reader.read();
            thread.interrupt(); // 释放内存和线程
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 测试优先级
        //        PriorityBlockingQueue<Message> queue = new PriorityBlockingQueue<>();
        //        Message m = new Message();
        //        m.what = 2;
        //        queue.put(m);
        //        m = new Message();
        //        m.what = 1;
        //        queue.put(m);
        //        m = new Message();
        //        m.what = 3;
        //        queue.put(m);
        //        System.out.println(queue);
        //        while (queue.peek() != null) {
        //            System.out.println(queue.poll());
        //        }
    }
}
