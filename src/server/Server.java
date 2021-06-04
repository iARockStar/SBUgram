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
            ServerSocket serverSocket = new ServerSocket(8080);
            while (true) {
                System.out.println("waiting for client to connect");
                Socket socket = serverSocket.accept();
                System.out.println("client connected("+Thread.currentThread().getId()+")");
                new ClientHandler(new ObjectInputStream(socket.getInputStream())
                        , new ObjectOutputStream(socket.getOutputStream())).start();
            }

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }


}
