package com.example.sockets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SampleSocketServer {
	
	public void start(int port)
	{
		//Steps to create socket, accept connection, and setup input/output "channels"
		try(ServerSocket serverSocket = new ServerSocket(port);
			Socket clientSocket = serverSocket.accept();
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));)
		{
			System.out.println("Client connected, waiting for message");
			String fromClient = "", toClient = "";
			while((fromClient = in.readLine()) != null)
			{
				System.out.println("Message from client " + fromClient);
				if("kill server".equalsIgnoreCase(fromClient))
				{
					System.out.println("CLient killed server process");
					break;
				}
				else 
				{
					System.out.println("From client: " + fromClient);
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			try
			{
				System.out.println("Closing server socket");
			}
			catch(Exception e)
			{
					e.printStackTrace();
			}
		}
		
	}
	public static void main(String [] args)
	{
		System.out.println("Starting server");
		SampleSocketServer server = new SampleSocketServer();
		server.start(3001);
		System.out.println("Server stopped");
	}

}
