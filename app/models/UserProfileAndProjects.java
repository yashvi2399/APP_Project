package models;

import java.util.List;
import java.util.Objects;

/**
 * Combines the data of Employer Profile and Projects related to that client into single object.
 *
 * @author Yashvi Pithadia
 */
public class UserProfileAndProjects {

    /**
     * User Profile object
     */
    private Employer employerProfile;

    /**
     * List to of project objects
     */
    private List<Display> projects;


    /**
     * Creates new object based on given parameters
     *
     * @param employerProfile Employer Profile
     * @param projects      List of projects
     */
    public UserProfileAndProjects(Employer employerProfile, List<Display> projects) {
        this.employerProfile = employerProfile;
        this.projects = projects;
    }

    /**
     *
     * @return employer object
     */
    public Employer getEmployer() {
        return employerProfile;
    }

    /**
     *
     * @param employerProfile user profile object.
     */
    public void setEmployer(Employer userProfile) {
        this.employerProfile = employerProfile;
    }

    /**
     * Returns list of project associated with this employer
     *
     * @return list of project
     */
    public List<Display> getProjects() {
        return projects;
    }

    /**
     * Sets list of projects
     *
     * @param projects: projects
     */
    public void setProjects(List<Display> projects) {
        this.projects = projects;
    }


}