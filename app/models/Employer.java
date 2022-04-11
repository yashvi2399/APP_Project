package models;

import java.text.SimpleDateFormat;

/**
 * @author Abhishek Mittal
 *
 */
public class Employer {
	
	String id;
	String username;
	String role;
	long registration_date;
	String limited_account;
	String display_name;
	String chosen_role;
	Location location;
	Status status;
	
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	/**
	 * Fetch Owner ID
	 * @return Owner ID
	 */
	public String getId() {
		return id;
	}
	/**
	 * Store Owner ID
	 * @param owner_id: Owner ID
	 */
	public void setId(String owner_id) {
		this.id = owner_id;
	}
	
	/**
	 * Fetch User Name
	 * @return User Name
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * Store User Name
	 * @param username: User Name
	 */
	public void setUsername(String username) {
		this.username = username;
	}	
	
	/**
	 * Fetch Owner Role
	 * @return Owner ID
	 */
	public String getRole() {
		return role;
	}
	/**
	 * Store Owner Role
	 * @param role: Owner Role
	 */
	public void setRole(String role) {
		this.role = role;
	}
	
	/**
	 * Fetch Registration date 
	 * @return Registration date 
	 */
	public String getRegistration_date() {
		
		SimpleDateFormat sdate;
		sdate = new SimpleDateFormat("MMM dd yyyy");
		return sdate.format(this.registration_date*1000);
	}
	/**
	 * Store Registration date 
	 * @param reg_date Registration date 
	 */
	public void setRegistration_date(long reg_date) {
		this.registration_date = reg_date;
	}
	
	
	/**
	 * Fetch Limited Account Value
	 * @return Limited Account Value
	 */
	public String getLimited_account() {
		return limited_account;
	}
	
	/**Store Limited Account Value
	 * @param limited_acc:Limited Account Value
	 */
	public void setLimited_account(String limited_acc) {
		this.limited_account = limited_acc;
	}
	
	/**
	 * Fetch Display Name of Owner
	 * @return Display Name of Owner
	 */
	public String getDisplay_name() {
		return display_name;
	}
	
	/**
	 * Store Display Name of Owner
	 * @param display_name: Display Name of Owner
	 */
	public void setDisplay_name(String display_name) {
		this.display_name = display_name;
	}
	
	/**
	 * Fetch Owners Chosen Role
	 * @return Owners Chosen Role
	 */
	public String getChosen_role() {
		return chosen_role;
	}
	
	/**
	 * Store Owners Chosen Role
	 * @param chosen_role: Owners Chosen Role
	 */
	public void setChosen_role(String chosen_role) {
		this.chosen_role = chosen_role;
	}
	
}
