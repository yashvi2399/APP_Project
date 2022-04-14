package models;

import static org.junit.Assert.assertEquals;
import org.junit.Test;


class CountryTest {

	@Test
	public void testsetName() {
		Country country = new Country();
		country.setName("testCountry");
		assertEquals("testCountry",country.getName());
	}

}
