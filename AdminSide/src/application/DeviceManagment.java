package application;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class DeviceManagment {
	private Label error = new Label();
	private Devices device;
	private Stage stage = new Stage();
	private BorderPane root = new BorderPane();
	private Scene scene=new Scene(root,800,800);

	public BorderPane getRoot() {
		return root;
	}

	public void setRoot(BorderPane root) {
		this.root = root;
	}

	public DeviceManagment(Devices device) {
		this.device = device;
	}

	public Devices getDevice() {
		return device;
	}

	public void setDevice(Devices device) {
		if (this.device != null) {
			if (this.device.getIp().equals(device.getIp()))
				return;

		}
		this.device = device;
		this.insideDeviceScene();
		this.show();

	}

	public void show() {
		stage.show();
	}

	public void insideDeviceScene() {
		Label[] label = new Label[6];
		TextField[] textFields = new TextField[6];
		for (int i = 0; i < textFields.length; i++) {
			textFields[i] = createBorderedTextField();
		}
		label[0] = createStyledLabel("IP");
		label[1] = createStyledLabel("name");
		label[2] = createStyledLabel("spec");
		label[4] = createStyledLabel("date");
		label[3] = createStyledLabel("condition");
		label[5] = createStyledLabel("elec Usage");
		textFields[0].setText(device.getIp() + "");
		textFields[1].setText(device.getName() + "");
		textFields[2].setText(device.getSpec() + "");
		textFields[3].setText(device.getCondition() + "");
		textFields[4].setText(device.getDate() + "");
		textFields[5].setText(device.getElecUsage() + "");
		HBox hbox[] = new HBox[6];
		for (int i = 0; i < hbox.length; i++) {
			hbox[i] = new HBox(10, label[i], textFields[i]);

			hbox[i].setAlignment(Pos.CENTER);
			hbox[i].setSpacing(5);
			hbox[i].setMaxWidth(300);
		}

		VBox vbox = new VBox(10, hbox);
		vbox.setAlignment(Pos.CENTER);
		Button delete = createStyledButton("Delete current Device");
		Button update = createStyledButton("Update Device");

		delete.setOnAction(e -> {
			deleteDeviceSQL();
			stage.close();
		});
		update.setOnAction(e -> {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
						"Maitech123");
				CallableStatement callableStatement = con.prepareCall("{call Update_deviceInfo(?,?,?,?,?,?)}");
				boolean flag = false;
				for (int i = 0; i < textFields.length; i++) {
					if (textFields[i] == null || textFields[i].getText().trim().equals("")) {
						flag = true;
						break;
					}
				}
				if (flag) {
					error.setText("there are some textfields are Empty!!!");
				} else {
					try {
						callableStatement.setString(1, textFields[0].getText());
						callableStatement.setString(2, textFields[1].getText());
						callableStatement.setString(3, textFields[2].getText());
						callableStatement.setInt(4, Integer.parseInt(textFields[3].getText()));
						try {
							callableStatement.setDate(5, new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd")
									.parse(textFields[4].getText().trim()).getTime()));
						} catch (ParseException e1) {
							error.setText("Error Parsing Date ( yyyy-MM-dd)");
						}
						callableStatement.setDouble(6, Double.parseDouble(textFields[5].getText()));
						callableStatement.executeUpdate();
						callableStatement.close();
						Main.admin.fillDevices();
						HBox holder2 = new HBox();
						holder2.setMinHeight(40);
						VBox vbox2 = new VBox(15, holder2, Main.admin.hbox1);
						Main.admin.getRoot().setTop(vbox2);
						Main.admin.hbox1.setAlignment(Pos.CENTER);
						Main.admin.getRoot().requestLayout();
						error.setText("Device Update!");
					} catch (Exception ex) {
						error.setText("Wrong input");
					}

				}
			} catch (SQLException ex) {
				error.setText("Error!");
				ex.printStackTrace();
			} catch (NumberFormatException ex) {
				error.setText("The ElectricityUsage field only accept numbers");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		});
		Button close = createStyledButton("close");
		Button manage = createStyledButton("Manage games");
		scene.setRoot(root);
		manage.setOnAction(e -> {
			scene.setRoot(new GameManagment(this.device.getIp()).manageGame());
			stage.setTitle("Manage Games for : " + this.device.getName());
		});
		close.setOnAction(ev -> {
			stage.close();
		});
		HBox hbox2 = new HBox(delete, update, manage, close);
		hbox2.setAlignment(Pos.CENTER);
		hbox2.setPadding(new Insets(15, 15, 15, 15));
		hbox2.setSpacing(10);
		root.setStyle("-fx-background-color: #AC87C5");
		HBox holder = new HBox();
		holder.setMinHeight(50);
		root.setBottom(hbox2);
		root.setCenter(vbox);
		root.setRight(new ImageView(new Image("Animation1.gif")));
		root.setTop(error);
		stage.setScene(scene);
		stage.setTitle("Device :" + this.device.getName());
	}

	public Label getError() {
		return error;
	}

	public void setError(Label error) {
		this.error = error;
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public boolean deleteDeviceSQL() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			CallableStatement callableStatement = con.prepareCall("{call Deletedevice(?)}");
			if (device != null) {
				System.out.println(device.getIp());
				callableStatement.setString(1, device.getIp());
				callableStatement.executeUpdate();
				Main.admin.BackFromDevice();
				return true;
			}
			callableStatement.close();
		} catch (Exception ex) {
			error.setText("Something went wrong");
		}
		return false;
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
		button.setStyle("-fx-background-color: #83C0C1; " + "-fx-text-fill: white; " + "-fx-background-radius: 5px; "
				+ "-fx-font-size: 16px; -fx-font-weight: bold;");
		button.setEffect(createGlowingShadow());
		button.setPrefSize(200, 40);
		return button;
	}

	private DropShadow createGlowingShadow() {
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.WHITE);
		shadow.setWidth(15);
		shadow.setHeight(15);
		return shadow;
	}

}
