package application;

import java.math.BigDecimal;

public class Booking {
private int id;
private int userid;
private String deviceID;
private String start;
private String end;
private int duration;
private BigDecimal cose;
public Booking(int id, int userid, String deviceID, String start, String end, int duration, BigDecimal cose) {
	this.id = id;
	this.userid = userid;
	this.deviceID = deviceID;
	this.start = start;
	this.end = end;
	this.duration = duration;
	this.cose = cose;
}
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getUserid() {
	return userid;
}
public void setUserid(int userid) {
	this.userid = userid;
}
public String getDeviceID() {
	return deviceID;
}
public void setDeviceID(String deviceID) {
	this.deviceID = deviceID;
}
public String getStart() {
	return start;
}
public void setStart(String start) {
	this.start = start;
}
public String getEnd() {
	return end;
}
public void setEnd(String end) {
	this.end = end;
}
public int getDuration() {
	return duration;
}
public void setDuration(int duration) {
	this.duration = duration;
}
public BigDecimal getCose() {
	return cose;
}
public void setCose(BigDecimal cose) {
	this.cose = cose;
}

}
