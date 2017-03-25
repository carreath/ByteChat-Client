package com.carreath.client;

import java.io.*;
import java.util.*;

import javax.swing.JLabel;

public class ClientResponseRunnable implements Runnable {
	BufferedReader input = null;   
    private boolean isRunning = true;
	public JLabel text;
	public JLabel users;
    private LinkedList<String> strings = new LinkedList<String>();



    public ClientResponseRunnable(BufferedReader input, JLabel text, JLabel users) {
        this.input = input;
        this.text = text;
        this.users = users;
    }

    public void run() {
        try {
            int count = 0;
            int tail = 0;
            while(isRunning()) {
                String message = input.readLine();
               
                String texts = "";

                strings.addLast(message + "<br>");
                for(String s : strings) {
                    texts += s;
                }
                if(strings.size() > 100) {
                    strings.removeFirst();
                    strings.removeFirst();
                    strings.removeFirst();
                    strings.removeFirst();
                    strings.removeFirst();
                }

                text.setText("<html>" + texts + "<html>");
            }
        } catch (IOException e) {
            return;
        }
        System.out.println("QUIT");
    }

    public synchronized boolean isRunning() {
        return isRunning;
    }

    public synchronized void close() {
    	isRunning = false;
    }
}