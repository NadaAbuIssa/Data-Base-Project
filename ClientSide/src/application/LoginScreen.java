package application;

import java.io.File;
import javafx.application.Platform;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.Timer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class LoginScreen {
	private Stage primaryStage;
	private GridPane grid = new GridPane();
	private Scene scene = new Scene(grid);
	private TextField user = new TextField();
	private PasswordField pass = new PasswordField();
	private Label userlb = new Label("UserName :");
	private Label passlb = new Label("PassWord :");
	private Label error = new Label("");
	private Button close = new Button("close");
	private Button sign = new Button("Login");
	private User current=null;

	public void setError(String string) {
		error.setText(string);
	}

	public LoginScreen() {
		this.primaryStage = new Stage();
		rootMaker();
	}

	private void rootMaker() {

		grid.setAlignment(Pos.CENTER);
		grid.add(userlb, 0, 0);
		grid.add(user, 1, 0);
		grid.add(passlb, 0, 1);
		grid.add(pass, 1, 1);
		grid.add(error, 2, 2);
		grid.add(close, 0, 9);
		grid.add(sign, 3, 9);
		BorderPane root=new BorderPane();
		Image image=new Image("Lock.gif");
		ImageView view=new ImageView(image);
		VBox vbox=new VBox(15,view,grid);
		vbox.setAlignment(Pos.CENTER);
		root.setCenter(vbox);
		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		primaryStage.setFullScreen(true);
		scene.setRoot(root);
		primaryStage.setScene(scene);
		primaryStage.setAlwaysOnTop(true);
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				event.consume();
			}
		});
		sign.setId("Login");
		sign.addEventHandler(ActionEvent.ACTION, new myEvents());
		close.setOnAction(e -> {
			this.close();
		});
	}

	public void show() {
		primaryStage.show();
	}

	public void close() {
		primaryStage.close();
	}

	public int checkLogin() {
		int index = -1;
		if (user.getText().equals("") || user.getText().equals(null)) {
			error.setText("UserName is Empty");
		} else {
			if (pass.getText().equals("") || pass.getText().equals(null)) {
				error.setText("Password is Empty");
			} else {
				String userString = user.getText();
				String passString = pass.getText();
				current = getUser(userString);
				if (current != null) {
					if (passString.equals(current.getPassword())) {
						index = 0;
					}
				}

			}
		}
		return index;
	}

	public String getUserName() {
		return this.user.getText().trim();
	}

	public User getUser(String username) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			Statement stat = con.createStatement();
			ResultSet rs = stat.executeQuery("SELECT * FROM user WHERE Username = '" + username + "'");
			User temp = null;
			if (rs.next()) {
				temp = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						 rs.getString(6),rs.getBigDecimal(7));
				return temp;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public BigDecimal getBalance() {
		
		return current.getBudget();
	}

	public User getCurrent() {
		return current;
	}

	public void setCurrent(User current) {
		this.current = current;
	}
}
