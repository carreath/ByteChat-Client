package com.carreath.client; 

import java.io.*;
import java.net.*;
import java.util.*;

class ClientSocket {
	public static void main(String args[]) throws Exception {
		URL whatismyip = new URL("http://checkip.amazonaws.com");
		BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

		final String ip = in.readLine(); //you get the IP as a String
		final String serverIP = (args.length > 0)? args[0]: "127.0.0.1";
		
		int port;
		try {
			port = Integer.parseInt(args[1]);
		}
		catch(Exception e) {
			port = 25565;
		}

		final int portNumber = port;
		final String connectionHost = (ip.equals(serverIP))? "127.0.0.1": serverIP; 
		
		Scanner sc = new Scanner(System.in);

		System.out.println("Creating socket to '" + connectionHost + "' on port " + portNumber);
		Socket socket = new Socket(connectionHost, portNumber);
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		System.out.print("Please Choose a User Name: ");
		String name = sc.nextLine();
		output.writeBytes("<UNAME> " + name + "\n");


		new Thread(new ClientResponseRunnable(input)).start();

		boolean a = true;
		while(a) {
			String out = sc.nextLine();
			output.writeBytes(out + "\n");
			if(out.equals("END")) break;
		}
		socket.close();
	}
}
