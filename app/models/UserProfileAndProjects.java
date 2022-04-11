package models;

import java.util.List;
import java.util.Objects;

/**
 * Combines the data of User Profile and Tweets related to that User into single object.
 *
 * @author Dmitriy Fingerman
 * @version 1.0.0
 */

public class UserProfileAndProjects {

    /**
     * User Profile object
     */
    private Employer employerProfile;

    /**
     * List to of tweet objects
     */
    private List<Display> projects;

    /**
     * Creates new object based on given parameters
     *
     * @param userProfile User Profile
     * @param tweets      List of tweets
     */
    public UserProfileAndProjects(Employer employerProfile, List<Display> projects) {
        this.employerProfile = employerProfile;
        this.projects = projects;
    }

    /**
     * Returns User Profile object
     *
     * @return user profile object
     */
    public Employer getEmployer() {
        return employerProfile;
    }

    /**
     * Sets user profile object based
     *
     * @param userProfile user profile object.
     */
    public void setEmployer(Employer userProfile) {
        this.employerProfile = employerProfile;
    }

    /**
     * Returns list of tweets associated with this user
     *
     * @return list of tweets
     */
    public List<Display> getProjects() {
        return projects;
    }

    /**
     * Sets list of tweets
     *
     * @param tweets list of tweets
     */
    public void setProjects(List<Display> projects) {
        this.projects = projects;
    }


    /**
     * Returns string representation of this model
     *
     * @return string representation of this model
     */
    public String toString() {

        String projectsAsString = projects.stream()
                .map(x -> x.toString())
                .reduce("", (a, b) -> a + "\n" + b);

        return employerProfile.toString() + "\n" + projectsAsString;
    }

    /**
     * Checks equality of two UserProfileAndTweets objects
     *
     * @param o Tweet to compare to
     * @return result of check
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserProfileAndProjects)) return false;
        UserProfileAndProjects that = (UserProfileAndProjects) o;
        return Objects.equals(getEmployer(), that.getEmployer()) &&
                Objects.equals(getProjects(), that.getProjects());
    }


    /**
     * Creates a hashcode
     *
     * @return hashcode
     */
    @Override
    public int hashCode() {

        return Objects.hash(employerProfile, projects);
    }
}