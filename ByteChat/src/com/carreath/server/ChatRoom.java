package com.carreath.server; 

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ChatRoom implements Runnable{
    private LinkedList<User> users = new LinkedList<User>();
    private final int MAX_READ = 10;

    //UserConnection management thread
    public void run() {
        try {
            //Loops while the current user is still connected
        	while(true) {
        		LinkedList<User> s_users = (LinkedList<User>) getUsers().clone();
        		for(User user : s_users) {  
	            	//read input from the user
        			if(!user.getInput()) disconnect(user);
	                LinkedList<String> buffer = user.getBuffer();

	                int readLength = (buffer.size()<MAX_READ)? buffer.size() : MAX_READ;
	                for(int i=0; i<readLength; i++) {
	                	String message = buffer.pollFirst();
	                	
		                //if user wishes to close ByteChat they type !CLOSE
		                if(message.contains("!CLOSE")) {
		                    close(user);
		                }
		                //if user wishes to change rooms they type !DISCONNECT
		                else if(message.contains("!DISCONNECT")) {
		                    disconnect(user);
		                }
		                else {		
		                	System.out.println(message);
			                for(User user2 : s_users) {
			                	if(user != user2) user2.writeBytes(message);
			                }
		                }
		            }
	            }
        	}
        } catch (IOException e) {
        	e.printStackTrace();
        }
    }

    private synchronized LinkedList<User> getUsers() {
    	return users;
    }
    
    public synchronized boolean connect(User user) {
    	try {
			user.writeBytes("Welcome");
			for(User user2 : users) {
            	user2.writeBytes("### '" + user.getName() + "' Has Connected.");
            }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return users.add(user);
    }
    
    private synchronized void close(User user) {
        try {      
            users.remove(user);            
            for(User user2 : users) {
            	user2.writeBytes(user.getName() + " Has Left The Lobby");
            }
            
            //these are no longer needed since the buffer is all that remains necessary
            user.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private synchronized void disconnect(User user) {
        try {  
            users.remove(user);
            
            for(User user2 : users) {
            	user2.writeBytes(user.getName() + " Has Left The Lobby");
            }
            
            user.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}