package playground.tool;

import java.util.*;

public class ArrayAndList {
    int[] counts = new int[3];

    // [1,2,3]
    Collection collection;
    List<Integer> countList = new ArrayList<>(12);
    LinkedList linkedList; // 链表
    ArrayList arrayList; // 不需要更改数据

    Vector vector; // 线程安全
    StringBuilder stringBuilder; // 线程不安全
    StringBuffer stringBuffer;// 线程安全

    TreeSet treeSet; //
    HashMap hashSet; //


    // {a:1, b:2 }

    Map map;
    TreeMap treeMap;
    HashMap hashMap;
    Hashtable hashtable;// 线程安全， kv不可以为Null

    public static void main(String[] args) {

    }
}
