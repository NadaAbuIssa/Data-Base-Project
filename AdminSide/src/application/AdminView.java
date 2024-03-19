package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class AdminView {
	public Stage stage = new Stage();
	public HBox hbox1 = new HBox();
	public BorderPane root = new BorderPane();
	public Label error = createStyledLabel("");
	public Label[] labels = new Label[5];
	public TextField[] textfields = new TextField[5];
	public Scene scene;

	public AdminView() {
		rootMaker();
	}

	public void show() {
		stage.show();
	}

	public void close() {
		stage.close();
	}

	public void BackFromDevice() {
		fillDevices();
		hbox1.setAlignment(Pos.CENTER);
		root.setTop(hbox1);
		root.requestLayout();
	}

	public void rootMaker() {
		error.setAlignment(Pos.CENTER);
		Button user = createStyledButton("Manage Users");
		Button summary=createStyledButton("Summary");
		summary.setOnAction(e->{
			EaringsScene earning=new EaringsScene();
			this.getScene().setRoot(earning.getRoot());
		});
		user.setId("Manage User");
		user.addEventHandler(ActionEvent.ACTION, new myEvents());
		Button close = createStyledButton("Close Program");
		Button addDevice = createStyledButton("Add Device");
		close.setOnAction(e -> {
			System.exit(0);
		});

		addDevice.addEventHandler(ActionEvent.ACTION, new myEvents());
		addDevice.setId("DeviceAdder");
		HBox buttons_Add = new HBox();

		buttons_Add.getChildren().addAll(summary,user, addDevice, close);
		buttons_Add.setSpacing(10);
		buttons_Add.setAlignment(Pos.CENTER);
		buttons_Add.setPadding(new Insets (10));
		Image image=new Image("BG.jpg");
		buttons_Add.setAlignment(Pos.BOTTOM_CENTER);
		HBox holder=new HBox();
		holder.setMinHeight(70);
		root.setBottom(holder);
		root.setCenter(buttons_Add);
		root.setStyle("-fx-background-image: url('" + image.getUrl() + "'); -fx-background-size: cover;");
		fillDevices();
		
		HBox holder2=new HBox();
		holder2.setMinHeight(40);
		VBox vbox=new VBox(5,holder2,hbox1);
		root.setTop(vbox);
		root.requestLayout();
		scene = new Scene(root, 1280, 720);
		stage.setFullScreen(true);
		stage.setScene(scene);
	}

	public Scene getScene() {
		return this.scene;
	}

	public BorderPane getRoot() {
		return this.root;
	}

	public void addDevice() {
		error.setAlignment(Pos.CENTER);
		for (int i = 0; i < textfields.length; i++) {
			textfields[i] = createBorderedTextField();
		}
		labels[0] = createStyledLabel("IP");
		labels[1] = createStyledLabel("Name");
		labels[2] = createStyledLabel("spec");
		labels[3] = createStyledLabel("date");
		labels[4] = createStyledLabel("Elec usage");
		Button add = createStyledButton("Confirm");
		Button cancel = createStyledButton("Close");
		add.setId("ConfirmDeviceAdding");
		add.addEventHandler(ActionEvent.ACTION, new myEvents());
		HBox[] hboxs = new HBox[5];
		for (int i = 0; i < hboxs.length; i++) {
			hboxs[i] = createHBox(labels[i], textfields[i]);
			hboxs[i].setAlignment(Pos.CENTER);
			hboxs[i].setSpacing(10);
		}
		HBox hbox = new HBox(10, add, cancel);
		hbox.setSpacing(10);
		hbox.setAlignment(Pos.CENTER);
		BorderPane root = new BorderPane();
		
		VBox vbox = new VBox(hboxs);
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);
		root.setTop(error);
		BorderPane rootFill=new BorderPane();
		rootFill.setCenter(vbox);
		rootFill.setStyle("-fx-background-color: rgba(131, 192, 193, 0.9);");
		root.setCenter(rootFill);
		VBox holderleft=new VBox();
		holderleft.setMinWidth(150);
		VBox holderRight=new VBox();
		holderRight.setMinWidth(150);
		hbox.setPadding(new Insets(15, 15, 15, 15));
		root.setBottom(hbox);
		root.setLeft(holderleft);
		root.setRight(holderRight);
		rootFill.setMaxHeight(450);
		rootFill.setMaxWidth(420);
		rootFill.setMinHeight(450);
		rootFill.setMinWidth(420);
		HBox holdertop=new HBox();
		holdertop.setMinHeight(150);
		root.setTop(holdertop);
		Image image=new Image("BGDevice.jpg");
		root.setStyle("-fx-background-image: url('" + image.getUrl() + "'); -fx-background-size: cover;");

		scene.setRoot(root);
		stage.setScene(scene);
		stage.show();
		cancel.setOnAction(ev -> {
			scene.setRoot(this.root);
			stage.setScene(scene);
		});

	}

	public void confimDeviceSQL() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
			CallableStatement callableStatement = con.prepareCall("{call InsertDevice(?,?,?,?,?,?)}");
			boolean flag = false;
			for (int i = 0; i < textfields.length; i++) {
				if (textfields[i] == null || textfields[i].getText().trim().equals("")) {
					flag = true;
					break;
				}
			}
			if (flag) {
				error.setText("there are some textfields are Empty!!!");
			} else {
				callableStatement.setString(1, textfields[0].getText());
				callableStatement.setString(2, textfields[1].getText());
				callableStatement.setString(3, textfields[2].getText());
				callableStatement.setInt(4, 0);
				try {
					callableStatement.setDate(5, new java.sql.Date(
							new SimpleDateFormat("yyyy-MM-dd").parse(textfields[3].getText().trim()).getTime()));
				} catch (ParseException e1) {
					error.setText("Error Parsing Date ( yyyy-MM-dd)");
				}
				callableStatement.setDouble(6, Double.parseDouble(textfields[4].getText()));
				callableStatement.executeUpdate();
				callableStatement.close();
				error.setText("Device Added!");

				fillDevices();
				hbox1.setAlignment(Pos.CENTER);
				root.setTop(hbox1);
				root.requestLayout();
			}
		} catch (NumberFormatException ex) {
			error.setText("The ElectricityUsage field only accept numbers");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException ex) {
			error.setText("IP already in use");
		}
	}

	public static LinearGradient createLinearGradient(Color startColor, Color endColor) {
		Stop[] stops = new Stop[] { new Stop(0, startColor), new Stop(1, endColor) };
		return new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
	}

	public void fillDevices() {
		hbox1 = new HBox();
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM device");

			while (rs.next()) {
				hbox1.getChildren().add(
						new Devices(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(5), rs.getDouble(6))
								.getVbox());
			}
			hbox1.setAlignment(Pos.CENTER);
			hbox1.setSpacing(15);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reciever() {
		final int portNumber = 49152; // Specify the port you want to listen on

		try (ServerSocket serverSocket = new ServerSocket(portNumber)) {
			System.out.println("Server is running and listening on port " + portNumber);

			while (true) {
				// Accept incoming client connections
				Socket clientSocket = serverSocket.accept();
				System.out.println("New client connected: " + clientSocket);

				// Create a new thread to handle the client
				Thread clientThread = new Thread(new ClientHandler(clientSocket));
				clientThread.start();
			}
		} catch (IOException e) {
			System.err.println("Error occurred while running the server: " + e.getMessage());
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

	public static HBox createHBox(Label label, TextField textField) {
		HBox hBox = new HBox(label, textField);
		hBox.setSpacing(15);
		hBox.setAlignment(Pos.CENTER);
		return hBox;
	}

	public static Button createStyledButton(String text) {
		Button button = new Button(text);
		button.setStyle("-fx-background-color: #83C0C1; " + "-fx-text-fill: white; " + "-fx-background-radius: 5px; "
				+ "-fx-font-size: 16px; -fx-font-weight: bold;");
		button.setEffect(createGlowingShadow());
		return button;
	}

	public static DropShadow createGlowingShadow() {
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.WHITE);
		shadow.setWidth(15);
		shadow.setHeight(15);
		return shadow;
	}

	public Devices getDevice(String ip) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM device WHERE DeviceID = '" + ip + "'");
			if (rs.next()) {
				return new Devices(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(5), rs.getDouble(6));
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

class ClientHandler implements Runnable {
	public final Socket clientSocket;

	public ClientHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		BufferedReader in;
		Devices temp = null;
		try {
			String inputLine;
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			while ((inputLine = in.readLine()) != null) {
				String stat = (clientSocket.getInetAddress().getHostAddress() + "," + inputLine);
				System.out.println(inputLine);
				String[] str = stat.split(",");
				temp = Main.admin.getDevice(str[0]);
				if (temp != null) {
					if (str[1].equals("0")) {
						temp.setFree();
					} else if (str[1].equals("1")) {
						temp.setBusy();

					}
				}
			}
		} catch (IOException e) {
			if (temp != null) {
				temp.setOffline();
			}

		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				System.err.println("Error occurred while closing client socket: " + e.getMessage());
			}
		}
	}
}
