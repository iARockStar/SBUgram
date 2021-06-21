package server;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {
        DataBase.initializeServer();
        try {
            ServerSocket serverSocket = new ServerSocket(2222);
            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(new ObjectInputStream(socket.getInputStream())
                        , new ObjectOutputStream(socket.getOutputStream())).start();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }


}
