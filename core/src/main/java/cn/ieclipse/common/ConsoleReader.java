package cn.ieclipse.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Jamling
 * 
 */
public class ConsoleReader {

    private ReaderListener listener;
    private String exitCmd; // exit command

    public ConsoleReader(String exitCmd, ReaderListener listener) {
        this.exitCmd = exitCmd;
        this.listener = listener;
    }

    public void read() throws IOException {
        // wrap stdin to BufferedReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        // read command from stding until exitcmd is written
        String cmd = reader.readLine();
        while (cmd != null && !cmd.equalsIgnoreCase(exitCmd)) {
            if (listener != null) {
                listener.read(cmd);
            }
            cmd = reader.readLine();
        }
        reader.close();
    }

    public static interface ReaderListener {
        void read(String line);
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        ConsoleReader reader = new ConsoleReader("quit", new ReaderListener() {

            @Override
            public void read(String line) {
                System.out.println("read : " + line);
            }
        });
        try {
            reader.read();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
}
