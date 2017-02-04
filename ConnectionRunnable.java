import java.io.InputStream;
import java.io.OutputStream;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class ConnectionRunnable implements Runnable{
    private LinkedList<DataOutputStream> outputs = null;
    private LinkedList<String> users = null;
    private DataOutputStream myOutStream = null;

    protected Socket clientSocket = null;
    protected String serverText   = null;

    private String name = "";

    public ConnectionRunnable(Socket clientSocket, String serverText, LinkedList<DataOutputStream> outputs, LinkedList<String> users) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
        this.outputs = outputs;
        this.users = users;
    }

    public void run() {
        try {
            DataOutputStream output = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            outputs.add(output);
            while(true) {
                String message = input.readLine();
                if(message.equals("END")) {
                    outputs.remove(output);
                    clientSocket = null;
                    message = name + ": Has Left The Channel";
                    for(int i=0; i<outputs.size(); i++) {
                        outputs.get(i).writeBytes(">> " + message + "\n"); 
                    }
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
            e.printStackTrace();
        }
        System.out.println("User: " + name + " Disconnected");
    }
}