package IT114_Project;
import java.net.*;
import java.io.*;
import java.awt.*;

public class TicTacToeServer {
	private int board[];
	   private boolean isMove;
	   private TextArea output;
	   //Player class is the 'Client'
	   private Player players[];
	   private ServerSocket server;
	   private Socket connection;
	   private int numberOfPlayers;
	   private int currentPlayer;

	   public TicTacToeServer()
	   {
		board = new int[9];
		isMove = true;
		players = new Player[2];
		currentPlayer = 0;
		
		// set up ServerSocket
		try
		{	server = new ServerSocket (5000, 2);
		}
		catch ( IOException e)
		{	e.printStackTrace();
			System.exit(1);				}
	   }
	 
	   public void execute()
		// wait for two connections so game can be played
	   {
		
		for ( int i = 0; i < players.length; i++ ) {
			try
			{	
			players[i] = new Player( server.accept(), this, i );
			players[i].start();
			++numberOfPlayers;
			}
			catch (IOException e)
			{	e.printStackTrace(); System.exit(1);	}
		}
	   }

	   public int getNumberOfPlayers()	
	   {	
		   return numberOfPlayers;	
	   }

	  

	   public synchronized boolean validMove (int loc, int player)
	 	// Determine if move is valid. This method is synchronized so that only 
		// one move can be made at a time
	   {   
		boolean moveDone = false;

		while ( player != currentPlayer ) {
			try 
			{	wait();	}
			catch	(InterruptedException e)
			{		}
		}
	   }
}

class Player extends Thread {
	// Player class to manage each Player as a thread
		Socket connection;
		DataInputStream input;
		DataOutputStream output;
		TicTacToeServer control;
		int number;
		char mark;

	  public Player (Socket sock, TicTacToeServer tttServer, int num )
	  {
		mark = ( num == 0 ? 'X' : 'O' );
		connection = sock;

		try
		{
			input = new DataInputStream(connection.getInputStream() );
			output = new DataOutputStream(connection.getOutputStream() );
		}
		catch (IOException e)
			{	e.printStackTrace(); System.exit(1);	}

		control = tttServer;
		number = num;
	   }
}

		

	

