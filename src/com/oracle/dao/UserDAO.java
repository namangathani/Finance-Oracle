package com.oracle.dao;

import com.oracle.beans.User;

public interface UserDAO {
    User login(String username, String password);

	User getUserByUsername(String username);

	void printAllUsers();
	void addUser(User user);
}
