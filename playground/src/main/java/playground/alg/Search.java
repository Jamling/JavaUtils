package playground.alg;

public class Search {
    /**顺序查找平均时间复杂度 O（n）
     * @param searchKey 要查找的值
     * @param array 数组（从这个数组中查找）
     * @return  查找结果（数组的下标位置）
     */
    public static int orderSearch(int searchKey,int[] array){
        if(array==null||array.length<1)
            return -1;
        for(int i=0;i<array.length;i++){
            if(array[i]==searchKey){
                return i;
            }
        }
        return -1;
        
    }
    
    /**
     * 二分查找又称折半查找，它是一种效率较高的查找方法。 【二分查找要求】：1.必须采用顺序存储结构 2.必须按关键字大小有序排列。
     * 
     * @param array
     *            有序数组 *
     * @param searchKey
     *            查找元素 *
     * @return searchKey的数组下标，没找到返回-1
     */
    public static int binarySearch(int[] array, int searchKey) {

        int low = 0;
        int high = array.length - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            if (searchKey == array[middle]) {
                return middle;
            } else if (searchKey < array[middle]) {
                high = middle - 1;
            } else {
                low = middle + 1;
            }
        }
        return -1;
    }
    
    /**
     * 分块查找
     * 
     * @param index
     *            索引表，其中放的是各块的最大值
     * @param st
     *            顺序表，
     * @param key
     *            要查找的值
     * @param m
     *            顺序表中各块的长度相等，为m
     * @return
     */
    public static int blockSearch(int[] index, int[] st, int key, int m) {
        // 在序列st数组中，用分块查找方法查找关键字为key的记录
        // 1.在index[ ] 中折半查找，确定要查找的key属于哪个块中
        int i = binarySearch(index, key);
        if (i >= 0) {
            int j = i > 0 ? i * m : i;
            int len = (i + 1) * m;
            // 在确定的块中用顺序查找方法查找key
            for (int k = j; k < len; k++) {
                if (key == st[k]) {
                    System.out.println("查询成功");
                    return k;
                }
            }
        }
        System.out.println("查找失败");
        return -1;
    }
    
    /**** 
     * Hash查找 
     *  
     * @param hash 
     * @param hashLength 
     * @param key 
     * @return 
     */  
    public static int searchHash(int[] hash, int hashLength, int key) {  
        // 哈希函数  
        int hashAddress = key % hashLength;  
      
        // 指定hashAdrress对应值存在但不是关键值，则用开放寻址法解决  
        while (hash[hashAddress] != 0 && hash[hashAddress] != key) {  
            hashAddress = (++hashAddress) % hashLength;  
        }  
      
        // 查找到了开放单元，表示查找失败  
        if (hash[hashAddress] == 0)  
            return -1;  
        return hashAddress;  
      
    }  
      
    /*** 
     * 数据插入Hash表 
     *  
     * @param hash 
     *            哈希表 
     * @param hashLength 
     * @param data 
     */  
    public static void insertHash(int[] hash, int hashLength, int data) {  
        // 哈希函数  
        int hashAddress = data % hashLength;  
      
        // 如果key存在，则说明已经被别人占用，此时必须解决冲突  
        while (hash[hashAddress] != 0) {  
            // 用开放寻址法找到  
            hashAddress = (++hashAddress) % hashLength;  
        }  
      
        // 将data存入字典中  
        hash[hashAddress] = data;  
    }  
}
