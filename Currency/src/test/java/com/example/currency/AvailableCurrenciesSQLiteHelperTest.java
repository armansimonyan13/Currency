package com.example.currency;

import org.junit.Test;
import static org.junit.Assert.*;

public class AvailableCurrenciesSQLiteHelperTest {
	@Test
	public void testCreateAvailableCurrenciesTableString() throws Exception {
		String query = AvailableCurrenciesSQLiteHelper.DATABASE_CREATE;
		assertEquals("Test ", query, "false string");
	}
}
