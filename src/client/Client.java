package client;



import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import other.*;

public class Client {
    static Socket socket;
    static ObjectOutputStream objectOutputStream;
    static ObjectInputStream objectInputStream ;
    static ConcurrentHashMap<CommandType,Object> commandSender
            = new ConcurrentHashMap<>();

    public static void connectToServer() {
        try {
            socket = new Socket("127.0.0.1", 8080);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public static void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        Client.objectOutputStream = objectOutputStream;
    }

    public static ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public static void setObjectInputStream(ObjectInputStream objectInputStream) {
        Client.objectInputStream = objectInputStream;
    }

    public static ConcurrentHashMap<CommandType, Object> getCommandSender() {
        return commandSender;
    }

    public static void setCommandSender(ConcurrentHashMap<CommandType, Object> commandSender) {
        Client.commandSender = commandSender;
    }
}
