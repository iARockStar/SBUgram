package server;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class doesn't do anything except accepting users
 * and initializing the server and connecting to the database.
 */
public class Server {
    /**
     * this field shows that during the life of this app the server is
     * available. fantastic stuff am i right?
     */
    private static final boolean isServerUp = true;

    /**
     * this method sends a true boolean which shows that the server is
     * always up.
     * @return a boolean which is true.
     */
    public static boolean isServerUp() {
        return isServerUp;
    }

    /**
     * this method initializes the server and creates a new ClientHandler thread
     * for each new user that connects to the server.
     */
    public static void main(String[] args) {
        DataBase.initializeServer();
        try {
            ServerSocket serverSocket = new ServerSocket(2222);
            while (isServerUp()) {
                Socket socket = serverSocket.accept();
                new ClientHandler(new ObjectInputStream(socket.getInputStream())
                        , new ObjectOutputStream(socket.getOutputStream())).start();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }


}
