/*
 * Copyright 2014-2018 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package playground.jcu;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 
 *
 */
public class CountDownLatchTest {
    public void test(final int count) {
        CountDownLatch latch = new CountDownLatch(count);
        Thread thread = new Thread() {
            @Override
            public void run() {
                int c = count + 3;
                for (int i = 0; i < c; i++) {
                    Thread t = new Thread() {
                        public void run() {
                            try {
                                int t = new Random().nextInt(5) + 1;
                                Thread.sleep(t * 1000);
                                System.out.println("thread "
                                        + Thread.currentThread().getName()
                                        + " finished ");
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            } finally {
                                latch.countDown();
                            }
                            System.out.println(
                                    "the latch count is " + latch.getCount());
                        };
                    };
                    t.start();
                }
            }
        };
        thread.start();
        try {
            latch.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("latch count is 0, block state exited");
    }
    
    public static void main(String[] args) {
        CountDownLatchTest t = new CountDownLatchTest();
        t.test(2);
    }
}
