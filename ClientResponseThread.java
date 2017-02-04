import java.io.*;
import java.net.Socket;

public class ClientResponseThread implements Runnable {
	BufferedReader input = null;

    public ClientResponseThread(BufferedReader input) {
        this.input = input;
    }

    public void run() {
        try {
            long time = System.currentTimeMillis();
            while(true) {
                String message = input.readLine();
                System.out.println("> " + message);
            }
        } catch (IOException e) {
            return;
        }
    }

    public void close() {
    	return;
    }
}