import java.io.*;
import java.net.*;
import java.util.*;

class ByteChatClient {
	public static void main(String args[]) throws Exception {
		final String host = "localhost";
		final int portNumber = 9000;
		Scanner sc = new Scanner(System.in);

		System.out.println("Creating socket to '" + host + "' on port " + portNumber);
		Socket socket = new Socket(host, portNumber);
		DataOutputStream output = new DataOutputStream(socket.getOutputStream());
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		System.out.print("Please Choose a User Name: ");
		String name = sc.nextLine();
		output.writeBytes("<UNAME> " + name + "\n");


		new Thread(new ClientResponseThread(input)).start();

		boolean a = true;
		while(a) {
			String out = sc.nextLine();
			output.writeBytes(out + "\n");
			if(out.equals("END")) break;
		}
		socket.close();
	}
}
