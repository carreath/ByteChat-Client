package com.carreath.server; 

import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.io.*;
import java.util.*;

public class ServerThread implements Runnable {	
    protected int          serverPort   = 25565;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    
    private LinkedList<ChatRoom> rooms;

    //Constructor accepts server port otherwise it defaults to 25565
    public ServerThread (int port, LinkedList<ChatRoom> rooms){
        this.serverPort = port;
        this.rooms = rooms;
    }

    //Run method manages the open socket and creates client threads when needed
    public void run(){
        synchronized(this){
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while(!isStopped()){
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
            new User(clientSocket, rooms).setRooms(rooms);
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
    		URL whatismyip = new URL("http://checkip.amazonaws.com");
    		BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
    		final String ip = in.readLine(); //you get the IP as a String
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
        	e.printStackTrace();
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }

}