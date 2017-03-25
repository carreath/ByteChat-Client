package com.carreath.server;

import java.util.LinkedList;
import java.util.Scanner;

class ServerManager {
	public static void main(String args[]) {
		ServerThread server = null;
		LinkedList<ChatRoom> rooms = new LinkedList<ChatRoom>();

		int port = 25565;
		try {
			port = Integer.parseInt(args[0]);
		}
		catch(Exception e) {
			System.out.println("Using Default Port: " + port);
		}
		
		try {			
			ChatRoom room = new ChatRoom ();
			Thread t = new Thread(room);
			t.start();
			rooms.add(room);
			
			room = new ChatRoom ();
			t = new Thread(room);
			t.start();
			rooms.add(room);
			
			room = new ChatRoom ();
			t = new Thread(room);
			t.start();
			rooms.add(room);
			
			server = new ServerThread (port, rooms);
			Thread t2 = new Thread(server);
			t2.start();
			
			Scanner sc = new Scanner(System.in);
			while(true) {
				String command = sc.nextLine();
				if(command.equals("QUIT")) {
					sc.close();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Stopping Server");
			if(server != null) server.stop();
		}
	}
}
