package playground.jvm;

import org.junit.Assert;
import org.junit.Test;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceTest {

    @Test public void testSoftReference() throws Exception {
        Object object = new Object();
        SoftReference<Object> softReference = new SoftReference<>(object);
        object = null;
        System.gc();

        Thread.sleep(1000);
        Assert.assertNotNull(softReference.get());
    }

    @Test public void testWeakReference() throws Exception {
        Object object = new Object();
        WeakReference<Object> softReference = new WeakReference<>(object);
        object = null;
        System.gc();

        Thread.sleep(1000);
        Assert.assertNull(softReference.get());
    }

    @Test
    public void testPhantomReference() throws Exception {
        Object object = new Object();
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        PhantomReference<Object> phantomReference = new PhantomReference<>(object, queue);
        object = null;
        System.gc();
        Thread.sleep(1000);
        Assert.assertNotNull(queue.poll());
        Assert.assertNull(phantomReference.get());
    }
}