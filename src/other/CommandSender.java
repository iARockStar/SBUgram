package other;

import java.io.Serializable;
import client.*;

public class CommandSender implements Serializable {

    private CommandType commandType;
    private Object object;

    public CommandSender(CommandType commandType, Object object) {
        this.commandType = commandType;
        this.object =object;
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public Object getUser() {
        return object;
    }
}
