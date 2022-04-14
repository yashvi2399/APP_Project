package models;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import models.*;

public class EmployerTest {

	@Test
	public void testGetId() {
		Employer emp = new Employer();
		emp.setId("5411241");
		assertEquals("5411241",emp.getId());
	}


	@Test
	public void testGetUsername() {
		Employer emp = new Employer();
		emp.setUsername("vw1525394vw");
		assertEquals("vw1525394vw",emp.getUsername());
	}


	@Test
	public void testGetLocation() {
		Employer emp = new Employer();
		Location location = new Location();
		emp.setLocation(location);
		assertEquals(location,emp.getLocation());
	}


	@Test
	public void testGetRole() {
		Employer emp = new Employer();
		emp.setRole("employer");
		assertEquals("employer",emp.getRole());
	}


	@Test
	public void testGetRegistration_date() {
		Employer emp = new Employer();
		emp.setRegistration_date(1649871918);
		assertEquals("Apr 13 2022",emp.getRegistration_date());
	}


	@Test
	public void testGetLimited_account() {
		Employer emp = new Employer();
		emp.setLimited_account("false");
		assertEquals("false",emp.getLimited_account());
	}


	@Test
	public void testGetDisplay_name() {
		Employer emp = new Employer();
		emp.setDisplay_name("vw1525394vw");
		assertEquals("vw1525394vw",emp.getDisplay_name());
	}

	@Test
	public void testGetChosen_role() {
		Employer emp = new Employer();
		emp.setChosen_role("both");
		assertEquals("both",emp.getChosen_role());
	}


	@Test
	public void testStatus() {
		Employer emp = new Employer();
		Status status = new Status();
		emp.setStatus(status);
		assertEquals(status,emp.getStatus());
	}


}
