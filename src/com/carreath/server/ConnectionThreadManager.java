package com.carreath.server;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.io.*;
import java.util.*;

public class ConnectionThreadManager implements Runnable{
	public long lastConnection = System.currentTimeMillis();

    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;

    private LinkedList<DataOutputStream> outputs = new LinkedList<DataOutputStream>();
    private LinkedList<String> users = new LinkedList<String>();

    public ConnectionThreadManager (int port){
        this.serverPort = port;
    }

    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
            	System.out.println("Waiting for connection");
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException(
                    "Error accepting client connection", e);
            }
            new Thread(new ConnectionRunnable(clientSocket, "Multithreaded Server", outputs, users)).start();
            lastConnection = System.currentTimeMillis();
        }
        System.out.println("Server Stopped.") ;
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }

}