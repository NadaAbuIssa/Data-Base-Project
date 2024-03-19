package application;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class UserManagment {
	private TableView<Object> table = new TableView<>();
	private TextField[] textFields = new TextField[7];
	private TextField searchtxt = createBorderedTextField();
	private Label error = new Label("");
	private BorderPane root = new BorderPane();
	private Scene scene;
	private TextField[] textfields = new TextField[7];

	public UserManagment() {
		rootMaker();
	}

	public void rootMaker() {
		TableColumn id = new TableColumn<>("ID:");
		id.setMinWidth(150);
		id.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
		TableColumn username = new TableColumn<>("User Name:");
		username.setMinWidth(150);
		username.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
		TableColumn password = new TableColumn<>("Password:");
		password.setMinWidth(150);
		password.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
		TableColumn firstname = new TableColumn<>("First Name:");
		firstname.setMinWidth(150);
		firstname.setCellValueFactory(new PropertyValueFactory<User, String>("firstname"));
		TableColumn lastname = new TableColumn<>("Last Name:");
		lastname.setMinWidth(150);
		lastname.setCellValueFactory(new PropertyValueFactory<User, String>("lastname"));
		TableColumn number = new TableColumn<>("Phone Number:");
		number.setMinWidth(150);
		number.setCellValueFactory(new PropertyValueFactory<User, String>("number"));
		TableColumn budget = new TableColumn<>("Budget:");
		budget.setMinWidth(150);
		budget.setCellValueFactory(new PropertyValueFactory<User, BigDecimal>("budget"));
		table.getColumns().addAll(id, username, password, firstname, lastname, number, budget);
		Button insert = createStyledButton("Insert");
		insert.setId("InsertUser");
		Button close = createStyledButton("Back");

		Button addpayment = createStyledButton("Add Payment");
		insert.addEventHandler(ActionEvent.ACTION, new myEvents());
		tablefill();
		root.setCenter(table);
		root.setStyle("-fx-background-color: black");
		FlowPane flow = new FlowPane();

		flow.setAlignment(javafx.geometry.Pos.CENTER);
		searchtxt.setPromptText("ID or userName");
		Button search = createStyledButton("Search");
		search.setId("SearchUser");
		close.setId("UserBack");
		close.addEventHandler(ActionEvent.ACTION, new myEvents());
		search.addEventHandler(ActionEvent.ACTION, new myEvents());
		HBox hbox = new HBox(15, insert, searchtxt, search, close);
		hbox.setAlignment(Pos.CENTER);
		flow.getChildren().addAll(hbox);
		root.setBottom(flow);

	}

	private TableView<Booking> tableBooking = new TableView<Booking>();

	public void getBookingDetailsSQL(int ID) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			String query = "SELECT * FROM booking WHERE ClientID = ?";
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setInt(1, ID);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				tableBooking.getItems().add(new Booking(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getInt(6), rs.getBigDecimal(7)));
			}

			tableBooking.refresh();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void bookingDetails(int ID) {
		TableColumn id = new TableColumn<>("Booking ID:");
		id.setMinWidth(150);
		id.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("id"));
		TableColumn userid = new TableColumn<>("User ID:");
		userid.setMinWidth(150);
		userid.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("userid"));
		TableColumn deviceID = new TableColumn<>("device ID:");
		deviceID.setMinWidth(150);
		deviceID.setCellValueFactory(new PropertyValueFactory<Booking, String>("deviceID"));
		TableColumn start = new TableColumn<>("start Time:");
		start.setMinWidth(150);
		start.setCellValueFactory(new PropertyValueFactory<Booking, String>("start"));
		TableColumn end = new TableColumn<>("end startTime:");
		end.setMinWidth(150);
		end.setCellValueFactory(new PropertyValueFactory<Booking, String>("end"));
		TableColumn duration = new TableColumn<>("duration:");
		duration.setMinWidth(150);
		duration.setCellValueFactory(new PropertyValueFactory<Booking, Integer>("duration"));
		TableColumn cose = new TableColumn<>("Cost:");
		cose.setMinWidth(150);
		cose.setCellValueFactory(new PropertyValueFactory<Booking, BigDecimal>("cose"));
		tableBooking.getColumns().addAll(id, userid, deviceID, start, end, duration, cose);
		BorderPane root = new BorderPane();
		root.setStyle("-fx-background-color: black");
		Button back = AdminView.createStyledButton("Back");
		Stage stage = new Stage();
		back.setOnAction(e -> {
			stage.close();
		});
		root.setBottom(new HBox(back));
		root.setCenter(tableBooking);
		try {
			getBookingDetailsSQL(ID);
		} catch (Exception e) {

		}
		stage.setScene(new Scene(root));
		stage.show();
	}

	public void paymentDetails(int ID) {
		BorderPane root = new BorderPane();
		Button back = AdminView.createStyledButton("Back");
		Stage stage = new Stage();
		root.setStyle("-fx-background-color: black");

		back.setOnAction(e -> {
			stage.close();
		});
		root.setBottom(new HBox(10, back));
		TableView<Payment> tablepayment = new TableView<Payment>();
		TableColumn id = new TableColumn<>("Booking ID:");
		id.setMinWidth(150);
		id.setCellValueFactory(new PropertyValueFactory<Payment, Integer>("id"));
		TableColumn userid = new TableColumn<>("User ID:");
		userid.setMinWidth(150);
		userid.setCellValueFactory(new PropertyValueFactory<Payment, Integer>("userid"));
		TableColumn method = new TableColumn<>("Payment Method:");
		method.setMinWidth(150);
		method.setCellValueFactory(new PropertyValueFactory<Payment, String>("method"));
		TableColumn ammount = new TableColumn<>("ammount:");
		ammount.setMinWidth(150);
		ammount.setCellValueFactory(new PropertyValueFactory<Payment, BigDecimal>("ammount"));
		TableColumn date = new TableColumn<>("date:");
		date.setMinWidth(150);
		date.setCellValueFactory(new PropertyValueFactory<Payment, Date>("date"));
		tablepayment.getColumns().addAll(id, userid, method, ammount, date);
		root.setCenter(tablepayment);
		try {
			tablepayment.getItems().clear();
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM payment WHERE UserID = '" + ID + "'");

			while (rs.next()) {
				tablepayment.getItems().add(
						new Payment(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getBigDecimal(4), rs.getDate(5)));
			}
			tablepayment.refresh();
		} catch (ClassNotFoundException e) {
		} catch (SQLException e) {
		}
		stage.setScene(new Scene(root));
		stage.show();
	}

	public void tablefill() {
		try {
			table.getItems().clear();
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM user");

			while (rs.next()) {
				table.getItems().add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5), rs.getString(6), rs.getBigDecimal(7)));
			}
			table.refresh();
		} catch (Exception e) {

		}
	}

	public BorderPane getRoot() {
		return this.root;
	}

	public void close() {
		Main.admin.getScene().setRoot(Main.admin.getRoot());
	}

	public void insertUser() {
		Stage stage = new Stage();
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root, 600, 600);
		scene.getRoot().setStyle("-fx-background-color: #83C0C1; -fx-text-fill: white;");
		
		// Diagonal strip
		Color start = Color.rgb(108, 99, 200); // Define the color using RGB values
		Color end = Color.rgb(7, 116, 173);
		Rectangle diagonalStrip = new Rectangle(600, 600,
				createLinearGradient(start, end));		diagonalStrip.setRotate(45);
		root.getChildren().add(diagonalStrip);
		Image image=new Image("ForNada.gif");
		ImageView view=new ImageView(image);
		VBox vbox=new VBox(view);
		vbox.setMaxHeight(70);
		vbox.setAlignment(Pos.TOP_LEFT);
		root.setLeft(vbox);
		Label[] labels = new Label[7];
		labels[0] = createStyledLabel("ID");
		labels[1] = createStyledLabel("Username");
		labels[2] = createStyledLabel("Password");
		labels[3] = createStyledLabel("First Name");
		labels[4] = createStyledLabel("Last Name");
		labels[5] = createStyledLabel("Number");
		labels[6] = createStyledLabel("Budget");

		for (int i = 0; i < textfields.length; i++) {
			textfields[i] = createBorderedTextField();
		}

		HBox[] hBoxes = new HBox[7];
		for (int i = 0; i < hBoxes.length; i++) {
			hBoxes[i] = createHBox(labels[i], textfields[i]);
		}
		error.setAlignment(Pos.CENTER);
		error.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
		HBox hbox = new HBox(error);
		hbox.setAlignment(Pos.CENTER);
		VBox vBox = new VBox();
		vBox.getChildren().addAll(hBoxes);
		vBox.setMaxWidth(300);
		vBox.setMinWidth(300);
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(15);
		root.setCenter(vBox);
		root.setTop(hbox);
		Button confirm = createStyledButton("Confirm");
		Button cancel = createStyledButton("Cancel");
		confirm.setId("ConfirmUser");
		HBox hBoxButtons = new HBox(confirm, cancel);
		hBoxButtons.setAlignment(Pos.CENTER);
		hBoxButtons.setSpacing(20);
		root.setBottom(hBoxButtons);
		stage.setTitle("Gaming User Insertion");
		stage.setScene(scene);
		stage.show();

		// Event handling
		cancel.setOnAction(e -> stage.close());
		confirm.addEventHandler(ActionEvent.ACTION, new myEvents());

	}

	public void insertUserSQL() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			CallableStatement callableStatement = con.prepareCall("{call InsertUser(?,?,?,?,?,?,?)}");
			boolean flag = false;
			for (int i = 0; i < textfields.length; i++) {
				if (textfields[i] == null || textfields[i].getText().trim().equals("")) {
					flag = true;
					break;
				}
			}
			if (flag) {
				error.setText("there are some textFields are Empty!!!");
			} else {
				callableStatement.setInt(1, Integer.parseInt(textfields[0].getText()));
				callableStatement.setString(2, textfields[1].getText());
				callableStatement.setString(3, textfields[2].getText());
				callableStatement.setString(4, textfields[3].getText());
				callableStatement.setString(5, textfields[4].getText());
				callableStatement.setString(6, textfields[5].getText());
				callableStatement.setBigDecimal(7, BigDecimal.valueOf(Double.parseDouble(textfields[6].getText())));
				callableStatement.executeUpdate();
				callableStatement.close();
				tablefill();
				error.setText("User Added!");
			}
		} catch (SQLException ex) {
			error.setText("User Already Added");
		} catch (NumberFormatException ex) {
			error.setText("The Id field and the budged field only accept numbers");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

	}

	public void SearchScene() {
		BorderPane rootUpdate = new BorderPane();
		Stage stage = new Stage();
		scene = new Scene(rootUpdate, 600, 600);
		scene.getRoot().setStyle("-fx-background-color: #83C0C1; -fx-text-fill: white;");
		User user = getUser();
		if (user != null) {
			// Diagonal strip
	        Color start = Color.rgb(108, 99, 200); // Define the color using RGB values
			Color end = Color.rgb(7, 116, 173);
			Rectangle diagonalStrip = new Rectangle(600, 600,
					createLinearGradient(start, end));
			diagonalStrip.setRotate(45);
			rootUpdate.getChildren().add(diagonalStrip);
			ComboBox<String> combo = new ComboBox<String>();
			combo.getItems().addAll("Booking History", "Payment History");
			combo.setId("UserCombo");
			Button pay = createStyledButton("Pay");
			combo.setOnAction(e -> {
				if (combo.getSelectionModel().getSelectedItem().equals("Booking History")) {
					bookingDetails(user.getId());
				}
				if (combo.getSelectionModel().getSelectedItem().equals("Payment History")) {
					paymentDetails(user.getId());
				}
			});
			pay.setOnAction(e -> {
				try {
					Stage stage2 = new Stage();
					BorderPane root = new BorderPane();
					Button ok = new Button("ok");
					Button cancel = new Button("no");
					TextField amm = new TextField();
					ComboBox<String> method = new ComboBox<String>();
					method.getItems().addAll("Cash", "Card");
					ok.setOnAction(ev -> {
						if (method.getSelectionModel().getSelectedItem() != null) {
							int balance = Integer.parseInt(amm.getText());
							insertPaymentSQL(user.getId(), balance, method.getSelectionModel().getSelectedItem());
							updateUserBalance(user.getId(), balance);
						}
						stage2.close();
						stage.close();
						tablefill();

					});
					root.setBottom(new HBox(ok, cancel));
					root.setCenter(new HBox(method, amm));
					stage2.setScene(new Scene(root, 600, 600));
					stage2.show();
					cancel.setOnAction(ev -> {
						stage2.close();
					});
					amm.setPromptText("Ammount here");
				} catch (Exception ex) {
					error.setText("Amount is wrong");
				}
			});
			Label[] labels = new Label[7];
			labels[0] = createStyledLabel("ID");
			labels[1] = createStyledLabel("Username");
			labels[2] = createStyledLabel("Password");
			labels[3] = createStyledLabel("First Name");
			labels[4] = createStyledLabel("Last Name");
			labels[5] = createStyledLabel("Number");
			labels[6] = createStyledLabel("Budget");
			Label error = new Label("");
			error.setStyle("-fx-text-fill: red; -fx-font-size: 16px; -fx-font-weight: bold;");
			HBox hbox = new HBox(error);
			for (int i = 0; i < textFields.length; i++) {
				textFields[i] = createBorderedTextField();
			}
			textFields[0].setEditable(false);
			textFields[0].setDisable(true);
			if (user != null) {
				textFields[0].setText(user.getId() + "");

				textFields[1].setText(user.getUsername() + "");
				textFields[2].setText(user.getPassword() + "");
				textFields[3].setText(user.getFirstname() + "");
				textFields[4].setText(user.getLastname() + "");
				textFields[5].setText(user.getNumber() + "");
				textFields[6].setText(user.getBudget() + "");
			} else {
				error.setText("User not found");
			}
			HBox[] hBoxes = new HBox[7];
			for (int i = 0; i < hBoxes.length; i++) {
				hBoxes[i] = createHBox(labels[i], textFields[i]);
			}

			VBox vBox = new VBox();
			Image image=new Image("ForNada.gif");
			ImageView view=new ImageView(image);
			VBox vbox=new VBox(view);
			vbox.setMaxHeight(70);
			vbox.setAlignment(Pos.TOP_LEFT);
			rootUpdate.setLeft(vbox);
			vBox.getChildren().addAll(hBoxes);
			vBox.setMaxWidth(300);
			vBox.setMinWidth(300);
			vBox.setAlignment(Pos.CENTER);
			vBox.setSpacing(15);
			rootUpdate.setCenter(vBox);

			Button delete = createStyledButton("Delete");
			Button update = createStyledButton("Update");
			Button cancel = createStyledButton("Cancel");

			HBox hBoxButtons = new HBox(combo, pay, update, delete, cancel);
			hBoxButtons.setAlignment(Pos.CENTER);
			hBoxButtons.setSpacing(20);
			rootUpdate.setBottom(hBoxButtons);
			rootUpdate.setTop(hbox);
			stage.setTitle("Gaming User Removal");
			stage.setScene(scene);
			stage.show();
			update.setOnAction(e -> {
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
					Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
							"Maitech123");
					CallableStatement callableStatement = con.prepareCall("{call Update_UserInfo(?,?,?,?,?,?,?)}");
					if (user != null) {
						callableStatement.setInt(1, user.getId());
						callableStatement.setString(2, textFields[1].getText().trim());
						callableStatement.setString(3, textFields[2].getText().trim());
						callableStatement.setString(4, textFields[3].getText().trim());
						callableStatement.setString(5, textFields[4].getText().trim());
						callableStatement.setString(6, textFields[5].getText().trim());
						callableStatement.setBigDecimal(7,
								BigDecimal.valueOf(Double.parseDouble(textFields[6].getText().trim())));
						callableStatement.executeUpdate();
						table.getItems().clear();
						tablefill();
						clearTextFields();
						table.refresh();
						error.setText("User Updated");
					} else {
						error.setText("User Not Found");
					}
					callableStatement.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
			// Event handling
			cancel.setOnAction(e -> stage.close());
			delete.setOnAction(e -> {
				deleteUserSql(user);
				stage.close();
				tablefill();
			});
		}
	}

	public void insertPaymentSQL(int id, int ammount, String method) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			CallableStatement callableStatement = con.prepareCall("{call InsertPayment(?,?,?,?)}");

			callableStatement.setInt(1, id);
			callableStatement.setString(2, method);
			callableStatement.setBigDecimal(3, BigDecimal.valueOf(ammount));
			callableStatement.setDate(4, new java.sql.Date(new Date().getTime()));
			callableStatement.executeUpdate();
			callableStatement.close();

		} catch (SQLException ex) {
			error.setText("UserAlreadyAdded");
		} catch (NumberFormatException ex) {
			error.setText("The Id field and the budged field only accept numbers");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

	}

	public void updateUserBalance(int id, int balance) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			CallableStatement callableStatement = con.prepareCall("{call UpdateUserBudget(?,?)}");
			callableStatement.setInt(1, id);
			callableStatement.setInt(2, balance);
			callableStatement.executeUpdate();
			callableStatement.close();

		} catch (SQLException ex) {

		} catch (NumberFormatException ex) {

		} catch (ClassNotFoundException e1) {

		}
	}

	public void deleteUserSql(User user) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			CallableStatement callableStatement = con.prepareCall("{call Deleteuser(?)}");
			if (user != null) {
				callableStatement.setInt(1, user.getId());
				callableStatement.executeUpdate();
				table.getItems().remove(user);
				clearTextFields();
				table.refresh();
				error.setText("User Deleted");
			} else {
				error.setText("User Not Found");
			}
			callableStatement.close();
		} catch (Exception ex) {
			
		}
	}
	private void clearTextFields() {
		for(int i=0;i<textFields.length;i++) {
			textFields[i].setText(null);;
		}
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

	public  HBox createHBox(Label label, TextField textField) {
		HBox hBox = new HBox(label, textField);
		hBox.setSpacing(15);
		hBox.setAlignment(Pos.CENTER);
		return hBox;
	}

	public  Button createStyledButton(String text) {
		Button button = new Button(text);
		button.setStyle("-fx-background-color: #6C22A6; " + "-fx-text-fill: white; " + "-fx-background-radius: 5px; "
				+ "-fx-font-size: 16px; -fx-font-weight: bold;");
		button.setEffect(createGlowingShadow());
		return button;
	}

	public User getUser() {
		if (searchtxt.getText() != null && !searchtxt.getText().trim().equals("")) {
			int search = Integer.parseInt(searchtxt.getText().trim());
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
						"Maitech123");
				PreparedStatement preparedStatement = con.prepareStatement("SELECT * FROM user WHERE UserID = ?");
				preparedStatement.setInt(1, search);
				ResultSet rs = preparedStatement.executeQuery();
				if (rs.next()) {
					User user = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
							rs.getString(5), rs.getString(6), rs.getBigDecimal(7));
					preparedStatement.close();
					rs.close();
					return user;
				}
				return null;
			} catch (Exception ex) {

				ex.printStackTrace();
				return null;
			}

		} else if (table.getSelectionModel().getSelectedItem() != null) {
			return ((User) table.getSelectionModel().getSelectedItem());
		} else {
			return null;
		}
	}

	

	
	
	private LinearGradient createLinearGradient(Color startColor, Color endColor) {
		Stop[] stops = new Stop[] { new Stop(0, startColor), new Stop(1, endColor) };
		return new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
	}

	private Border createGlowingBorder() {
		BorderStroke borderStroke = new BorderStroke(Color.PURPLE, BorderStrokeStyle.SOLID, new CornerRadii(10),
				BorderStroke.THIN);

		return new Border(borderStroke);
	}

	private DropShadow createGlowingShadow() {
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.MEDIUMPURPLE);
		shadow.setWidth(15);
		shadow.setHeight(15);
		return shadow;
	}

}
