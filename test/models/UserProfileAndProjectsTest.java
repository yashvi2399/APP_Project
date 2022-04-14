package models;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import models.*;

public class UserProfileAndProjectsTest {
	
	
	Employer emp = new Employer();
	List<Display> proj = new ArrayList<Display>();
	
	@Test
	public void testEmployer() {
		UserProfileAndProjects prof = new UserProfileAndProjects(emp,proj);
		prof.setEmployer(emp);
		assertEquals(emp,prof.getEmployer());
	}
	
	@Test
	public void testProjects() {
		UserProfileAndProjects prof = new UserProfileAndProjects(emp,proj);
		prof.setProjects(proj);
		assertEquals(proj,prof.getProjects());
	}

}
