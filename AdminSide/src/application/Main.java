package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class Main extends Application {
	public static AdminView admin;

	@Override
	public void start(Stage primaryStage) {
		try {
			admin = new AdminView();
			admin.show();

			Thread thread = new Thread(new Runnable() {
	            @Override
	            public void run() {
	                admin.reciever();
	            }
	        });
	        thread.setDaemon(true);
	        thread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Thread fxThread = new Thread(new Runnable() {
            @Override
            public void run() {
                Application.launch(Main.class, args);
            }
        });
        fxThread.setDaemon(true);
        fxThread.start();
	}
}