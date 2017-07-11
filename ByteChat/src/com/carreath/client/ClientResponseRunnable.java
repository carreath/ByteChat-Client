package com.carreath.client; import java.io.*;

public class ClientResponseRunnable implements Runnable {
	private BufferedReader input = null;

    public ClientResponseRunnable(BufferedReader input) {
        this.input = input;
    }

    public void run() {
        try {
            while(true) {
                String message = input.readLine();
                System.out.println(message);
            }
        } catch (IOException e) {
            System.out.println(e.getStackTrace());
            return;
        }
    }

    public void close() {
    	return;
    }
}