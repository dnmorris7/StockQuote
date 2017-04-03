package com.codecrunchcorner.stockquote;

public class StockInfo {

	private String daysLow = "";
	private String daysHigh = "";
	private String yearLow = "";
	private String yearHigh = "";
	private String name = "";
	private String lastTradePriceonly = "";
	private String change = "";
	private String daysRange = "";

	public StockInfo(String daysLow, String daysHigh, String yearlow,
			String yearHigh, String name, String lastTradePriceonly,
			String change, String daysRange) {
		super();
		this.daysLow = daysLow;
		this.daysHigh = daysHigh;
		this.yearLow = yearlow;
		this.yearHigh = yearHigh;
		this.name = name;
		this.lastTradePriceonly = lastTradePriceonly;
		this.change = change;
		this.daysRange = daysRange;
	}

	public String getDaysLow() {
		return daysLow;
	}

	public void setDaysLow(String daysLow) {
		this.daysLow = daysLow;
	}

	public String getDaysHigh() {
		return daysHigh;
	}

	public void setDaysHigh(String daysHigh) {
		this.daysHigh = daysHigh;
	}

	public String getYearLow() {
		return yearLow;
	}

	public void setYearlow(String yearlow) {
		this.yearLow = yearlow;
	}

	public String getYearHigh() {
		return yearHigh;
	}

	public void setYearHigh(String yearHigh) {
		this.yearHigh = yearHigh;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastTradePriceonly() {
		return lastTradePriceonly;
	}

	public void setLastTradePriceonly(String lastTradePriceonly) {
		this.lastTradePriceonly = lastTradePriceonly;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public String getDaysRange() {
		return daysRange;
	}

	public void setDaysRange(String daysRange) {
		this.daysRange = daysRange;
	}

}
