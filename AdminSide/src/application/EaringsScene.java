package application;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.Date;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class EaringsScene {
	private DatePicker start;
	private TableView<Earnings> table;
	private BorderPane root = new BorderPane();

	public EaringsScene() {
		this.start = new DatePicker();
		root.setStyle("-fx-background-color: black");
		HBox hbox = new HBox(5, start, createStyledLabel("Start"));
		Image image = new Image("Moneeey.jpg");
		hbox.setAlignment(Pos.CENTER);
		root.setStyle("-fx-background-image: url('" + image.getUrl() + "'); -fx-background-size: cover;");

		table = new TableView<Earnings>();
		table.setMaxWidth(350);
		table.setMaxHeight(400);
		table.setStyle(
				"-fx-border-color: black; -fx-border-width: 2px; -fx-background-color: white; -fx-text-fill: black;");
		table.setEffect(createGlowingShadow());
		TableColumn date = new TableColumn<>("date ");
		date.setMinWidth(150);
		date.setCellValueFactory(new PropertyValueFactory<Earnings, Date>("date"));
		TableColumn total = new TableColumn<>("Total:");
		total.setMinWidth(150);
		total.setCellValueFactory(new PropertyValueFactory<Earnings, BigDecimal>("total"));

		table.getColumns().addAll(date, total);
		VBox vbox = new VBox(5, hbox, table);
		vbox.setAlignment(Pos.CENTER);
		root.setCenter(vbox);
		Button calc = createStyledButton("Calc");
		Button close = createStyledButton("Close");
		close.setOnAction(e -> {
			Main.admin.getScene().setRoot(Main.admin.getRoot());
		});
		calc.setOnAction(e -> {
			fillClass();
			fillTable();
		});
		HBox hbox2 = new HBox(10, calc, close);
		hbox2.setAlignment(Pos.CENTER);
		root.setBottom(hbox2);

	}

	public static Button createStyledButton(String text) {
		Button button = new Button(text);
		button.setStyle("-fx-background-color: #83C0C1; " + "-fx-text-fill: white; " + "-fx-background-radius: 5px; "
				+ "-fx-font-size: 16px; -fx-font-weight: bold;");
		button.setEffect(createGlowingShadow());
		return button;
	}

	public void fillTable() {
		try {
			table.getItems().clear();
			LocalDate startDate = start.getValue();
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
						"Maitech123");
				Statement stat = con.createStatement();
				ResultSet rs = stat.executeQuery("SELECT * FROM earnings WHERE EarningsDate =" + startDate.toString() + ";");

				while (rs.next()) {
					System.out.println("here");
					table.getItems().add(new Earnings(rs.getDate(1), rs.getBigDecimal(2)));
				}
			
			table.refresh();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Label createStyledLabel(String text) {
		Label label = new Label(text);
		label.setStyle("-fx-text-fill: black; -fx-font-size: 16px; -fx-font-weight: bold;");
		label.setEffect(createGlowingShadow());

		label.setMaxWidth(170);
		label.setMinWidth(170);
		label.setAlignment(Pos.CENTER);
		return label;
	}

	public static DropShadow createGlowingShadow() {
		DropShadow shadow = new DropShadow();
		shadow.setColor(Color.BLACK);
		shadow.setWidth(15);
		shadow.setHeight(15);
		return shadow;
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

	public void fillClass() {
	    try {
	        LocalDate startDate = start.getValue();

	        // Establish database connection
	        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
	                "Maitech123")) {
	            String sql = "INSERT INTO earnings (EarningsDate, TotalEarnings) SELECT ?, SUM(Amount) AS TotalEarnings FROM payment WHERE PaymentDate = ?";

	            try {
	                if (startDate != null )	{
	                        CallableStatement callableStatement = con.prepareCall(sql);
	                        callableStatement.setString(1, startDate.toString());
	                        callableStatement.setString(2, startDate.toString());
	                        callableStatement.executeUpdate();
	                        callableStatement.close();
	                    }
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }

	        // Refresh the TableView
	        table.refresh();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public BorderPane getRoot() {
		return root;
	}
}
