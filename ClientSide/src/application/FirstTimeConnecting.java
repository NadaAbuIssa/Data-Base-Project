package application;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class FirstTimeConnecting {
	private Label[] labels = new Label[3];
	private TextField[] textfields = new TextField[3];
	private Label error = new Label();
	private Devices device;
	private String adminip;

	FirstTimeConnecting() {
		rootMaker();

		Thread thread = new Thread(() -> {
			Main.first.sendStatus(0);
		});
		thread.setDaemon(true);
		thread.start();
	}

	public Devices getDevice() {
		return device;
	}

	public void setDevice(Devices device) {
		this.device = device;
	}

	public void sendStatus(int i) {
		int startport = 49152;
		Socket socket;
		try {
			socket = new Socket(adminip, startport);
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			out.println(i);
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

	public void rootMaker() {
		try {
			device = getDevice(InetAddress.getLocalHost().getHostAddress());
			if (device != null) {
				Main.login.show();
			} else {
				addAdminID();
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Label createStyledLabel(String text) {
		Label label = new Label(text);
		label.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
		return label;
	}

	private TextField createBorderedTextField() {
		TextField textField = new TextField();
		textField.setStyle(
				"-fx-border-color: #4CAF50; -fx-border-width: 2px; -fx-background-color: #2E2E2E; -fx-text-fill: white;");
		textField.setEffect(createGlowingShadow());
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
		button.setStyle("-fx-background-color: #4CAF50; " + "-fx-text-fill: white; " + "-fx-background-radius: 5px; "
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

	private LinearGradient createLinearGradient(Color startColor, Color endColor) {
		Stop[] stops = new Stop[] { new Stop(0, startColor), new Stop(1, endColor) };
		return new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, stops);
	}

	public void addAdminID() {
		Stage stage = new Stage();
		BorderPane root = new BorderPane();
		TextField txt = new TextField();
		Label lab = new Label("IP Admin");
		Button ok = new Button("Confirm");
		ok.setOnAction(e -> {
			if (txt.getText().isEmpty()) {

			} else {
				this.adminip = txt.getText().trim();
				addDevice();
				stage.close();
			}
		});
		root.setCenter(new HBox(5, lab, txt));
		root.setBottom(ok);
		stage.setScene(new Scene(root));
		stage.show();
	}

	private Stage stage = new Stage();

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void addDevice() {
		Rectangle diagonalStrip = new Rectangle(600, 600, createLinearGradient(Color.DEEPSKYBLUE, Color.DARKSLATEGRAY));

		for (int i = 0; i < textfields.length; i++) {
			textfields[i] = createBorderedTextField();
		}
		labels[0] = createStyledLabel("Name");
		labels[1] = createStyledLabel("Spec");
		labels[2] = createStyledLabel("Elec usage");
		Button add = createStyledButton("Confirm");
		add.setId("ConfirmDeviceAdding");
		add.addEventHandler(ActionEvent.ACTION, new myEvents());
		HBox[] hboxs = new HBox[3];
		for (int i = 0; i < hboxs.length; i++) {
			hboxs[i] = createHBox(labels[i], textfields[i]);
		}
		HBox hbox = new HBox(10, add);
		hbox.setAlignment(Pos.CENTER);
		BorderPane root = new BorderPane();
		Rectangle diagonalStrip1 = new Rectangle(600, 600,
				createLinearGradient(Color.DEEPSKYBLUE, Color.DARKSLATEGRAY));
		diagonalStrip.setRotate(45);
		root.getChildren().add(diagonalStrip1);
		VBox vbox = new VBox(hboxs);
		root.setTop(error);
		root.setCenter(vbox);
		root.setBottom(hbox);
		stage.setScene(new Scene(root));
		stage.show();

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

	public void confimDeviceSQL() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
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
				try {
					callableStatement.setString(1, InetAddress.getLocalHost().getHostAddress());
					callableStatement.setString(2, textfields[0].getText());
					callableStatement.setString(3, textfields[1].getText());
					callableStatement.setInt(4, 0);
					callableStatement.setDate(5, new java.sql.Date(new Date().getTime()));
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				callableStatement.setDouble(6, Double.parseDouble(textfields[2].getText()));
				callableStatement.executeUpdate();
				callableStatement.close();
				error.setText("Device Added!");

			}
		} catch (NumberFormatException ex) {
			error.setText("The ElectricityUsage field only accept numbers");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException ex) {
			error.setText("IP already in use");
		}
	}

}
