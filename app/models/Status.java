package models;

/**
 * @author Abhishek Mittal
 *
 */
public class Status {

	boolean email_verified;

	/**
	 * @return boolean value
	 */
	public boolean isEmail_verified() {
		return email_verified;
	}

	/**
	 * @param sets boolean value for email verification
	 */
	public void setEmail_verified(boolean email_verified) {
		this.email_verified = email_verified;
	}
}
