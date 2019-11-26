
import java.awt.BorderLayout;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class TicTacToeServer extends JFrame {

    private final String[] TTTboard = new String[9]; 
    private final JTextArea moveTallyArea; 
    private final Player[] players; 
    private ServerSocket server; 
    private int currentPlayerMove; 
    private final static int playerX = 0; 
    private final static int playerO = 1; 
    private final static String[] possibleMoves = {"X", "O"}; 
    private final ExecutorService runGame; 
    private final Lock gameLocked; 
    private final Condition isPlayerConnected; 
    private final Condition isPlayerTurn; 

    
    public TicTacToeServer() {
        super("Tic-Tac-Toe Server"); 

        runGame = Executors.newFixedThreadPool(2);
        gameLocked = new ReentrantLock(); 

        isPlayerConnected = gameLocked.newCondition();

        isPlayerTurn = gameLocked.newCondition();

        for (int i = 0; i < 9; i++) {
            TTTboard[i] = ""; 
        }
        players = new Player[2]; 
        currentPlayerMove = playerX; 

        try {
            server = new ServerSocket(12345, 2); 
        } catch (IOException ioException) {
            System.out.println(ioException.toString());
            System.exit(1);
        }

        moveTallyArea = new JTextArea(); 
        add(moveTallyArea, BorderLayout.CENTER);
        moveTallyArea.setText("Server awaiting connections\n");
        setSize(300, 300); 
        setVisible(true); 
    }

    
    public void execute() {
        for (int i = 0; i < players.length; i++) {
            try {
                players[i] = new Player(server.accept(), i);
                runGame.execute(players[i]); 
            } catch (IOException ioException) {
                System.out.println(ioException.toString());
                System.exit(1);
            }
        }

        gameLocked.lock(); 

        try {
            players[playerX].setSuspended(false); 
            isPlayerConnected.signal(); 
        } finally {
            gameLocked.unlock(); 
        }
    }


    private void messageDisplay(final String message) {
            moveTallyArea.append(message); 
    }


    public boolean validateMove(int location, int player) {
        while (player != currentPlayerMove) {
            gameLocked.lock();

            try {
                isPlayerTurn.await(); 
            } catch (InterruptedException exception) {
                System.out.println(exception.toString());
            } finally {
                gameLocked.unlock(); 
            }
        }
        if (!isBlockOccupied(location)) {
            TTTboard[location] = possibleMoves[currentPlayerMove]; 
            currentPlayerMove = (currentPlayerMove + 1) % 2; 

            players[currentPlayerMove].otherPlayerMoved(location);

            gameLocked.lock(); 

            try {
                isPlayerTurn.signal(); 
            } finally {
                gameLocked.unlock(); 
            }

            return true; 
        } else {
            return false; 
        }
    }


    public boolean isWinner() {
        return (!TTTboard[0].isEmpty() && TTTboard[0].equals(TTTboard[1]) && TTTboard[0].equals(TTTboard[2]))
                || (!TTTboard[3].isEmpty() && TTTboard[3].equals(TTTboard[4]) && TTTboard[3].equals(TTTboard[5]))
                || (!TTTboard[6].isEmpty() && TTTboard[6].equals(TTTboard[7]) && TTTboard[6].equals(TTTboard[8]))
                || (!TTTboard[0].isEmpty() && TTTboard[0].equals(TTTboard[3]) && TTTboard[0].equals(TTTboard[6]))
                || (!TTTboard[1].isEmpty() && TTTboard[1].equals(TTTboard[4]) && TTTboard[1].equals(TTTboard[7]))
                || (!TTTboard[2].isEmpty() && TTTboard[2].equals(TTTboard[5]) && TTTboard[2].equals(TTTboard[8]))
                || (!TTTboard[0].isEmpty() && TTTboard[0].equals(TTTboard[4]) && TTTboard[0].equals(TTTboard[8]))
                || (!TTTboard[2].isEmpty() && TTTboard[2].equals(TTTboard[4]) && TTTboard[2].equals(TTTboard[6]));
    }
    
    public boolean isGameOver() {
        return isWinner() || boardComplete();
    }
    
    public boolean isBlockOccupied(int location) {
        return TTTboard[location].equals(possibleMoves[playerX]) || TTTboard[location].equals(possibleMoves[playerO]);
    }

    
    public boolean boardComplete() {
        for (int i = 0; i < TTTboard.length; ++i) {
            if (TTTboard[i].isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private class Player implements Runnable {

        private final Socket connection; 
        private Scanner input; 
        private Formatter outputText; 
        private final int playerNum; 
        private final String XorO;
        private boolean isSuspended = true; 

        public Player(Socket socket, int number) {
            playerNum = number; 
            XorO = possibleMoves[playerNum]; 
            connection = socket; 

            try {
                input = new Scanner(connection.getInputStream());
                outputText = new Formatter(connection.getOutputStream());
            } catch (IOException ioException) {
                System.out.println(ioException.toString());
                System.exit(1);
            }
        }

        public void otherPlayerMoved(int location) {
            outputText.format("Opponent moved\n");
            outputText.format("%d\n", location); 
            outputText.flush(); 
            outputText.format(isWinner() ? "DEFEAT\n" : boardComplete() ? "TIE\n" : "");
            outputText.flush();
        }


        @Override
        public void run() {
            try {
                messageDisplay("Player " + XorO + " connected\n");
                outputText.format("%s\n", XorO); 
                outputText.flush(); 

                if (playerNum == playerX) {
                    outputText.format("%s\n%s", "Player X connected", "Waiting for another player\n");
                    outputText.flush(); 
                    gameLocked.lock(); 

                    try {
                        while (isSuspended) {
                            isPlayerConnected.await(); 
                        }
                    } catch (InterruptedException exception) {
                        System.out.println(exception.toString());
                    } finally {
                        gameLocked.unlock(); 
                    }

                    outputText.format("Other player connected. Your move.\n");
                    outputText.flush(); 
                } else {
                    outputText.format("Player O connected, please wait\n");
                    outputText.flush(); 
                }

                while (!isGameOver()) {
                    int location = 0; 

                    if (input.hasNext()) {
                        location = input.nextInt(); 
                    }
                    if (validateMove(location, playerNum)) {
                        messageDisplay("\nlocation: " + location);
                        outputText.format("Valid move.\n"); 
                        outputText.flush(); 
                        outputText.format(isWinner() ? "VICTORY\n" : boardComplete() ? "TIE\n" : "");
                        outputText.flush();
                    } else {
                        outputText.format("Invalid move, try again\n");
                        outputText.flush(); 
                    }
                }
            } finally {
                try {
                    connection.close(); 
                } catch (IOException ioException) {
                    System.out.println(ioException.toString());
                    System.exit(1);
                }
            }
        }

        public void setSuspended(boolean status) {
            isSuspended = status; 
        }
    }

}