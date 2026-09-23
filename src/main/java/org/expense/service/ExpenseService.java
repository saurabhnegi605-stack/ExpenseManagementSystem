package org.expense.service;

import org.expense.DAO.ExpenseDAO;
import org.expense.Main;
import org.expense.Model.Expense;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class ExpenseService {

    private final ExpenseDAO obj ;
    private final Scanner sc = new Scanner(System.in) ;
    public ExpenseService(ExpenseDAO obj){
        this.obj = obj ;
    }

    public boolean addExpense(Expense expense){
        if(expense.getExpense_date() == null){
            expense.setExpense_date(LocalDate.now());
        }

        if(expense.getCategory() == null || expense.getCategory().isBlank()){
            System.out.println("Category cannot be empty");
            return false;
        }
        return obj.addExpense(expense);
    }

    public List<Expense> getAllExpense(int user_id){
        return obj.getAllExpense(user_id) ;
    }

    public boolean updateExpense(int id,Expense expense) {
        return obj.updateExpense(expense,id);
    }



    // update Expense by unique id
    public void getExpenseById(int expense_id,int user_id) throws SQLException {
        Expense expense = obj.getExpenseById(expense_id,user_id);
        if(expense == null){
            System.out.println("Expense Not found!");
            return;
        }
        System.out.println(expense.toString());
    }


    // Delete an expense by id
    public int deleteExpenseById(int id,int user_id) throws SQLException{
            PreparedStatement statement = obj.deleteExpense(id,user_id);
        return statement.executeUpdate();
    }
    // Sum of all expenses
    public double viewTotalExpense(int user_id){
        try {
            PreparedStatement statement = obj.viewTotalExpense(user_id) ;
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getDouble("total");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0d;
    }

    // Expense Report By Specific Attribute(column)
    public boolean expenseByAttribute(int choice,int user_id)throws SQLException{
        boolean flag = true ;
        switch (choice){
            case 1 ->
                        // Total amount category wise
                        obj.expenseByCategory(user_id);
            case 2 -> {
                    // Expense detail of specific category
                    System.out.print("Category :");
                    String category = sc.nextLine();
                    ResultSet rs = obj.specificCategory(category,user_id);
                    if (!rs.next()){
                        System.out.println("There is no Such category "+category);
                    }else {
                        while(rs.next()){
                            System.out.println("ID: "+rs.getInt("id")+"| ₨ "
                                    +rs.getDouble("amount")+" | "
                                    +rs.getString("category")+" | "
                                    +rs.getString("description")+" | "
                                    +rs.getDate("expense_date"));
                        }
                    }
                }
                case 3 -> {
                    // Expense report by expense_date
                    System.out.println("Enter Date(YYYY-MM-DD): ");
                    String date = sc.nextLine() ;
                    List<Expense> expenses = obj.expenseByDate(LocalDate.parse(date),user_id);
                    for(Expense i : expenses){
                        System.out.println(i);
                    }
                }
                case 4 -> flag = false ;

                default -> System.out.println("Invalid Input. Try Again");
            }
            return flag;
    }
    // Searches expense between two dates (EXP:- 2026-04-01 to 2026-09-07)
    public List<Expense> getExpenseByDateRange(int user_id,LocalDate startDate,LocalDate endDate){
        if(startDate == null || endDate == null){
            System.out.println("Dates can not be null !");
            return null ;
        }
        if(startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start_date can not be after End_date!!");
        }
        return obj.getExpenseByDateRange(user_id,startDate,endDate);
    }

    public Double totalExpenseOfMonth(int user_id,LocalDate start_date, LocalDate end_date){
        if (start_date.isAfter(end_date))
            throw new IllegalArgumentException("Start date can not be after end date!!");

        return obj.monthlyTotal(user_id,start_date,end_date);
    }

    public Map<String,Double> getMonthlyReport(int user_id,LocalDate start_date,LocalDate end_date){
        return obj.getMonthlyReport(user_id,start_date,end_date);
    }

    public Map<String,Double> categoryTotal(int user_id){
        return obj.getCategoryTotals(user_id);
    }
}
