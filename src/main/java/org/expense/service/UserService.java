package org.expense.service;

import org.expense.DAO.UserDAO;
import org.expense.Model.User;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public boolean registerUser(User user){
        if (user.getName() == null || user.getName().isBlank()){
            System.out.println("Name can not be empty!");
            return false;
        }

        if (user.getEmail() == null || user.getEmail().isBlank()){
            System.out.println("Email can not be empty!");
            return false;
        }
        if(!user.getEmail().contains("@gmail.com")){
            System.out.println("Please enter valid email.");
            return false ;
        }
        if (user.getPassword() == null || user.getPassword().length() < 4){
            System.out.println("Password must contain at least 4 characters.");
            return false ;
        }
        return userDAO.registerUser(user);
    }

    public User loginUser(String email,String password){
        if (email == null || email.isBlank()){
            System.out.println("Email can not be empty!");
            return null;
        }
        if(!email.contains("@gmail.com")){
            System.out.println("Please enter valid email.");
            return null ;
        }
        if (password == null || password.length() < 4){
            System.out.println("Password must contain at least 4 characters.");
            return null ;
        }
        return userDAO.loginUser(email,password);
    }
}
