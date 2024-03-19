package application;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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

public class Devices {
	private int condition = 0;
	private String ip;
	private String name;
	private String spec;
	private Date date;
	private ImageView busy = new ImageView(new Image("blue.png"));
	private ImageView free = new ImageView(new Image("green.png"));
	private ImageView offline = new ImageView(new Image("red.png"));
	private double elecUsage;
	private VBox vbox;
	private Button status;

	public Devices(String ip, String name, String spec, Date date, double elecUsage) {
		this.condition = 0;
		this.ip = ip;
		this.name = name;
		this.spec = spec;
		this.date = date;
		this.elecUsage = elecUsage;
		status = AdminView.createStyledButton(name);
		status.setId(this.ip+",DeviceStatus");
		status.addEventHandler(ActionEvent.ACTION, new myEvents());
		free.setFitWidth(70);
		free.setFitHeight(70);
		busy.setFitWidth(70);
		busy.setFitHeight(70);
		offline.setFitWidth(70);
		offline.setFitHeight(70);
		vbox = new VBox(5, free, status);
		vbox.setAlignment(Pos.CENTER);
		vbox.setMaxSize(140, 140);
	}

	public void setBusy() {
		vbox = new VBox(5, busy, status);
	}

	public void setFree() {
		vbox = new VBox(5, busy, free);
	}

	public void setOffline() {
		vbox = new VBox(5, busy, offline);
	}

	public int getCondition() {
		return condition;
	}

	public void setCondition(int condition) {
		this.condition = condition;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public ImageView getBusy() {
		return busy;
	}

	public void setBusy(ImageView busy) {
		this.busy = busy;
	}

	public ImageView getFree() {
		return free;
	}

	public void setFree(ImageView free) {
		this.free = free;
	}

	public ImageView getOffline() {
		return offline;
	}

	public void setOffline(ImageView offline) {
		this.offline = offline;
	}

	public double getElecUsage() {
		return elecUsage;
	}

	public void setElecUsage(double elecUsage) {
		this.elecUsage = elecUsage;
	}

	public Button getStatus() {
		return status;
	}

	public void setStatus(Button status) {
		this.status = status;
	}

	public VBox getVbox() {
		return vbox;
	}

	public void setVbox(VBox vbox) {
		this.vbox = vbox;
	}

}
