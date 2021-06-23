package beans;

import java.util.Date;

public class Period {

	private long dateFrom;
	private long dateTo;
	
	public Period() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Period(long dateFrom, long dateTo) {
		super();
		this.dateFrom = dateFrom;
		this.dateTo = dateTo;
	}

	public long getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(long dateFrom) {
		this.dateFrom = dateFrom;
	}

	public long getDateTo() {
		return dateTo;
	}

	public void setDateTo(long dateTo) {
		this.dateTo = dateTo;
	}
}
