package com.carreath.server; 

import java.io.*;
import java.net.Socket;
import java.util.*;

public class User {
	public boolean isConnected;
	
	protected Socket clientSocket = null;

    private LinkedList<String> buffer = new LinkedList<String>();
    LinkedList<ChatRoom> rooms;
    
    private String name = "";
    private DataOutputStream output = null;
    private BufferedReader input = null;

    public User(Socket clientSocket, LinkedList<ChatRoom> rooms) {
        this.clientSocket = clientSocket;
        isConnected = true;
        this.rooms = rooms;
        
        try {
	    	//Get in and outs for the users handled by this thread
	        output = new DataOutputStream(this.clientSocket.getOutputStream());
	        input = new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));

			name = input.readLine();
			System.out.println("Got Name " + name);
        } catch (IOException e) {
	        isConnected = false;
	    }
        
        waitForConnection();
    }

    public void waitForConnection() {
		try {
			
	    	while(true) {
	    		String message = input.readLine();
	    		if(message.contains("!CONNECT")) {
	    			int roomNum = Integer.parseInt(message.substring(9));
	    			if(rooms.get(roomNum) != null) {
	    				if(connect(rooms.get(roomNum))) break;
	    			}
	    		}
	    	}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				close();
			} catch (Exception e1) {}
		}
    }
    
    public void setRooms(LinkedList<ChatRoom> rooms) {
    	this.rooms = rooms;
    }
    
    public void disconnect() {
    	waitForConnection();
    }
    
    public boolean connect(ChatRoom room) {
    	return room.connect(this);
    }
    
    public String getName() {
    	return name;
    }
    public void close() throws IOException {
    	output = null;
    	input = null;
    	clientSocket = null;
    }
    public void writeBytes(String message) throws IOException {
    	output.writeBytes(message + "\n");
    }
   
    //UserConnection management thread
    public boolean getInput() {
        try {
        	if(clientSocket != null) {
        		while(input.ready()) {
	        		//read input from the user
	                String message = input.readLine();
	                
	                writeBytes("YOU :" + message);
	                message = name + ": " + message;
	                buffer.addLast(message);
        		}
        	}
        } catch (IOException e) {
            isConnected = false;
            return false;
        }
        return true;
    }
    
    public LinkedList<String> getBuffer() {
    	return buffer;
    }
}