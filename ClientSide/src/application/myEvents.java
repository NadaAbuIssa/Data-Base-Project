package application;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class myEvents implements EventHandler {

	@Override
	public void handle(Event e) {
		if (e.getSource() instanceof Button) {
			if (((Button) e.getSource()).getId().equals("Login")) {
				int check = Main.login.checkLogin();
				if (check == -1) {
					Main.login.setError("Wrong password or UserName!");
				} else {
					Main.login.close();
					Timer time=new Timer(Main.login.getBalance().doubleValue()*20);
					time.start(new Stage());
					Thread thread = new Thread(() -> {
			            Main.first.sendStatus(1);
			        });
			        thread.setDaemon(true);
			        thread.start();
				}
			}else if (((Button) e.getSource()).getId().equals("ConfirmDeviceAdding")) {
				Main.first.confimDeviceSQL();
				Main.first.getStage().close();
				Main.first=new FirstTimeConnecting();
			}
		}

	}

}
