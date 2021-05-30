package cn.ieclipse.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Jamling
 */
public class ConsoleReader {

    private ReaderListener listener;
    /**
     * exit command
     */
    private String exitCmd;

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

    public void listen() {
        try {
            read();
        } catch (IOException e) {
            // do nothing
        }
    }

    /**
     * Listener
     */
    public interface ReaderListener {
        /**
         * notify the content of input
         *
         * @param line content of input
         */
        void read(String line);
    }
}
