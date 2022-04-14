package models;

import static org.junit.Assert.assertEquals;
import org.junit.Test;


class LocationTest {

	@Test
	public void testsetName() {
		Location loc = new Location();
		Country cont = new Country();
		loc.setCountry(cont);
		assertEquals(cont,loc.getCountry());
	}

}
