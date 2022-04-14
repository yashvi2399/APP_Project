package models;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import models.*;

public class DisplayTest {

	@Test
	public void testGetOwner_id() {
		Display dis = new Display();
		dis.setOwner_id("61329632");
		assertEquals("61329632",dis.getOwner_id());
	}


	@Test
	public void testGetTitle() {
		Display dis = new Display();
		dis.setTitle("English to Spanish translation");
		assertEquals("English to Spanish translation",dis.getTitle());
	}


	@Test
	public void testGetTime_submitted() {
		Display dis = new Display();
		dis.setTime_submitted(1649871918);
		assertEquals("Apr 13 2022",dis.getTime_submitted());
	}

	@Test
	public void testGetType() {
		Display dis = new Display();
		dis.setType("fixed");
		assertEquals("fixed",dis.getType());
	}


	@Test
	public void testGetSkills() {
		Display dis = new Display();
		Jobs[] jobs = new Jobs[2];
		Jobs job = new Jobs();
		jobs[0] = job;
		dis.setJobs(jobs);
		assertEquals(jobs,dis.getJobs());
	}
	
	

	
}
