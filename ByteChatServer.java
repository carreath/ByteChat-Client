/* TCPClient.java from Kurose-Ross */

import java.io.*;
import java.net.*;

class ByteChatServer {
	public static void main(String args[]) {
		ConnectionHandler  server = null;
		try {
			server = new ConnectionHandler (9000);
			new Thread(server).start();
			while(true) {
				Thread.sleep(10000);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("Stopping Server");
			if(server != null) server.stop();
		}
	}
}
