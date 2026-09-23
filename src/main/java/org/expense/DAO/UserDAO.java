package org.expense.DAO;

import org.expense.Model.User;
import org.expense.util.DatabaseConnection;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public boolean registerUser(User user){
        String sql = "INSERT INTO users(name,email,password) VALUES(?,?,?)";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());

            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            if(e.getErrorCode() == 1062){
                // With code 1062,we are handling duplicate key entry exception separately.
                // For MySQL, error code 1062 represents a duplicate key exception
                System.out.println("An account with this email already exists.");
            }else {
                System.out.println("Registration Failed.");
            }

        }
        return false;
    }

    public User loginUser(String email,String password){
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){

            statement.setString(1,email);
            statement.setString(2,password);

            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                return new User(rs.getInt("id"),rs.getString("name"),
                            rs.getString("email"),rs.getString("password"));
            }
        }catch (SQLException e){
            System.out.println("Login Failed!");
        }
        return null ;
    }
}
