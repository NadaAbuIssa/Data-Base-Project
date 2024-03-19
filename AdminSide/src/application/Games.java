package application;

import java.util.Date;

public class Games {
	private String id;
	private String title;
	private String genre;
	private Date date;
	private String description;
	private String deviceIp;
	public String getDeviceIp() {
		return deviceIp;
	}
	public void setDeviceIp(String deviceIp) {
		this.deviceIp = deviceIp;
	}
	public Games(String id, String title, String genre, Date date, String description,String ip) {
		super();
		this.id = id;
		this.title = title;
		this.genre = genre;
		this.date = date;
		this.description = description;
		this.deviceIp=ip;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "Games [id=" + id + ", title=" + title + ", genre=" + genre + ", date=" + date + ", description="
				+ description + ", IP="+deviceIp+" ]";
	}
	
}
