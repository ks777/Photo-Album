package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;

public class UserModel implements Model {
	
	public ArrayList<User> all = new ArrayList<User>();
	public User active;
	
	/*Walk through the data directory and read all users into memory */
	public UserModel() {
		File file = new File("data");
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
		
		try {
			Files.walk(Paths.get("data")).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) {
			        try {
						all.add(readUser(filePath.getFileName().toString()));
					} catch (Exception e) {
						e.printStackTrace();
					}
			    }
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	
	/* Find a user already in memory */
	public User findUser(String u_id)
	{
		User present;
		Iterator<User> iterator = all.iterator();
		while(iterator.hasNext()) {
			present = iterator.next();
			if(present.getID().equals(u_id))
				return present;
		}
		return null;
	}

	@Override
	public void deleteUser(String ID) {
		try {
			Files.walk(Paths.get("data")).forEach(filePath -> {
			    if (Files.isRegularFile(filePath)) {
			        try {
						if(filePath.toFile().getName().toString().equals(ID))
							if(filePath.toFile().delete())
								System.out.println("User sucessfully deleted.");
					} catch (Exception e) {
						e.printStackTrace();
					}
			    }
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*Read a user into memory, add to all list*/
	public User readUser(String ID) throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data" + File.separator + ID));
		User u = (User)ois.readObject();
		ois.close();
		return u;
	}
	
	/* Write a user into memory */
	public void writeUser(User u) throws IOException {
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("data" + File.separator + u.getID()));
		oos.writeObject(u);
		oos.close();
	}



}
