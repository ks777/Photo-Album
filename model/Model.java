package model;

import java.io.IOException;

/**
 * Interface for the Model
 */
public interface Model {
	
	public void writeUser(User u) throws IOException ;
	public User readUser(String ID) throws IOException, ClassNotFoundException;
		
	/**
	 *  Find a User
	 * @param ID (String) of user.
	 * @return true if data was written to specified ID
	 * @throws IOException if IO fails
	 */
	User findUser(String ID) throws IOException;	

	/**
	 * Delete a user
	 * @param ID (String) of user
	 */
	void deleteUser(String ID);


	
}