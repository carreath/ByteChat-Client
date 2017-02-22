
package com.carreath.server; import java.io.*;
import java.net.Socket;
import java.util.*;

public class ConnectionRunnable implements Runnable{
    private LinkedList<DataOutputStream> outputs = null;
    private LinkedList<String> users = null;

    protected Socket clientSocket = null;
    protected String serverText   = null;

    private String name = "";
    private DataOutputStream output = null;
    private BufferedReader input = null;

    public ConnectionRunnable(Socket clientSocket, String serverText, LinkedList<DataOutputStream> outputs, LinkedList<String> users) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
        this.outputs = outputs;
        this.users = users;
    }

    public void run() {
        try {
            output = new DataOutputStream(clientSocket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            outputs.add(output);
            while(clientSocket != null) {
                String message = input.readLine();
                if(message.equals("END")) {
                    disconnect();
                    break;
                }

                message = name + ": " + message;
                if(message.contains("<UNAME>")) {
                    name = message.substring(10);
                    while(users.contains(name.toUpperCase())) {
                        output.writeBytes("Username Already Taken\n");
                        name = input.readLine();
                    }
                    users.add(name.toUpperCase());
                    message = "User: " + name + " has joined the channel";
                } 

                for(int i=0; i<outputs.size(); i++) {
                    outputs.get(i).writeBytes(">> " + message + "\n"); 
                }     
            }
        } catch (IOException e) {
            disconnect();
        }
        System.out.println("User: " + name + " Disconnected");
    }

    private void disconnect() {
        try {
            outputs.remove(output);
            users.remove(name.toUpperCase());        
            String message = name + ": Has Left The Channel";
            for(int i=0; i<outputs.size(); i++) {
                outputs.get(i).writeBytes(">> " + message + "\n"); 
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}