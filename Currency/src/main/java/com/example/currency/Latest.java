package com.example.currency;

import android.util.Pair;

import javax.sql.RowSetReader;
import java.util.List;

public class Latest {
	private String disclaimer;
	private String license;
	private String timestamp;
	private String base;
	private List<Pair<String, Double>> rates;

	public void setDisclaimer(String disclaimer) {
		this.disclaimer = disclaimer;
	}

	public String getDisclaimer() {
		return disclaimer;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public String getLicense() {
		return license;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getBase() {
		return base;
	}

	public void setRates(List<Pair<String, Double>> rates) {
		this.rates = rates;
	}

	public List<Pair<String, Double>> getRates() {
		return rates;
	}
}
