package client;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import other.*;

/**
 * This class provides socket and streams for connecting to
 * the server and nothing more!
 */

public class Client {
    static Socket socket;
    static ObjectOutputStream objectOutputStream;
    static ObjectInputStream objectInputStream ;
    static ConcurrentHashMap<CommandType,Object> commandSender
            = new ConcurrentHashMap<>();
    private static boolean isServerUp = false;

    /**
     * connectToServer method is called at the beginning of the client side program.
     * it connects client side to the server side.
     */
    public static void connectToServer() {
        try {
            socket = new Socket("127.0.0.1", 2222);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    //getters and setters
    public static ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public static ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public static ConcurrentHashMap<CommandType, Object> getCommandSender() {
        return commandSender;
    }

    public static void setCommandSender(ConcurrentHashMap<CommandType, Object> commandSender) {
        Client.commandSender = commandSender;
    }

    public static boolean isServerUp() {
        return isServerUp;
    }

    public static void setServerUp(boolean serverUp) {
        isServerUp = serverUp;
    }
}
