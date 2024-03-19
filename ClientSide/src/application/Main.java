package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	public static LoginScreen login;
	public static FirstTimeConnecting first;
	@Override
	public void start(Stage primaryStage) {
		try {
			login=new LoginScreen();
			first=new FirstTimeConnecting();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
