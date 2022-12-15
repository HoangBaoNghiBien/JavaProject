import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import static java.lang.Integer.parseInt;

public class ClientController implements Initializable {
    @FXML
    public TextField chatInput;

    @FXML
    public ListView<String> chatList;

    @FXML
    public ListView<String> clientList;

    @FXML
    public ListView<String> receiveList;

    @FXML
    public Text clientNumber;

    @FXML
    public Button sendAllBtn;

    @FXML
    public Button sendBtn;

    Client connection;
    public ArrayList<Integer> clientsList;
    public List<String> messageList;
    ArrayList<String> destination;

    public ClientController() {
        clientsList = new ArrayList<>();
    }

    public void SelectConnection(Client c) {
        connection = c;
    }

    @FXML
    void sendAllBtnMethod(ActionEvent event) {
        MessageData m = new MessageData();
        m.setText(chatInput.getText());
        m.messageList = destination;

        if (m.messageList != null) {
            for (String i : m.messageList) {
                m.clientID.add(parseInt(i));
            }
        }
        m.setToAll(true);
        //System.out.println(chatInput.getText());
        connection.send(m);
        chatInput.clear();
    }

    @FXML
    void sendBtnMethod(ActionEvent event) {
        MessageData m = new MessageData();
        m.setText(chatInput.getText()); // getting text from textfield to send
        m.messageList = messageList;
        // System.out.println("destination: " + messageList);
        // System.out.println("userText: " + chatInput.getText());

        if (m.messageList != null) {
            for (String i : m.messageList) {
                m.clientID.add(parseInt(i));
            }
        }
        //System.out.println(chatInput.getText());
        connection.send(m);
        chatInput.clear();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        
    }
}
