import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Ellipse;

public class TicTacToe extends Application {
	private char playerTurn = 'X';

	private Cell[][] cell = new Cell[3][3];

	private Label status = new Label("X's turn to play");
	public ListView<String> listView = new ListView<>();

	@Override
	public void start(Stage primaryStage) {
		// A 3X3 board of JavaFX Buttons using GridPane
		BorderPane borderPane = new BorderPane();
		borderPane.setStyle(" -fx-background-image: url('./background/background.png'); "
				+ "-fx-background-repeat: stretch;" +
				"-fx-background-size: fullscreen;"
				+ " -fx-background-position: center;"
				+ "  -fx-text-fill: #b22222;");
		GridPane gridPane = new GridPane();
		gridPane.setStyle("-fx-background-color: lightgray;");
		gridPane.setMinSize(450, 450);
		gridPane.setPrefSize(450, 450);
		gridPane.setMaxSize(450, 450);
		gridPane.setHgap(5);
		gridPane.setVgap(5);

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				cell[i][j] = new Cell();
				gridPane.add(cell[i][j], j, i);
			}
		}

		// Create 2 buttons
		Button btn1 = new Button("Start Over");
		Button btn2 = new Button("Exit");
		btn1.setStyle("-fx-font: 22 arial; -fx-base: #DBDFFF;");
		btn2.setStyle("-fx-font: 22 arial; -fx-base: #DBDFFF;");
		// Create a VBox
		HBox hBox = new HBox();
		// set the spacing between the two buttons to 10
		hBox.setSpacing(10);
		// set the padding around the VBox to 10
		hBox.setPadding(new Insets(10));

		// add the two buttons to the VBox
		hBox.getChildren().addAll(btn1, btn2);
		// create a vbox to hold the label
		VBox vBox = new VBox();
		borderPane.setCenter(gridPane);
		borderPane.setTop(hBox);
		
		// create border for label
		status.setStyle("-fx-border-color: lightgrey; -fx-base: lightgrey; -fx-border-width: 5; -fx-font: 22 arial");
		// set min width for label
		status.setMinWidth(270);
		vBox.getChildren().add(status);
		listView.getItems().add("X's turn to play");
		vBox.getChildren().add(listView);
		borderPane.setRight(vBox);

		// set padding for vbox
		vBox.setPadding(new Insets(10));
		borderPane.setRight(vBox);
		// set padding right for borderPane
		borderPane.setMargin(status, new Insets(10));

		// Create and attach an EventHandler for btn1 which clear the board game a
		btn1.setOnAction(value -> {
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++) {
					cell[i][j].setToken(' ');
				}
			status.setText("X's turn to play");
			listView.getItems().add("New game started");
			listView.getItems().add("X's turn to play");
			playerTurn = 'X';
			btn1.setDisable(false);
		});
		
		// Create and attach an EventHandler for btn2 which exits the game
		btn2.setOnAction(value -> {
			System.exit(0);
		});

		Scene scene = new Scene(borderPane, 700, 700);
		primaryStage.setTitle("Tic-Tac-Toe");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public boolean isFull() {
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
				if (cell[i][j].getToken() == ' ')
					return false;

		return true;
	}

	public boolean isWon(char token) {
		for (int i = 0; i < 3; i++)
			if (cell[i][0].getToken() == token
					&& cell[i][1].getToken() == token
					&& cell[i][2].getToken() == token) {
				return true;
			}

		for (int j = 0; j < 3; j++)
			if (cell[0][j].getToken() == token
					&& cell[1][j].getToken() == token
					&& cell[2][j].getToken() == token) {
				return true;
			}

		if (cell[0][0].getToken() == token
				&& cell[1][1].getToken() == token
				&& cell[2][2].getToken() == token) {
			return true;
		}

		if (cell[0][2].getToken() == token
				&& cell[1][1].getToken() == token
				&& cell[2][0].getToken() == token) {
			return true;
		}

		return false;
	}

	public class Cell extends Pane {
		private char token = ' ';

		public Cell() {
			setStyle("-fx-border-color: black");
			this.setPrefSize(2000, 2000);
			this.setOnMouseClicked(e -> handleMouseClick());
		}

		public char getToken() {
			return token;
		}

		public void setToken(char c) {
			token = c;
			if (token == 'X') {
				Line line1 = new Line(10, 10,
						this.getWidth() - 10, this.getHeight() - 10);
				line1.endXProperty().bind(this.widthProperty().subtract(10));
				line1.endYProperty().bind(this.heightProperty().subtract(10));
				Line line2 = new Line(10, this.getHeight() - 10,
						this.getWidth() - 10, 10);
				line2.startYProperty().bind(
						this.heightProperty().subtract(10));
				line2.endXProperty().bind(this.widthProperty().subtract(10));
				this.getChildren().addAll(line1, line2);
			} else if (token == 'O') {
				Ellipse ellipse = new Ellipse(this.getWidth() / 2,
						this.getHeight() / 2, this.getWidth() / 2 - 10,
						this.getHeight() / 2 - 10);
				ellipse.centerXProperty().bind(
						this.widthProperty().divide(2));
				ellipse.centerYProperty().bind(
						this.heightProperty().divide(2));
				ellipse.radiusXProperty().bind(
						this.widthProperty().divide(2).subtract(10));
				ellipse.radiusYProperty().bind(
						this.heightProperty().divide(2).subtract(10));
				ellipse.setStroke(Color.BLACK);
				ellipse.setFill(Color.WHITE);

				getChildren().add(ellipse);
			}
			else if (token == ' ') {
				this.getChildren().clear();
			}
		}

		private void handleMouseClick() {
			if (token == ' ' && playerTurn != ' ') {
				setToken(playerTurn);
				if (isWon(playerTurn)) {
					status.setText(playerTurn + " won! The game is over");
					listView.getItems().add(playerTurn + " won! The game is over");
					playerTurn = ' ';
				} else if (isFull()) {
					status.setText("Draw! The game is over");
					listView.getItems().add("Draw! The game is over");
					playerTurn = ' ';
				} else {
					playerTurn = (playerTurn == 'X') ? 'O' : 'X';
					status.setText(playerTurn + "'s turn");
					listView.getItems().add(playerTurn + "'s turn");
				}
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
