package com.example.currency;

import android.util.JsonReader;
import android.util.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class LatestReader {
	private JsonReader reader;
	private Latest latest;

	LatestReader(InputStream in) throws UnsupportedEncodingException {
		reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		latest = new Latest();
	}

	public Latest read() throws IOException {
		try {
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals("disclaimer")) {
					latest.setDisclaimer(reader.nextString());
				} else if (name.equals("license")) {
					latest.setLicense(reader.nextString());
				} else if (name.equals("timestamp")) {
					latest.setTimestamp(reader.nextString());
				} else if (name.equals("base")) {
					latest.setBase(reader.nextString());
				} else if (name.equals("rates")) {
					latest.setRates(readRates());
				}
			}
			reader.endObject();
		} finally {
			reader.close();
		}

		return latest;
	}

	public List<Pair<String, Double>> readRates() throws IOException {
		List<Pair<String, Double>> rates = new ArrayList<Pair<String, Double>>();

		reader.beginObject();
		while (reader.hasNext()) {
			rates.add(new Pair<String, Double>(reader.nextName(), reader.nextDouble()));
		}
		reader.endObject();

		return rates;
	}
}
