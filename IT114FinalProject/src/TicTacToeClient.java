import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;


public final class TicTacToeClient extends JFrame implements Runnable {

    private final JTextField playerMark; 
    private final JTextArea outputArea; 
    private final JPanel boardPanel; 
    private final JPanel panel2; 
    private final Square[][] gameBoard; 
    private Square thisSquare; 
    private Socket connection; 
    private Scanner inputText; 
    private Formatter outputText; 
    private final String TTTHost; 
    private String myPlay; 
    private boolean isTurn; 
    private final String X = "X"; 
    private final String O = "O"; 

    // set up user-interface and board
    public TicTacToeClient(String host) {
        TTTHost = host; 
        outputArea = new JTextArea(4, 30); 
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.SOUTH);

        boardPanel = new JPanel(); 
        boardPanel.setLayout(new GridLayout(3, 3));
        gameBoard = new Square[3][3]; 

       
        for (int row = 0; row < gameBoard.length; row++) {
            for (int column = 0; column < gameBoard[row].length; column++) {
                gameBoard[row][column] = new Square(" ", row * 3 + column);
                boardPanel.add(gameBoard[row][column]);        
            }
        }

        playerMark = new JTextField(); 
        playerMark.setEditable(false);
        add(playerMark, BorderLayout.NORTH);

        panel2 = new JPanel(); 
        panel2.add(boardPanel, BorderLayout.CENTER); 
        add(panel2, BorderLayout.CENTER); 

        setSize(300, 225); 
        setVisible(true); 

        clientConnection();
    }

    public void clientConnection() {
        try {
            connection = new Socket(InetAddress.getByName(TTTHost), 54721);

            inputText = new Scanner(connection.getInputStream());
            outputText = new Formatter(connection.getOutputStream());
        } catch (IOException ioException) {
            System.out.println(ioException.toString());
        }

        ExecutorService worker = Executors.newFixedThreadPool(1);
        worker.execute(this); 
    }


    @Override
    public void run() {
        myPlay = inputText.nextLine(); 

        isTurn = (myPlay.equals(X)); 

        while (true) {
            if (inputText.hasNextLine()) {
                displayMessages(inputText.nextLine());
            }
        }
    }
    
    private void showMessage(final String message) {
        outputArea.append(message); 
    }


    private void setPlay(final Square squareToPlay, final String play) {
        squareToPlay.setMark(play); 
	}

    private void displayMessages(String message) {
        switch (message) {
            case "Valid move.":
                showMessage("Valid move, please wait for your next turn.\n");
                setPlay(thisSquare, myPlay); 
                break;
            case "Invalid move, try again":
                showMessage(message + "\n");
                isTurn = true; 
                break;
            case "Opponent moved":
                int location = inputText.nextInt(); 
                inputText.nextLine(); 
                int row = location / 3; 
                int column = location % 3; 
                setPlay(gameBoard[row][column],
                        (myPlay.equals(X) ? O : X));   
                showMessage("Opponent moved. Play your turn.\n");
                isTurn = true; 
                break;
            case "DEFEAT":
            case "TIE":
            case "VICTORY":
                showMessage(message + "\n"); 
                isTurn = false;
                break;
            default:
                showMessage(message + "\n"); 
                break;
        }
    }

    public void ClickedSquare(int location) {
        // if it is my turn
        if (isTurn) {
            outputText.format("%d\n", location); 
            outputText.flush();
            isTurn = false; 
        }
    }

    
    public void currentSquare(Square square) {
        thisSquare = square; 
    }

    
    private class Square extends JPanel {

        private String play; 
        private final int location; 

        public Square(String squareMark, int squareLocation) {
            play = squareMark; 
            location = squareLocation; 

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    currentSquare(Square.this); 

                    
                    ClickedSquare(getSquareLocation());
                }
            });
        }

      
        public int getSquareLocation() {
            return location; 
        }
        
        @Override
        public Dimension getSize() {
            return new Dimension(30, 30); 
        }

        
        @Override
        public Dimension getMinimumSize() {
            return getSize(); 
        }

        
        public void setMark(String newMark) {
            play = newMark; 
            repaint(); 
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawRect(0, 0, 29, 29); 
            g.drawString(play, 11, 20);    
        }
    }
    public static void main(String[] args) 
    {
    	/*TicTacToeClient client = new TicTacToeClient("127.0.0.1");
//		int port = -1;
//		try{
//			//not safe but try-catch will get it
//			port = Integer.parseInt(args[0]);
//		}
//		catch(Exception e){
//			System.out.println("Invalid port");
//		}
//		if(port == -1){
//			return;
//		}
		client.clientConnection();*/
    	
    	TicTacToeClient test; 

        if (args.length == 0) {
            test = new TicTacToeClient("127.0.0.1"); 
        } else {
            test = new TicTacToeClient(args[0]); 
        }

        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
 }

