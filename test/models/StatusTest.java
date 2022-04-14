package models;
import static org.junit.Assert.assertEquals;
import org.junit.Test;


class StatusTest {

	@Test
	void testGet_isEmail_verified() {
		Status stat = new Status();
		stat.setEmail_verified(true);
		assertEquals(true,stat.isEmail_verified());
		
	}

}
