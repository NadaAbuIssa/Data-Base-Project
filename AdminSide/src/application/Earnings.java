package application;

import java.math.BigDecimal;
import java.util.Date;

public class Earnings {
private Date date;
private BigDecimal total;
@Override
public String toString() {
	return "Earnings [date=" + date + ", total=" + total + "]";
}
public Earnings(Date date,BigDecimal total) {
	this.date=date;
	this.total=total;
}
public Date getDate() {
	return date;
}
public void setDate(Date date) {
	this.date = date;
}
public BigDecimal getTotal() {
	return total;
}
public void setTotal(BigDecimal total) {
	this.total = total;
}
}
