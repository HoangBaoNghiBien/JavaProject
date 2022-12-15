import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MessageData implements Serializable{
    private static final long serialVersionUID = 1L;
    private boolean toAll;
    private String message;
    public List<String> messageList;
    public ArrayList<Integer> clientID;
    public int whichClient;

    MessageData() {
        this.toAll = false;
        this.message = "";
        this.messageList = new ArrayList<String>();
        this.clientID = new ArrayList<Integer>();
    }

    public void setText(String text) {
        this.message = text;
    }

    public String getText() {
        return this.message;
    }

    public void setToAll(boolean toAll) {
        this.toAll = toAll;
    }

    public boolean isToAll() {
        return this.toAll;
    }
}
