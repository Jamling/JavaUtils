package cn.ieclipse.common;

public class NameValue<V> {
    private String name;
    private V value;
    
    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @param value
     *            the value to set
     */
    public void setValue(V value) {
        this.value = value;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return the value
     */
    public V getValue() {
        return value;
    }
}
