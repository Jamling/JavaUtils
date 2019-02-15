package cn.ieclipse.common;

import java.util.Random;

/**
 * @author Jamling
 * 
 */
public class Radix {
    public final static int HEX_BIN_WIDTH = 4;
    public final static int BYTE_BIN_WIDTH = 8;
    
    /**
     * bin string to integer.<br />
     * 0110 is 6
     * 
     * @param bin
     *            binary string
     * @return integer value of binary string.
     */
    public static int bin2Int(String bin) throws ConvertException {
        if (bin == null) {
            return 0;
        }
        
        int res = 0;
        if (bin.length() > 32) {
            throw new ConvertException(
                    "unsupported int binary string,length must less than 32 but actual was "
                            + bin.length());
        }
        char[] ch = bin.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (c == '1') {
                res += 1 << (ch.length - i - 1);
            }
            else if (c == '0') {
                
            }
            else {
                throw new ConvertException("invalid binary char : '" + c
                        + "' index : " + i);
            }
        }
        return res;
    }
    
    /**
     * hex string to integer.<br />
     * sample: hex2Int("61") = 97;
     * 
     * @param hex
     *            hex string
     * @return integer value
     */
    public static int hex2Int(String hex) {
        if (hex == null) {
            return 0;
        }
        int res = 0;
        char[] ch = hex.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            int decChar = 0;
            if (c >= '0' && c <= '9') {
                decChar = c - '0';
            }
            else if (c >= 'a' && c <= 'f') {
                decChar = c - 87;// 'a'(97) - 87 = 10
            }
            else if (c >= 'A' && c <= 'F') {
                decChar = c - 55;// 'A'(65) - 65 = 10
            }
            res += decChar << ((ch.length - i - 1) << 2);
        }
        return res;
    }
    
    /**
     * hex string to binary string.<br />
     * sample : AF = 10101111
     * 
     * @param hex
     *            hex string
     * @return binary string
     */
    public static String hex2Bin(String hex) {
        if (hex == null) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder(hex.length() << 4);
        char[] ch = hex.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            byte b = getHexChar(ch[i]);
            sb.append(getFixWidthString(Integer.toBinaryString(b),
                    HEX_BIN_WIDTH));
        }
        return sb.toString();
    }
    
    /**
     * convert binary string to hex string
     * 
     * @param bin
     *            binary string
     * @return hex string
     */
    public static String bin2Hex(String bin) {
        if (bin == null) {
            return "";
        }
        if (bin.length() % BYTE_BIN_WIDTH != 0) {
            throw new ConvertException("wrong binary string length");
        }
        StringBuilder tmp = new StringBuilder();
        int k = 0;
        for (int i = 0; i < bin.length(); i += HEX_BIN_WIDTH) {
            k = 0;
            for (int j = 0; j < HEX_BIN_WIDTH; j++) {
                String s = bin.substring(i + j, i + j + 1);
                if ("0".equals(s)) {
                    
                }
                else if ("1".equals(s)) {
                    k += (1 << HEX_BIN_WIDTH - j - 1);
                }
                else {
                    throw new ConvertException(
                            "invalid char of binary string char = " + s
                                    + " index = " + (i + j));
                }
            }
            System.out.println(k);
            tmp.append(getFixWidthString(Integer.toHexString(k), 2));
        }
        return tmp.toString();
    }
    
    /**
     * from CCBUtil
     * 
     * @param src
     * @return
     */
    public static String bin2Ascii(byte[] src) {
        int len, i;
        byte tb;
        char high, tmp, low;
        String result = new String();
        len = src.length;
        for (i = 0; i < len; i++) {
            tb = src[i];
            
            tmp = (char) ((tb >>> 4) & 0x000f);
            if (tmp >= 10)
                high = (char) ('a' + tmp - 10);
            else
                high = (char) ('0' + tmp);
            result += high;
            tmp = (char) (tb & 0x000f);
            if (tmp >= 10)
                low = (char) ('a' + tmp - 10);
            else
                low = (char) ('0' + tmp);
            
            result += low;
        }
        return result;
    }
    
    private static byte getHexChar(char c) {
        int decChar = 0;
        if (c >= '0' && c <= '9') {
            decChar = c - '0';
        }
        else if (c >= 'a' && c <= 'f') {
            decChar = c - 87;// 'a'(97) - 87 = 10
        }
        else if (c >= 'A' && c <= 'F') {
            decChar = c - 55;// 'A'(65) - 65 = 10
        }
        else {
            // invalid hex char , ignore
        }
        return (byte) decChar;
    }
    
    private static String getFixWidthString(String str, int width) {
        if (str.length() == width) {
            return str;
        }
        StringBuilder sb = new StringBuilder(width);
        for (int i = 0; i < width - str.length(); i++) {
            sb.append('0');
        }
        sb.append(str);
        return sb.toString();
    }
    
    public static String str2Unicode(String str) {
        StringBuilder sb = new StringBuilder();
        char[] ch = str.toCharArray();
        for (char c : ch) {
            sb.append("\\u");
            sb.append(Integer.toHexString(c));
        }
        return sb.toString();
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        int dec = new Random().nextInt();
        System.out.println(dec + "\t" + bin2Int(Integer.toBinaryString(dec)));
        System.out.println(dec + "\t" + hex2Int(Integer.toHexString(dec)));
        System.out.println(hex2Bin("0F"));
        System.out.println(str2Unicode("简体中文Abc"));
        System.out.println(bin2Hex("11010011"));
        System.out.println(bin2Ascii("d3".getBytes()));
    }
    
    public static final class ConvertException extends RuntimeException {
        private static final long serialVersionUID = 1581731008947272135L;
        
        public ConvertException() {
            super();
        }
        
        public ConvertException(String msg) {
            super(msg);
        }
        
        public ConvertException(String msg, Throwable t) {
            super(msg, t);
        }
        
        public ConvertException(Throwable t) {
            super(t);
        }
    }
}
