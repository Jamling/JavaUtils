package cn.ieclipse.common.tool;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStore.Entry;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.List;

/**
 * @author Jamling
 * 
 */
public class KeyTool {
    private String storeFile = "";
    private String storePass = "";
    private KeyStore store;
    
    public KeyTool(String file, String pass) throws NoSuchAlgorithmException,
            CertificateException, IOException, KeyStoreException {
        this.storeFile = file;
        this.storePass = pass;
        
        FileInputStream in = new FileInputStream(storeFile);
        this.store = KeyStore.getInstance("JKS");
        this.store.load(in, storePass.toCharArray());
        in.close();
    }
    
    public KeyTool(String pass) throws KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException {
        this.storePass = pass;
        this.store = KeyStore.getInstance("JKS");
        this.store.load(null,
                storePass == null ? null : storePass.toCharArray());
    }
    
    public void save(String path, String password) throws IOException,
            KeyStoreException, NoSuchAlgorithmException, CertificateException {
        FileOutputStream output = new FileOutputStream(path);
        if (password != null) {
            this.store.store(output, password.toCharArray());
        }
        else {
            this.store.store(output, this.storePass.toCharArray());
        }
        output.close();
    }
    
    public static Provider[] getProviders() {
        Provider[] ps = Security.getProviders();
        return ps;
    }
    
    public static X509Certificate readCertificate(String file)
            throws CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        FileInputStream in = new FileInputStream(file);
        Certificate c = cf.generateCertificate(in);
        in.close();
        if (c instanceof X509Certificate) {
            return (X509Certificate) c;
        }
        return null;
    }
    
    public void readKeystore() throws Exception {
    }
    
    public List<String> getAliases() throws KeyStoreException {
        List<String> list = new ArrayList<String>();
        Enumeration<String> aliases = store.aliases();
        while (aliases.hasMoreElements()) {
            list.add(aliases.nextElement());
        }
        return list;
    }
    
    public Certificate getCertificate(String alias) throws KeyStoreException {
        return this.store.getCertificate(alias);
    }
    
    public Key getKey(String alias, String password)
            throws UnrecoverableKeyException, KeyStoreException,
            NoSuchAlgorithmException {
        return store.getKey(alias, password.toCharArray());
    }
    
    public Entry getEntry(String alias, String password)
            throws NoSuchAlgorithmException, UnrecoverableEntryException,
            KeyStoreException {
        KeyStore.PasswordProtection p = new KeyStore.PasswordProtection(
                password.toCharArray());
        Entry e = this.store.getEntry(alias, p);
        return e;
    }
    
    public void deleteEntry(String alias) throws KeyStoreException {
        this.store.deleteEntry(alias);
    }
    
    // public void setEntry() {
    // KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
    // gen.initialize(1024);
    // KeyPair pair = gen.genKeyPair();
    // X509Certificate c = X509CertImpl.
    //
    //
    // KeyStore.PrivateKeyEntry entry = new KeyStore.PrivateKeyEntry(
    // pair.getPrivate(), chain);
    //
    // }
    
    public void gen(String keytool, long days, String alias, String password) {
        ProcessBuilder pb = new ProcessBuilder("cmd ", "/k",
                "start", // cmd Shell命令
                "keytool",
                "-genkey", // -genkey表示生成密钥
                "-validity", // -validity指定证书有效期(单位：天)，这里是36000天
                "36500",
                "-keysize",// 指定密钥长度
                "1024",
                "-alias", // -alias指定别名，这里是ss
                "ss",
                "-keyalg", // -keyalg 指定密钥的算法 (如 RSA DSA（如果不指定默认采用DSA）)
                "RSA",
                "-keystore", // -keystore指定存储位置，这里是d:/demo.keystore
                "d:/demo.keystore",
                "-dname",// CN=(名字与姓氏), OU=(组织单位名称), O=(组织名称), L=(城市或区域名称),
                         // ST=(州或省份名称), C=(单位的两字母国家代码)"
                "CN=(SS), OU=(SS), O=(SS), L=(BJ), ST=(BJ), C=(CN)",
                "-storepass", // 指定密钥库的密码(获取keystore信息所需的密码)
                "123456", "-keypass",// 指定别名条目的密码(私钥的密码)
                "123456", "-v"// -v 显示密钥库中的证书详细信息 )
        );
    }
    
    public PrivateKey getPrivateKey(String alias, String password)
            throws Exception {
        Entry e = getEntry(alias, password);
        if (e instanceof PrivateKeyEntry) {
            return ((PrivateKeyEntry) e).getPrivateKey();
        }
        return null;
    }
    
    public KeyPair getKeyPair(String alias, String password)
            throws UnrecoverableKeyException, KeyStoreException,
            NoSuchAlgorithmException {
        Key key = getKey(alias, password);
        if (key instanceof PrivateKey) {
            PublicKey pubKey = getPublicKey(alias);
            return new KeyPair(pubKey, (PrivateKey) key);
        }
        return null;
    }
    
    public PublicKey getPublicKey(String alias) throws KeyStoreException {
        Certificate c = getCertificate(alias);
        return c.getPublicKey();
    }
    
    public void exportBase64(String alias, String password, String path)
            throws Exception {
        PrivateKey privateKey = getPrivateKey(alias, password);
        String encoded = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        FileWriter fw = new FileWriter(path);
        fw.write("—–BEGIN PRIVATE KEY—–/n");
        fw.write(encoded);
        fw.write("/n");
        fw.write("—–END PRIVATE KEY—–");
        fw.close();
    }
    
    public void exportCert(String alias, String password, String path)
            throws UnrecoverableKeyException, KeyStoreException,
            NoSuchAlgorithmException, IOException {
        KeyPair pair = getKeyPair(alias, password);
        if (pair != null) {
            try {
                ObjectOutputStream fos = new ObjectOutputStream(
                        new FileOutputStream(path));
                fos.writeObject(pair.getPrivate());
                fos.writeObject(getCertificate(alias));
                fos.close();
            } finally {
                
            }
        }
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        KeyTool tool = new KeyTool("ieclipse.keystore", "storepass");
        
        System.out.println("alias:" + tool.getAliases());
        Key key = tool.getKey("pde_alias", "keypass");
        PrivateKey pk = tool.getPrivateKey("pde_alias", "keypass");
        PublicKey pubKey = tool.getPublicKey("pde_alias");
        Entry entry = tool.getEntry("pde_alias", "keypass");
        System.out.println(entry.getClass());
        // System.out.println(Base64.encode(pk.getEncoded()));
        // System.out.println(entry);
        System.out.println(entry);
        
        X509Certificate cer = KeyTool.readCertificate("a.cer");
        
        byte[] sn = cer.getSerialNumber().toByteArray();
        System.out.println(bin2hex(sn));
        byte[] finger = cer.getEncoded();
        // finger =
        // MessageDigest.getInstance(cer.getSigAlgName()).digest(finger);
        // System.out.println(cer);
        System.out.println(bin2hex(finger));
        tool.exportCert("pde_alias", "keypass", "c:/b.cert");
        
        KeyTool t1 = new KeyTool("123");
        t1.save("2.ks", null);
        t1 = new KeyTool("2.ks", "123");
        System.out.println(t1.getAliases());
        
    }
    
    /*
     * 
     * MD5 : DE:76:16:F8:D1:E3:41:8E:CF:C9:E2:7D:9A:FA:BF:5C
     * SHA1:50:DE:B1:DF:F3:D9:DA:D0:53:3E:8B:4C:D5:BD:16:6F:EC:ED:2F:5F
     */
    public static String bin2hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length << 1);
        for (byte b : bytes) {
            sb.append(Integer.toHexString(b >> 4 & 0x0f));
            sb.append(Integer.toHexString(b & 0x0f));
            sb.append(" ");
        }
        return sb.toString();
    }
}
