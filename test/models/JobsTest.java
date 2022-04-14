package models;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

class JobsTest {

	@Test
	public void testsetName() {
		Jobs job = new Jobs();
		job.setName("testName");
		assertEquals("testName",job.getName());
	}

}
