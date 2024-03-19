package application;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;

public class myEvents implements EventHandler {
	public static UserManagment manager=new UserManagment();
public static DeviceManagment device=new DeviceManagment(null);
	@Override
	public void handle(Event e) {
		if(e.getSource() instanceof Button) {
			if(((Button)e.getSource()).getId().equals("InsertUser")) {
				manager.insertUser();
			}else if(((Button)e.getSource()).getId().equals("SearchUser")) {
				manager.SearchScene();
			}else if(((Button)e.getSource()).getId().equals("ConfirmUser")) {
				manager.insertUserSQL();
			}else if(((Button)e.getSource()).getId().equals("Manage User")) {
				Main.admin.getScene().setRoot(manager.getRoot());
			}else if(((Button)e.getSource()).getId().equals("DeviceAdder")) {
				Main.admin.addDevice();
			}else if(((Button)e.getSource()).getId().equals("ConfirmDeviceAdding")) {
				Main.admin.confimDeviceSQL();
			}else if(((Button)e.getSource()).getId().contains("DeviceStatus")) {
				Devices temp=getDevice(((Button)e.getSource()).getId().split(",")[0]);
				device.setDevice(temp);
				device.show();
			}else if(((Button)e.getSource()).getId().equals("UserBack")) {
				manager.close();
			}
		}
	}
	public Devices getDevice(String ip) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gaming_cafe", "root",
					"Maitech123");
			Statement stat = con.createStatement();
            ResultSet rs = stat.executeQuery("SELECT * FROM device WHERE DeviceID = '" + ip + "'");
			Devices temp=null;
            if (rs.next()) {
                temp = new Devices(rs.getString(1), rs.getString(2), rs.getString(3), rs.getDate(5), rs.getInt(6));
            }
            return temp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
