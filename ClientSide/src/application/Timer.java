package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

public class Timer extends Application {
	private ScheduledExecutorService executor;
	private int seconds;
	private Label timer;
	private BorderPane root;
	private Button close;
	private LocalDateTime start;
	private LocalDateTime end;
	private int starttime;

	public Timer(double minutes) {
		this.seconds = (int) minutes * 60;
		this.starttime = seconds;
		start = LocalDateTime.now();
	}

	@Override
	public void start(Stage primaryStage) {
		timer = new Label();
		root = new BorderPane();
		close = new Button("Log out");
		root.setCenter(timer);
		root.setBottom(close);
		close.setOnAction(e -> {
			this.stop();
			Main.login.show();
			Thread thread = new Thread(() -> {
	            Main.first.sendStatus(0);
	        });
	        thread.setDaemon(true);
	        thread.start();
			this.insertBookingSQL();
			this.updateUserBalance();
			primaryStage.close();
		});
		Scene scene = new Scene(root, 150, 150);
		primaryStage.setScene(scene);
		primaryStage.show();

		executor = Executors.newSingleThreadScheduledExecutor();

		executor.scheduleAtFixedRate(() -> {
			if (seconds > 0) {
				int second = (int) seconds % 60;
				int min = seconds / 60;
				// Update UI from JavaFX application thread
				Platform.runLater(() -> timer.setText("Seconds remaining: " + min + ":" + second));
				seconds--;
			} else {
				// Update UI from JavaFX application thread
				Platform.runLater(() -> Main.login.show());
				stop();
			}
		}, 0, 1, TimeUnit.SECONDS);
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getEnd() {
		return end;
	}

	public void setEnd(LocalDateTime end) {
		this.end = end;
	}

	public void stop() {
		this.end = LocalDateTime.now();
		executor.shutdown();
	}

	public void updateUserBalance() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			CallableStatement callableStatement = con.prepareCall("{call UpdateUserBudget(?,?)}");
			callableStatement.setInt(1, Main.login.getCurrent().getId());
			callableStatement.setInt(2, -(((this.starttime - this.seconds) / 60) / 20));
			callableStatement.executeUpdate();
			callableStatement.close();

		} catch (SQLException ex) {

		} catch (NumberFormatException ex) {

		} catch (ClassNotFoundException e1) {

		}
	}

	public void insertBookingSQL() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			CallableStatement callableStatement = con.prepareCall("{call InsertBooking(?,?,?,?,?,?)}");
			callableStatement.setInt(1, Main.login.getCurrent().getId());
			callableStatement.setString(2, Main.first.getDevice().getIp());
			callableStatement.setString(3, Date.from(start.atZone(ZoneId.systemDefault()).toInstant()).toGMTString());
			callableStatement.setString(4, Date.from(end.atZone(ZoneId.systemDefault()).toInstant()).toGMTString());
			callableStatement.setInt(5, (int)(Duration.between(start, end).getSeconds()));
			callableStatement.setBigDecimal(6, BigDecimal.valueOf(((this.starttime - this.seconds) / 60) / 20));
			callableStatement.executeUpdate();
			callableStatement.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		} 
	}

}