package playground.alg;

import java.util.Arrays;
import java.util.Collections;

/**
 * 
 * 
 * https://www.cnblogs.com/guoyaohua/p/8600214.html
 * 
 * @author Jamling
 * @date 2019年2月21日
 *       
 */
public class Sort {
    /**
     * 冒泡排序
     * <p>
     * 冒泡排序是一种简单的排序算法。它重复地走访过要排序的数列，一次比较两个元素，如果它们的顺序错误就把它们交换过来。
     * 走访数列的工作是重复地进行直到没有再需要交换，也就是说该数列已经排序完成。这个算法的名字由来是因为越小的元素会经由交换慢慢“浮”到数列的顶端。
     * </p>
     * 
     * <pre>
     * 比较相邻的元素。如果第一个比第二个大，就交换他们两个。  
     * 对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对。在这一点，最后的元素应该会是最大的数。  
     * 针对所有的元素重复以上的步骤，除了最后一个。
     * 持续每次对越来越少的元素重复上面的步骤，直到没有任何一对数字需要比较。
     * </pre>
     * <p>
     * 最佳情况：T(n) = O(n) 最差情况：T(n) = O(n2) 平均情况：T(n) = O(n2)
     * </p>
     * 
     * @param numbers
     *            需要排序的整型数组
     */
    public static void bubbleSort(int[] numbers) {
        int temp = 0;
        int size = numbers.length;
        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size - 1 - i; j++) {
                if (numbers[j] > numbers[j + 1]) // 交换两数位置
                {
                    temp = numbers[j];
                    numbers[j] = numbers[j + 1];
                    numbers[j + 1] = temp;
                }
            }
        }
    }
    
    /**
     * 选择排序算法
     * 
     * <pre>
     * 在未排序序列中找到最小元素，存放到排序序列的起始位置  
     * 再从剩余未排序元素中继续寻找最小元素，然后放到排序序列末尾。 
     * 以此类推，直到所有元素均排序完毕。
     * </pre>
     * <p>
     * 最佳情况：T(n) = O(n2) 最差情况：T(n) = O(n2) 平均情况：T(n) = O(n2)
     * </p>
     * 
     * @param numbers
     */
    public static int[] selectionSort(int[] array) {
        if (array.length == 0)
            return array;
        for (int i = 0; i < array.length; i++) {
            int minIndex = i;
            for (int j = i; j < array.length; j++) {
                if (array[j] < array[minIndex]) // 找到最小的数
                    minIndex = j; // 将最小数的索引保存
            }
            int temp = array[minIndex];
            array[minIndex] = array[i];
            array[i] = temp;
        }
        return array;
    }
    
    /**
     * 插入排序
     * 
     * <pre>
     * 从第一个元素开始，该元素可以认为已经被排序
     * 取出下一个元素，在已经排序的元素序列中从后向前扫描 
     * 如果该元素（已排序）大于新元素，将该元素移到下一位置  
     * 重复步骤3，直到找到已排序的元素小于或者等于新元素的位置  
     * 将新元素插入到该位置中  
     * 重复步骤2
     * </pre>
     * 
     * @param numbers
     *            待排序数组
     */
    public static int[] insertionSort(int[] array) {
        if (array.length == 0)
            return array;
        int current;
        for (int i = 0; i < array.length - 1; i++) {
            current = array[i + 1];
            int preIndex = i;
            while (preIndex >= 0 && current < array[preIndex]) {
                array[preIndex + 1] = array[preIndex];
                preIndex--;
            }
            array[preIndex + 1] = current;
        }
        return array;
    }
    
    /**
     * 不同的函数支持的排序算法不一样，需要深入源码查看
     * 
     * <pre>
     * 1，使用遗留的归并排序
     * 1.1 当数组长度< Arrays.INSERTIONSORT_THRESHOLD (7)时，使用插入排序
     * </pre>
     */
    public static void jdkArraySort() {
        // 如果java.util.Arrays.useLegacyMergeSort为true，使用归并排序
        // 否则使用TimSort;
    }
    
    public static void jdkCollectionSort() {
        Collections.sort(null);
    }
}
