
import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class GuiServer extends Application{

	
	TextField s1,s2,s3,s4, c1;
	Button serverChoice,clientChoice,b1;
	HashMap<String, Scene> sceneMap;
	GridPane grid;
	HBox buttonBox;
	VBox clientBox;
	Scene startScene;
	BorderPane startPane;
	Server serverConnection;
	Client clientConnection;
	ListView<String> listItems, listItems2;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("The Networked Client/Server GUI Example");
		
		this.serverChoice = new Button("Server");
		this.serverChoice.setStyle("-fx-pref-width: 300px; -fx-pref-height: 300px; -fx-font-family: 'Arial'");
		
		this.serverChoice.setOnAction(e->{ primaryStage.setScene(sceneMap.get("server"));
											primaryStage.setTitle("This is the Server");
				serverConnection = new Server(data -> {
					Platform.runLater(()->{
						listItems.getItems().add(data.toString());
						listItems.setStyle("-fx-font-family: 'Arial'");
					});

				});
											
		});
		
		this.clientChoice = new Button("Client");
		this.clientChoice.setStyle("-fx-pref-width: 300px; -fx-pref-height: 300px; -fx-font-family: 'Arial'");
		
		this.clientChoice.setOnAction(e-> {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/clientView.fxml"));
			Parent root = null;
			try {
				root = loader.load();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			ClientController controller = loader.getController();
			clientConnection = new Client(data -> {
				Platform.runLater(()->{
					String[] msgArr = data.toString().split("-");
					String msg = msgArr[0];
					System.out.println("MSG:" + msg);

					// get char from msg
					char c = msg.charAt(8);
					System.out.println("Char:" + c);
					String onlineListStr = msgArr[1];
					//System.out.println("onlineListStr: " + onlineListStr);

					controller.clientList.getItems().clear();
					controller.chatList.getItems().add(msg);
					controller.sendBtn.setDisable(true);
					controller.sendAllBtn.setDisable(true);

					//controller.clientNumber.setText("CLIENT " + count);
					//count++;

					onlineListStr = (onlineListStr.replace("[", "")).replace("]", "").replaceAll(" ", "");
					String[] onlineList = onlineListStr.split(",");
					if (controller.clientNumber.getText().equals("CLIENT #")) {
						// controller.clientNumber.setText("CLIENT " + onlineList.length);
						controller.clientNumber.setText("CLIENT " + c);
					}

					if (onlineList.length > 1) {
						controller.sendAllBtn.setDisable(false);
					}
					for (String i : onlineList) {
						controller.clientList.getItems().add("Client: " + i);
						//System.out.println(i);
					}

					controller.clientList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
					controller.clientList.setOnMouseClicked(t -> {
						controller.sendBtn.setDisable(false);
						controller.receiveList.getItems().clear();
						ObservableList<String> selectedItems = controller.clientList.getSelectionModel().getSelectedItems();
						for (String s : selectedItems) {
							s.replace("Client: ", "");
							controller.receiveList.getItems().addAll(s);
						}

						
						// controller.receiveList.getItems().addAll(selectedItems);

						controller.messageList = selectedItems.stream().collect(Collectors.toList());
						for (int i = 0; i < controller.messageList.size(); i++) {
							controller.messageList.set(i, controller.messageList.get(i).replace("Client: ", ""));
							System.out.println(controller.messageList.get(i));
						}
					});
				});

			});

			clientConnection.start();
			controller.SelectConnection(clientConnection);

			primaryStage.setTitle("Client");
			Scene s1 = new Scene(root);
			s1.getStylesheets().add("/style/style.css");
			primaryStage.setScene(s1);
			primaryStage.show();
		});
		
		this.buttonBox = new HBox(400, serverChoice, clientChoice);
		startPane = new BorderPane();
		startPane.setPadding(new Insets(70));
		startPane.setCenter(buttonBox);
		
		startScene = new Scene(startPane, 800,800);
		
		listItems = new ListView<String>();
		//listItems2 = new ListView<String>();

		sceneMap = new HashMap<String, Scene>();
		sceneMap.put("server",  createServerGui());

		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

		primaryStage.setScene(startScene);
		primaryStage.show();
		
	}
	
	public Scene createServerGui() {
		
		BorderPane pane = new BorderPane();
		pane.setPadding(new Insets(70));
		pane.setStyle("-fx-background-color: #d8c5b1");
		
		pane.setCenter(listItems);
	
		return new Scene(pane, 700, 500);
		
		
	}

}
