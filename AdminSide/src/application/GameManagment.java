package application;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;

public class GameManagment {
	private TableView<Games> table = new TableView();
	private TextField[] textfields = new TextField[6];
	private Label[] labels = new Label[6];
	private Label error = createStyledLabel("");
	private BorderPane root = new BorderPane();
	private String ip;
	private BorderPane rootUpdate = new BorderPane();
	private BorderPane rootAdd = new BorderPane();

	public GameManagment(String ip) {
		this.ip = ip;
	}

	public BorderPane manageGame() {
		Image image = new Image("BGMario.jpg");
		root.setStyle("-fx-background-image: url('" + image.getUrl() + "'); -fx-background-size: cover;");
		table.setMaxHeight(300);
		table.setMaxWidth(350);
		TableColumn id = new TableColumn<>("ID:");
		id.setMinWidth(150);
		id.setCellValueFactory(new PropertyValueFactory<User, String>("id"));
		TableColumn title = new TableColumn<>("Game Title:");
		title.setMinWidth(150);
		title.setCellValueFactory(new PropertyValueFactory<User, String>("title"));
		TableColumn genre = new TableColumn<>("Genre:");
		genre.setMinWidth(150);
		genre.setCellValueFactory(new PropertyValueFactory<User, String>("genre"));
		TableColumn datecoloumn = new TableColumn<>("Date Adder:");
		datecoloumn.setMinWidth(150);
		datecoloumn.setCellValueFactory(new PropertyValueFactory<User, Date>("date"));
		TableColumn description = new TableColumn<>("Description:");
		description.setMinWidth(150);
		description.setCellValueFactory(new PropertyValueFactory<User, String>("description"));
		TableColumn deviceIp = new TableColumn<>("Phone Number:");
		deviceIp.setMinWidth(150);
		deviceIp.setCellValueFactory(new PropertyValueFactory<User, String>("deviceIp"));
		table.getColumns().addAll(id, title, genre, datecoloumn, description, deviceIp);
		Button delete = createStyledButton("Delete Game");
		Button update = createStyledButton("Update Game");
		Button add = createStyledButton("Add Game");
		Button close = createStyledButton("Back");
		close.setOnAction(e -> {
			myEvents.device.getScene().setRoot(myEvents.device.getRoot());
		});
		FlowPane flow = new FlowPane();
		HBox hbox = new HBox(10, add, update, delete,close);
		hbox.setAlignment(Pos.CENTER_LEFT);
		flow.setAlignment(Pos.TOP_CENTER);
		hbox.setSpacing(10);
		hbox.setPadding(new Insets(15,15,15,15));
		flow.setPadding(new Insets(15,15,15,15));
        table.setStyle("-fx-background-color: #789461; -fx-table-cell-border-color: #EBD9B4;");
		flow.getChildren().add(hbox);
		hbox.setAlignment(Pos.CENTER);
		root.setCenter(table);
		root.setBottom(flow);
		root.setTop(error);
		addAllGames();
		delete.setOnAction(e -> {
			deleteGameSQL();
		});

		update.setOnAction(e -> {
			BorderPane temproot = updateGameScene();

			if (temproot != null)
				myEvents.device.getScene().setRoot(temproot);
			else
				error.setText("No Device was selected!!!");

		});
		add.setOnAction(e -> {
			myEvents.device.getScene().setRoot(addGameScene());

		});
		return root;
	}

	public void insertGameSQL(Label error) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			CallableStatement callableStatement = con.prepareCall("{call InsertGame(?,?,?,?,?,?)}");
			boolean flag = false;
			for (int i = 0; i < 4; i++) {
				if (textfields[i] == null || textfields[i].getText().trim().equals("")) {
					flag = true;
					break;
				}
			}
			if (flag) {
				error.setText("there are some textfields are Empty!!!");
			} else {
				callableStatement.setInt(1, Integer.parseInt(textfields[0].getText().trim()));
				callableStatement.setString(6, this.ip);
				callableStatement.setString(2, textfields[1].getText());
				callableStatement.setDate(4, new java.sql.Date(new Date().getTime()));
				callableStatement.setString(3, textfields[2].getText());
				callableStatement.setString(5, textfields[3].getText());
				callableStatement.executeUpdate();
				callableStatement.close();
				error.setText("Game Added!");
				addAllGames();

			}
		} catch (SQLException ex) {
			error.setText("Game already Exists!");
		} catch (NumberFormatException ex) {
			error.setText("The ID field only accept numbers");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public void updateGameSQL() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			CallableStatement callableStatement = con.prepareCall("{call UpdateGame(?,?,?,?,?,?)}");
			boolean flag = false;
			for (int i = 0; i < 4; i++) {
				if (textfields[i] == null || textfields[i].getText().trim().equals("")) {
					flag = true;
					break;
				}
			}
			if (flag) {
				error.setText("there are some textfields are Empty!!!");
			} else {
				callableStatement.setString(1, textfields[0].getText());
				callableStatement.setString(6, this.ip);
				callableStatement.setString(2, textfields[1].getText());
				callableStatement.setDate(4, new java.sql.Date(new Date().getTime()));
				callableStatement.setString(3, textfields[2].getText());
				callableStatement.setString(5, textfields[3].getText());
				callableStatement.executeUpdate();
				callableStatement.close();
				error.setText("Game Added!");
				addAllGames();

			}
		} catch (SQLException ex) {
			error.setText("Game already Exists!");
		} catch (NumberFormatException ex) {
			error.setText("The ID field only accept numbers");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public void addAllGames() {
		try {
			table.getItems().clear();
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			CallableStatement callableStatement = con.prepareCall("{call Find_Game_UsingDeviceID(?)}");
			callableStatement.setString(1, this.ip);
			ResultSet rs = callableStatement.executeQuery();
			while (rs.next()) {
				table.getItems().add(new Games(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(4),
						rs.getString(5), rs.getString(6)));
			}
			callableStatement.close();
			table.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteGameSQL() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123);
			CallableStatement callableStatement = con.prepareCall("{call DeleteGame(?)}");
			boolean flag = false;
			String id = null;
			if (table.getSelectionModel().getSelectedItem() != null) {
				id = table.getSelectionModel().getSelectedItem().getId();
			} else {
				if (textfields[0].getText().trim().isEmpty()) {
					flag = true;
				} else {
					id = textfields[0].getText().trim();
				}
			}
			if (flag) {
				error.setText(" No game from the table was selected!!!");
			} else {
				callableStatement.setString(1, id);
				callableStatement.executeUpdate();
				callableStatement.close();
				error.setText("Game Deleted!");
				addAllGames();

			}
		} catch (SQLException ex) {
			error.setText("Game Does Not Exist!");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (NullPointerException e1) {
			error.setText("Select Something!");
		} catch (NumberFormatException e1) {
			error.setText("Only Numbers!");
		}
	}

	public Games getGame() {
		if (table.getSelectionModel().getSelectedItem() == null) {
			error.setText("Select something first!");
			return null;
		}
		return table.getSelectionModel().getSelectedItem();
	}

	public BorderPane updateGameScene() {
		Games game = getGame();
		try {
			for (int i = 0; i < textfields.length; i++) {
				textfields[i] = new TextField();
			}

			labels[0] = createStyledLabel("Game ID:");
			labels[1] = createStyledLabel("Game Title:");
			labels[2] = createStyledLabel("Game Genre:");
			labels[3] = createStyledLabel("Game descreption:");
			textfields[0].setText(game.getId());
			textfields[1].setText(game.getTitle());
			textfields[2].setText(game.getGenre());
			textfields[3].setText(game.getDescription());
			HBox hbox1 = new HBox(10, labels[0], textfields[0]);
			HBox hbox2 = new HBox(10, labels[1], textfields[1]);
			HBox hbox3 = new HBox(10, labels[2], textfields[2]);
			HBox hbox4 = new HBox(10, labels[3], textfields[3]);

			VBox vbox1 = new VBox(10, hbox1, hbox2, hbox3, hbox4);
			rootUpdate.setCenter(vbox1);
			error.setMinWidth(150);
			rootUpdate.setTop(error);
			rootUpdate.setStyle("-fx-background-color: #AC87C5");
			Button update = createStyledButtonAdd("Update Game ");
			Button close = createStyledButtonAdd("Close");
			update.setOnAction(e -> {
				updateGameSQL();
			});
			close.setOnAction(e -> {
				myEvents.device.getScene().setRoot(root);
			});
			HBox hbox = new HBox(10, update, close);
			hbox.setAlignment(Pos.CENTER);
			rootUpdate.setBottom(hbox);
			return rootUpdate;
		} catch (NullPointerException e) {
			error.setText("GameNotFound!");
		}
		return null;
	}

	public BorderPane addGameScene() {
		Label error = createStyledLabel("");
		for (int i = 0; i < textfields.length; i++) {
			textfields[i] = createBorderedTextField();
		}

		labels[0] = createStyledLabel("Game ID:");
		labels[1] = createStyledLabel("Game Title:");
		labels[2] = createStyledLabel("Game Genre:");
		labels[3] = createStyledLabel("Game descreption:");

		HBox hbox1 = new HBox(10, labels[0], textfields[0]);
		HBox hbox2 = new HBox(10, labels[1], textfields[1]);
		HBox hbox3 = new HBox(10, labels[2], textfields[2]);
		HBox hbox4 = new HBox(10, labels[3], textfields[3]);
		rootAdd.setStyle("-fx-background-color: #AC87C5");
		VBox vbox1 = new VBox(10, hbox1, hbox2, hbox3, hbox4);
		rootAdd.setCenter(vbox1);
		rootAdd.setTop(error);
		Button add = createStyledButtonAdd("Add a Game to the Device");
		Button close = createStyledButtonAdd("Close");
		add.setOnAction(e -> {
			insertGameSQL(error);
		});
		close.setOnAction(e -> {
			myEvents.device.getScene().setRoot(root);
		});
		HBox hbox = new HBox(10, add, close);
		hbox.setAlignment(Pos.CENTER);
		rootAdd.setBottom(hbox);
		return rootAdd;
	}

	private LinearGradient createLinearGradient(Color startColor, Color endColor) {
		Stop[] stops = new Stop[] { new Stop(0, startColor), new Stop(1, endColor) };
		return new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
	}

	private Label createStyledLabel(String text) {
		Label label = new Label(text);
		label.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
		label.setMaxWidth(170);
		label.setMinWidth(170);
		label.setAlignment(Pos.CENTER);
		return label;
	}

	private TextField createBorderedTextField() {
		TextField textField = new TextField();
		textField.setStyle(
				"-fx-border-color: white; -fx-border-width: 2px; -fx-background-color: white; -fx-text-fill: black;");
		textField.setEffect(createGlowingShadow());
		textField.setAlignment(Pos.CENTER);
		textField.setMinWidth(150);
		textField.setMaxWidth(150);
		textField.setMaxHeight(30);
		textField.setMinHeight(30);
		return textField;
	}

	private HBox createHBox(Label label, TextField textField) {
		HBox hBox = new HBox(label, textField);
		hBox.setSpacing(15);
		hBox.setAlignment(Pos.CENTER);
		return hBox;
	}

	private Button createStyledButton(String text) {
		Button button = new Button(text);
		button.setStyle("-fx-background-color: #99BC85; " + "-fx-text-fill: white; " + "-fx-background-radius: 5px; "
				+ "-fx-font-size: 16px; -fx-font-weight: bold;");
		button.setEffect(createGlowingShadow());
		return button;
	}
	private Button createStyledButtonAdd(String text) {
		Button button = new Button(text);
		button.setStyle("-fx-background-color: #6962AD; " + "-fx-text-fill: white; " + "-fx-background-radius: 5px; "
				+ "-fx-font-size: 16px; -fx-font-weight: bold;");
		button.setEffect(createGlowingShadow());
		return button;
	}
	private DropShadow createGlowingShadow() {
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.DEEPSKYBLUE);
		shadow.setWidth(15);
		shadow.setHeight(15);
		return shadow;
	}

}
