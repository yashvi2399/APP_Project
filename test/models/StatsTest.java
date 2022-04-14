package models;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import models.*;

public class StatsTest {

	@Test
	public void testGetWord() {
		Stats st = new Stats();
		st.setWord("the");
		assertEquals("the", st.getWord());
	}


	@Test
	public void testGetCount() {
		Stats st = new Stats();
		st.setCount((long) 21);
		assertEquals((long)21, (long)st.getCount());
	}
	
	@Test
	public void testCountrysetName() {
		Country country = new Country();
		country.setName("testCountry");
		assertEquals("testCountry",country.getName());
	}
	
	@Test
	public void testJobsetName() {
		Jobs job = new Jobs();
		job.setName("testName");
		assertEquals("testName",job.getName());
	}
	
	@Test
	public void testLocationsetName() {
		Location loc = new Location();
		Country cont = new Country();
		loc.setCountry(cont);
		assertEquals(cont,loc.getCountry());
	}
	
	@Test
	public void testGet_isEmail_verified() {
		Status stat = new Status();
		stat.setEmail_verified(true);
		assertEquals(true,stat.isEmail_verified());
		
	}
	
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
	public void testPreview_description() {
		Display dis = new Display();
		dis.setPreview_description("fixed");
		assertEquals("fixed",dis.getPreview_description());
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
