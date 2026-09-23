// DAO : Data Access Object
package org.expense.DAO;
import org.expense.util.DatabaseConnection;
import org.expense.Model.Expense;

import javax.xml.crypto.Data;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ExpenseDAO {

    // Add a row in 'expense' table
    public boolean addExpense(Expense expense){
        String addQuery = "INSERT INTO expense(user_id,amount,category,description,expense_date) VALUES(?,?,?,?,?)" ;
        try {
            Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement s1 = connection.prepareStatement(addQuery) ;

            s1.setInt(1,expense.getUser_id());
            s1.setDouble(2,expense.getAmount());
            s1.setString(3,expense.getCategory());
            s1.setString(4,expense.getDescription());
            s1.setDate(5,java.sql.Date.valueOf(expense.getExpense_date()));

            s1.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to insert!");
            System.out.println(e.getMessage());
        }
        return true ;
    }


    // To retrive the whole table from database
    public List<Expense> getAllExpense(int user_id){
        // collection to store each row of table as object
        List<Expense> list = new LinkedList<>();
        try {
            Connection connection = DatabaseConnection.getConnection();
            String retriveQuery = "SELECT * FROM expense WHERE user_id = ? ORDER BY expense_date";
            PreparedStatement statement = connection.prepareStatement(retriveQuery);
            statement.setInt(1,user_id);

            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                Expense obj = new Expense();
                obj.setId(rs.getInt("id"));
                obj.setUser_id(rs.getInt("user_id"));
                obj.setAmount(rs.getDouble("amount"));
                obj.setCategory(rs.getString("category"));
                obj.setDescription(rs.getString("description"));
                obj.setExpense_date(rs.getDate("expense_date").toLocalDate());

                list.add(obj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return list ;
    }



    // Find the expense by id
    public Expense getExpenseById(int expense_id,int user_id) throws SQLException {
        Connection connection = DatabaseConnection.getConnection();
        String ByID = "SELECT * FROM expense WHERE id = ? AND user_id = ?";
        PreparedStatement statement = connection.prepareStatement(ByID);
        statement.setInt(1,expense_id);
        statement.setInt(2,user_id);
        ResultSet rs = statement.executeQuery();
        if(rs.next()){
             return new Expense(rs.getInt("id"),
                                   rs.getInt("user_id"),
                                   rs.getDouble("amount"),
                                   rs.getString("category"),
                                   rs.getString("description"),
                                   rs.getDate("expense_date").toLocalDate()
                    );
        }
        return null ;
    }

    public boolean updateExpense(Expense expense,int id){
        String updateQuery = """
    UPDATE expense
    SET amount = COALESCE(?, amount),
        category = COALESCE(?, category),
        description = COALESCE(?, description),
        expense_date = COALESCE(?,expense_date)
    WHERE id = ? AND user_id = ? ;
    """;
        try {
            Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(updateQuery);

            statement.setObject(1, expense.getAmount());
            statement.setString(2, expense.getCategory());
            statement.setString(3, expense.getDescription());
            statement.setObject(4, expense.getExpense_date());

            statement.setInt(5, id);
            statement.setInt(6, expense.getUser_id());
            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Delete an expense(row) by ID
    public PreparedStatement deleteExpense(int id,int user_id) throws SQLException{
            Connection connection = DatabaseConnection.getConnection();
            String deleteQuery = "DELETE FROM expense WHERE id = ? AND user_id = ?";

            PreparedStatement statement = connection.prepareStatement(deleteQuery);
            statement.setInt(1,id);
            statement.setInt(2,user_id);
            return statement ;
    }

    // Total Amount From Database
    public PreparedStatement viewTotalExpense(int user_id)throws SQLException{
            Connection connection = DatabaseConnection.getConnection();
            String totalQuery = "SELECT SUM(amount) AS total FROM expense WHERE user_id = ?" ;
            PreparedStatement statement = connection.prepareStatement(totalQuery);
            statement.setInt(1,user_id);
            return statement ;
    }

    // Total Amount Category Wise
    public void expenseByCategory(int user_id)throws SQLException{

        Map<String,Double> map = new LinkedHashMap<>();         // Map<String,Double> collection for key-values pair
        Connection connection = DatabaseConnection.getConnection() ;
        String query = "SELECT category,SUM(amount) AS total FROM expense WHERE user_id = ? GROUP BY category" ;
        PreparedStatement statement = connection.prepareStatement(query) ;
        statement.setInt(1,user_id);
        ResultSet rs = statement.executeQuery();
        while(rs.next()){
            map.put((rs.getString("category")),rs.getDouble("total"));
        }

        for (Map.Entry<String,Double> i : map.entrySet() ){
            System.out.print(i.getKey()+": ₹"+i.getValue());
            System.out.println();
        }
    }

    // Expenses of Specific Category
    public ResultSet specificCategory(String category,int user_id)throws SQLException{
        Connection connection = DatabaseConnection.getConnection();
        String query = "SELECT * FROM expense WHERE category =? AND user_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1,category);
        statement.setInt(2,user_id);
        ResultSet rs = statement.executeQuery();
       return rs ;
    }

    // Search Expenses By Date
    public List<Expense> expenseByDate(LocalDate date,int user_id)throws SQLException{
        List<Expense> expenses = new ArrayList<>();
        Connection connection = DatabaseConnection.getConnection() ;
        String query = "SELECT * FROM expense WHERE expense_date =? AND user_id = ? ORDER BY expense_date";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setDate(1, Date.valueOf(date));
        statement.setInt(2, user_id);
        ResultSet rs = statement.executeQuery() ;
        while(rs.next()){
            int id = rs.getInt("id");
            int u_id = rs.getInt("user_id") ;
            Double amount = rs.getDouble("amount");
            String category = rs.getString("category");
            String description = rs.getString("description");
            Expense obj = new Expense(id,u_id,amount,category,description,date) ;
            expenses.add(obj);
        }
        return expenses ;
    }

    public List<Expense> getExpenseByDateRange(int user_id,LocalDate startDate,LocalDate endDate){
        List<Expense> expenses = new ArrayList<>();
        String sql = """
                SELECT * FROM expense
                WHERE expense_date BETWEEN ? AND ? AND user_id = ?
                ORDER BY expense_date
                """ ;
        try {
            Connection connection = DatabaseConnection.getConnection() ;
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setDate(1,Date.valueOf(startDate));
            statement.setDate(2,Date.valueOf(endDate));
            statement.setInt(3,user_id);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                Expense obj = new Expense();
                obj.setId(rs.getInt("id"));
                obj.setUser_id(rs.getInt("user_id"));
                obj.setAmount(rs.getDouble("amount"));
                obj.setCategory(rs.getString("category"));
                obj.setDescription(rs.getString("description"));
                obj.setExpense_date(rs.getDate("expense_date").toLocalDate());
                expenses.add(obj);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return expenses ;
    }

    // Category wise expense report of Month
    public Map<String,Double> getMonthlyReport(int user_id,LocalDate start_date,LocalDate end_date){
        Map<String,Double> map = new LinkedHashMap<>();
        try {
            Connection connection = DatabaseConnection.getConnection() ;
            String monthlyReport = """
                    SELECT category,SUM(amount) AS total FROM expense WHERE expense_date
                    BETWEEN ? AND ? AND user_id = ?
                    GROUP BY category
                    ORDER BY category
                   """;
            PreparedStatement statement2 = connection.prepareStatement(monthlyReport);
            statement2.setDate(1,Date.valueOf(start_date));
            statement2.setDate(2,Date.valueOf(end_date));
            statement2.setInt(3,user_id);

            ResultSet rs = statement2.executeQuery();
            while(rs.next()){
                String category = rs.getString("category");
                Double amount = rs.getDouble("total");
                map.put(category,amount);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return map ;
    }

    // total expenses of month
    public Double monthlyTotal(int user_id,LocalDate start_date, LocalDate end_date){
        String sql = "SELECT COALESCE(SUM(amount),0) AS total FROM expense WHERE expense_date BETWEEN ? AND ? AND user_id = ?";
        try(Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setDate(1,Date.valueOf(start_date));
            statement.setDate(2,Date.valueOf(end_date));
            statement.setInt(3,user_id);

            try(ResultSet rs = statement.executeQuery()){
                if(rs.next())
                    return rs.getDouble("total");
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0d;
    }

    public Map<String,Double> getCategoryTotals(int user_id){
        String sql = """
                SELECT category, SUM(amount) AS total FROM expense
                WHERE user_id = ?
                GROUP BY category
                ORDER BY total
                """ ;
        Map<String,Double> map = new LinkedHashMap<>();
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,user_id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                map.put(rs.getString("category"),rs.getDouble("total"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return map ;
    }
}
