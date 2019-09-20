package cn.ieclipse.common;

import java.nio.charset.Charset;
import java.util.Random;

import cn.ieclipse.util.StringUtils;

/** @author Jamling */
public class Radix {
    public static final int HEX_BIN_WIDTH = 4;
    public static final int BYTE_BIN_WIDTH = 8;
    public static final String HEX_CHAR = "0123456789abcdef";

    /**
     * bin string to integer.<br>
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
                "unsupported int binary string,length must less than 32 but actual was " + bin.length());
        }
        char[] ch = bin.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (c == '1') {
                res += 1 << (ch.length - i - 1);
            } else if (c == '0') {

            } else {
                throw new ConvertException("invalid binary char : '" + c + "' index : " + i);
            }
        }
        return res;
    }

    /**
     * hex string to integer.<br>
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
            } else if (c >= 'a' && c <= 'f') {
                decChar = c - 87; // 'a'(97) - 87 = 10
            } else if (c >= 'A' && c <= 'F') {
                decChar = c - 55; // 'A'(65) - 65 = 10
            }
            res += decChar << ((ch.length - i - 1) << 2);
        }
        return res;
    }

    /**
     * hex string to binary string.<br>
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
            sb.append(getFixWidthString(Integer.toBinaryString(b), HEX_BIN_WIDTH));
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

                } else if ("1".equals(s)) {
                    k += (1 << HEX_BIN_WIDTH - j - 1);
                } else {
                    throw new ConvertException("invalid char of binary string char = " + s + " index = " + (i + j));
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
    public static String byte2Hex(byte[] src) {
        // int len, i;
        // byte tb;
        // char high, tmp, low;
        // String result = new String();
        // len = src.length;
        // for (i = 0; i < len; i++) {
        // tb = src[i];
        //
        // tmp = (char) ((tb >>> 4) & 0x000f);
        // if (tmp >= 10)
        // high = (char) ('a' + tmp - 10);
        // else
        // high = (char) ('0' + tmp);
        // result += high;
        // tmp = (char) (tb & 0x000f);
        // if (tmp >= 10)
        // low = (char) ('a' + tmp - 10);
        // else
        // low = (char) ('0' + tmp);
        //
        // result += low;
        // }
        // return result;
        if (src == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder(src.length * 2);
        byte b;
        for (int i = 0; i < src.length; i++) {
            b = src[i];
            sb.append(HEX_CHAR.charAt((b >>> 4) & 0xf));
            sb.append(HEX_CHAR.charAt(b & 0xf));
        }
        return sb.toString();
    }

    public static String byte2Hex(byte[] src, String separator) {
        if (separator == null || separator.length() == 0) {
            return byte2Hex(src);
        }
        StringBuilder sb = new StringBuilder(src.length * 2 + (src.length - 1) * separator.length());
        byte b;
        for (int i = 0; i < src.length; i++) {
            if (sb.length() > 0) {
                sb.append(separator);
            }
            b = src[i];
            sb.append(HEX_CHAR.charAt((b >>> 4) & 0xf));
            sb.append(HEX_CHAR.charAt(b & 0xf));
        }
        return sb.toString();
    }

    public static String byte2Hex(byte b) {
        int h = ((b >>> 4) & 0xf);
        int l = (b & 0xf);
        return new String(new char[] {HEX_CHAR.charAt(h), HEX_CHAR.charAt(l)});
    }

    public static byte[] hex2byte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte)(getHexChar(achar[pos]) << 4 | getHexChar(achar[pos + 1]));
        }
        return result;
    }

    private static byte getHexChar(char c) {
        int decChar = 0;
        if (c >= '0' && c <= '9') {
            decChar = c - '0';
        } else if (c >= 'a' && c <= 'f') {
            decChar = c - 87; // 'a'(97) - 87 = 10
        } else if (c >= 'A' && c <= 'F') {
            decChar = c - 55; // 'A'(65) - 65 = 10
        } else {
            // invalid hex char , ignore
        }
        return (byte)decChar;
    }

    private static String getFixWidthString(String str, int width) {
        return StringUtils.getFixWidthString(str, width);
    }

    /**
     * Integer to {@link java.nio.ByteOrder#LITTLE_ENDIAN} bytes
     *
     * @param n
     *            int
     * @return byte[]
     */
    public static byte[] toLittleEndian(int n) {
        byte[] b = new byte[4];
        b[0] = (byte)(n & 0xff);
        b[1] = (byte)(n >> 8 & 0xff);
        b[2] = (byte)(n >> 16 & 0xff);
        b[3] = (byte)(n >> 24 & 0xff);
        return b;
    }

    public static byte[] toBigEndian(int integer) {
        int byteNum = (40 - Integer.numberOfLeadingZeros(integer < 0 ? ~integer : integer)) / 8;
        byte[] byteArray = new byte[4];

        for (int n = 0; n < byteNum; n++)
            byteArray[3 - n] = (byte)(integer >>> (n * 8));

        return (byteArray);
    }

    /**
     * 将C中的8位无符号整数转为Java中的整数
     *
     * @param data
     * @return
     */
    public static int fromINT8U(byte data) {
        return data & 0x0FF;
    }

    public static int fromINT16U(short data) {
        return data & 0x0FFFF;
    }

    public static long fromINT32U(int data) {
        return data & 0x0FFFFFFFF;
    }

    public static byte toINT8U(int i) {
        return (byte)(i & 0x0FF);
    }

    public static short toINT16U(int i) {
        return (short)(i & 0x0FFFF);
    }

    public static int toINT32U(int i) {
        return (int)(i & 0x0FFFFFFFF);
    }

    public static String fromCbytes(byte[] data) {
        return Radix.fromCbytes(data, Charset.defaultCharset());
    }

    public static String fromCbytes(byte[] data, Charset charset) {
        int len = data.length;
        for (int i = 0; i < data.length; i++) {
            if (data[i] == 0) {
                len--;
            }
        }
        return new String(data, 0, len, charset);
    }

    public static byte[] toCbytes(String s, Charset charset, int max) {
        byte[] b = charset == null ? s.getBytes() : s.getBytes(charset);
        if (max > 0) {
            byte[] dst = new byte[max];
            if (b.length > 0) {
                System.arraycopy(b, 0, dst, 0, Math.min(max, b.length));
            }
            return dst;
        }

        return b;
    }

    public static byte BCD84212byte(byte b) {
        byte ret = 0;

        return ret;
    }

    /**
     * Short to {@link java.nio.ByteOrder#LITTLE_ENDIAN} bytes
     *
     * @param n
     *            int
     * @return byte[]
     */
    public static byte[] toLittleEndian(short n) {
        byte[] b = new byte[2];
        b[0] = (byte)(n & 0xff);
        b[1] = (byte)(n >> 8 & 0xff);
        return b;
    }

    public static String fromPercent(float p, int radix) {
        int v = (int)((radix * radix - 1) * p);
        return Integer.toHexString(v / radix) + Integer.toHexString(v % radix);
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
