package cn.ieclipse.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jamling
 *         
 */
public class ProcessUtils {
    
    public static Process exec(String program, List<String> args) {
        return ProcessUtils.exec(program, args, null);
    }
    
    public static Process exec(String program, List<String> args,
            File directory) {
        ProcessBuilder builder = new ProcessBuilder();
        String cmd = program;
        File f = new File(program);
        if (f.exists() || cmd.indexOf(' ') >= 0) {
            builder.directory(f.getParentFile());
            cmd = String.format("\"%s\"", f.getName());
        }
        if (directory != null && directory.exists()) {
            builder.directory(directory);
        }
        if (args != null) {
            List<String> list = new ArrayList<>(args.size() + 1);
            list.add(0, cmd);
            list.addAll(args);
            builder.command(list);
        }
        else {
            builder.command(cmd);
        }
        try {
            Process p = builder.start();
            return p;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Process exec(String program, String... args) {
        if (args != null) {
            List<String> array = new ArrayList<String>(args.length);
            for (int i = 0; i < args.length; i++) {
                array.add(args[i]);
            }
            return exec(program, array);
        }
        return exec(program, (List<String>) null);
    }
    
    public static Process exec(String program) {
        return exec(program, (List<String>) null);
    }
    
    public static ExecResult getResult(Process p, String charset) {
        if (charset == null) {
            charset = System.getProperty("sun.jnu.encoding");
        }
        ExecResult ret = new ExecResult();
        try {
            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(p.getInputStream(), charset));
            String s = null;
            while ((s = buffer.readLine()) != null) {
                ret.add(s);
            }
            buffer.close();
            ret.exitValue = p.exitValue();
        } catch (Exception e) {
            // Ignore read errors; they mean the process is done.
        }
        return ret;
    }
    
    public static int handleProcess(Process p, String charset,
            ProcessHandler handler) {
        try {
            if (charset == null) {
                charset = System.getProperty("sun.jnu.encoding");
            }
            new PrintStream(p.getInputStream(), charset, handler).start();
            new PrintStream(p.getErrorStream(), charset, handler).setType(1).start();
            return p.waitFor();
        } catch (Exception e) {
            // Ignore read errors; they mean the process is done.
        }
        return -1;
    }
    
    public interface ProcessHandler {
        void info(String line);
        
        void error(String line);
    }
    
    public static class ExecResult {
        private List<String> results = new ArrayList<>();
        public int exitValue;
        
        public boolean isSuccess() {
            return exitValue == 0;
        }
        
        void add(String line) {
            results.add(line);
        }
        
        public List<String> getResults() {
            return results;
        }
        
        public void setExitValue(int exitValue) {
            this.exitValue = exitValue;
        }
    }
    
    public static class PrintStream extends Thread {
        ProcessHandler handler;
        InputStream in;
        String charset;
        private int type = 0;
        
        public PrintStream(InputStream in, String charset,
                ProcessHandler handler) {
            super();
            this.handler = handler;
            this.in = in;
            this.charset = charset;
        }
        
        public PrintStream setType(int type) {
			this.type = type;
			return this;
		}
        
        @Override
        public void run() {
            try {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(in, charset));
                String s = null;
                
                while ((s = br.readLine()) != null) {
                    if (type == 0) {
                        handler.info(s);
                	} else {
                	    handler.error(s);
                	}
                }
                br.close();
            } catch (Exception e) {
                handler.error(e.toString());
            }
        }
    }
}
