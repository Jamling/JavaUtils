package cn.ieclipse.common;

public final class Env {

    public static final String FILE_ENCODING;
    public static final String LF;
    public static final String OS_NAME;
    public static final String OS_ARCH;
    public static final String OS_VERSION;
    public static final String PATH_SEPARATOR;

    static {
        OS_NAME = System.getProperty("os.name");
        OS_ARCH = System.getProperty("os.arch");
        OS_VERSION = System.getProperty("os.version");
        PATH_SEPARATOR = System.getProperty("path.separator");
        FILE_ENCODING = System.getProperty("file.encoding");
        LF = System.getProperty("line.separator");
    }

    public static boolean isWindows() {
        return OS_NAME.toLowerCase().contains("windows");
    }

    public static boolean isLinux() {
        return OS_NAME.toLowerCase().contains("linux");
    }

    public static boolean isOSX() {
        return OS_NAME.toLowerCase().contains("os x");
    }

    public static void dumpEnv() {
        System.out.println("-------------env info------------");
        for (String key : System.getenv().keySet()) {
            System.out.println(key + "=" + System.getenv(key));
        }
    }

    public static void dumpProps() {
        System.out.println("-------------properties info------------");
        for (Object key : System.getProperties().keySet()) {
            System.out.println(key + "=" + System.getProperty((String)key));
        }
    }

}
