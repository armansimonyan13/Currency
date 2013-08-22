package com.example.currency;

import static org.junit.Assert.*;

import com.example.currency.DbAdapter.SelectedCurrenciesDbAdapter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SelectedCurrenciesDAOTest {

	@Before
	public void setUp() throws Exception {
		// Do nothing
	}

	@After
	public void tearDown() throws Exception {
		// Do nothing
	}

	@Test
	public void testQueryJoined() throws Exception {
		String query = SelectedCurrenciesDbAdapter.QUERY_JOINED;
		String golden = "SELECT _id, name, value FROM selected_currencies" +
				" JOIN available_currencies ON selected_currencies.currency_id = available_currencies._id;";
		assertEquals("Check joined query:", query, golden);
	}
}
