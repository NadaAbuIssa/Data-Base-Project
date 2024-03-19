package application;

import java.math.BigDecimal;
import java.util.Date;

public class Payment {
	private int id;
	private int userid;
	private String method;
	private BigDecimal ammount;
	private Date date;

	public Payment(int id, int userid, String method, BigDecimal ammount, Date date) {
		this.id = id;
		this.userid = userid;
		this.method = method;
		this.ammount = ammount;
		this.date = date;
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

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public BigDecimal getAmmount() {
		return ammount;
	}

	public void setAmmount(BigDecimal ammount) {
		this.ammount = ammount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
