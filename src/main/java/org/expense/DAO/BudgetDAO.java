package org.expense.DAO;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import org.expense.Model.Budget;
import org.expense.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.time.YearMonth;

public class BudgetDAO {
    public void addBudget(Budget budget,int user_id){
        String sql = "INSERT INTO budgets(budget_month,amount,user_id) VALUES(?,?,?)" ;
        try{
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1,budget.getBudgetsMonth().toString());
            statement.setDouble(2,budget.getAmount());
            statement.setInt(3,user_id);

            int rowAffect = statement.executeUpdate();
            if(rowAffect>0)
                System.out.println("Budget Inserted Successfully");
            else
                System.out.println("Failed to insert Budget!");
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public Budget getBudgetByMonth(YearMonth month,int user_id){
        String sql = "SELECT * FROM budgets WHERE budget_month = ? AND user_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1,month.toString());
            statement.setInt(2,user_id);

            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                int id = rs.getInt("id");
                int uId = rs.getInt("user_id");
                double amount = rs.getDouble("amount");
                return new Budget(id,uId,month,amount);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null ;
    }

    public int updateBudget(YearMonth month, double budget,int user_id){
        String sql = "UPDATE budgets SET amount = ? WHERE budget_month = ? AND user_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);){

            statement.setDouble(1,budget);
            statement.setString(2,month.toString());
            statement.setInt(3,user_id);
            return statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0 ;
    }

}
