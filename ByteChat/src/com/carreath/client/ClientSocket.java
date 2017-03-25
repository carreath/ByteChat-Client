package com.carreath.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

class ClientSocket extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private URL whatismyip;
	private BufferedReader in;
	DataOutputStream output;
	private boolean isRunning = true;
	
	JTextField input;
	public JLabel text;
	public JLabel users;
	Socket socket;
	ClientResponseRunnable response;
	
	public void initComponents() {
		this.setLayout(new BorderLayout());

		JPanel top = new JPanel();
		JPanel middle = new JPanel(new BorderLayout());
		JPanel middleLeft = new JPanel(new BorderLayout());
		JPanel bottom = new JPanel(new BorderLayout());

		JButton disconnect = new JButton("Disconnect");
		JButton close = new JButton("Close");
		JButton connect0 = new JButton("Connect 0");
		JButton connect1 = new JButton("Connect 1");
		JButton connect2 = new JButton("Connect 2");
		top.add(connect0);
		top.add(connect1);
		top.add(connect2);
		top.add(disconnect);
		top.add(close);
		
		users = new JLabel();
		middleLeft.add(users, BorderLayout.CENTER);
		text = new JLabel();
		text.setHorizontalAlignment(JLabel.LEFT);
		text.setVerticalAlignment(JLabel.BOTTOM);
		middle.add(middleLeft, BorderLayout.LINE_START);
		middle.add(text, BorderLayout.CENTER);
	
		input = new JTextField(); 
		JButton submit = new JButton("Submit");
		bottom.add(input, BorderLayout.CENTER);
		bottom.add(submit, BorderLayout.LINE_END);

		this.add(top, BorderLayout.PAGE_START);
		this.add(middle, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.PAGE_END);
		

		submit.addActionListener(e -> submitMessage(input.getText()));
		connect0.addActionListener(e -> connect(0));
		connect1.addActionListener(e -> connect(1));
		connect2.addActionListener(e -> connect(2));
		disconnect.addActionListener(e -> disconnect());
		close.addActionListener(e -> close());
	}
	
	private void connect(int i) {
		try {
			output.writeBytes("!CONNECT " + i + "\n");
			System.out.println("Attemp-Connect");

		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	private void close() {
		try {
			output.writeBytes("!CLOSE" + "\n");
			isRunning = false;
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	private  void disconnect() {
		try {
			output.writeBytes("!DISCONNECT" + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void submitMessage(String message) {
		try {
			//System.out.println(message);
			output.writeBytes(message + "\n");
			input.setText("");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public ClientSocket() {
		initComponents();
		try {

			whatismyip = new URL("http://checkip.amazonaws.com");
			in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			final String ip = in.readLine(); //you get the IP as a String
			final String serverIP = "159.203.31.239";
			final String connectionHost = (ip.equals(serverIP))? "127.0.0.1": "127.0.0.1"; 
			final int portNumber = 25565;
			
			Scanner sc = new Scanner(System.in);

			System.out.println("Creating socket to '" + connectionHost + "' on port " + portNumber);
			socket = new Socket(connectionHost, portNumber);
			output = new DataOutputStream(socket.getOutputStream());
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			response = new ClientResponseRunnable(input, text, users);
			Thread t = new Thread(response);
			t.start();


			String test1= JOptionPane.showInputDialog("What is your name: ");
			output.writeBytes(test1 + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void onClose() {
		response.close();
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) throws Exception {
		ClientSocket main = new ClientSocket();
		main.setVisible(true);
		main.setSize(700, 500);
		main.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
}
