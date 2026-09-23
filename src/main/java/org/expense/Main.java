package org.expense;

import org.expense.DAO.ExpenseDAO;
import org.expense.Model.Budget;
import org.expense.Model.Expense;
import org.expense.Model.User;
import org.expense.service.BudgetService;
import org.expense.service.UserService;
import org.expense.util.DatabaseConnection;
import org.expense.service.ExpenseService;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class Main {

    private   static final Scanner sc = new Scanner(System.in);

    private static final ExpenseService expenseService = new ExpenseService(new ExpenseDAO());
    private static final BudgetService budgetService = new BudgetService();
    private static final UserService userService = new UserService();
    private static User loginUser ;

    public static void main(String[] args)throws SQLException {
            Connection connection = DatabaseConnection.getConnection() ;
            connection.setAutoCommit(false);

            boolean running = true ;
            while(running){
                if(loginUser == null){
                    running = showAuthenticationMenu();
                }else {
                    applicationUI();
                }
            }
    }
    private static LocalDate readDate(String message){              // Code Reusability
        while (true) {
            System.out.print(message);
            String input = sc.nextLine();
            if (input.isBlank()){
                return null;
            }
            try {
                return LocalDate.parse(input);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date!");
                System.out.println("Description: "+e.getMessage());
            }
        }
    }

    private static YearMonth readYearMonth(String message){
        while (true){
            System.out.print("\n" + message);
            String yearMonth = sc.nextLine();
            try {
                return YearMonth.parse(yearMonth);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid Month!");
                System.out.println("Description: "+e.getMessage());
            }
            }
        }
    private static int readInt(String message){
        while(true) {
            System.out.print(message);
            String input = sc.nextLine();

            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static double readDouble(String message){
        while(true) {
            System.out.print(message);
            String input = sc.nextLine();

            try {
                double value = Double.parseDouble(input);
                if(value <= 0){
                    System.out.println("Amount must be greater than 0.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid Amount.");
            }
        }
    }
    private static boolean showAuthenticationMenu(){
        System.out.println();
        System.out.println("===============================");
        System.out.println("  EXPENSE MANAGEMENT SYSTEM");
        System.out.println("===============================");

        System.out.println("\n1. Register New User");
        System.out.println("2. Login User");
        System.out.println("3. Exit");
        int choice = readInt("Enter choice: ");
        switch (choice){
            case 1 -> registerUser();
            case 2 -> loginUser = loginUser();
            case 3 -> {
                return false ;
            }
            default -> System.out.println("Not valid option!");
        }
        return true ;
    }
    static void show_menu(){
        System.out.println("==========MENU=========");
        System.out.println("1. Add expense");
        System.out.println("2. View All Expense");
        System.out.println("3. Update Expense");
        System.out.println("4. Delete Expense");
        System.out.println("5. Find Expense By ID");
        System.out.println("6. View Total Expense");
        System.out.println("7. Expense Report By Specific Column");
        System.out.println("8. Search Expense Between Dates");
        System.out.println("9. Monthly Expense Report");
        System.out.println("10. Set Monthly Budget");
        System.out.println("11. View Monthly Budget");
        System.out.println("12. Update Budget");
        System.out.println("13. Show Dashboard");
        System.out.println("14. Logout");
        System.out.println("=======================");
    }

    private static void applicationUI() {
        try{
            show_menu();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> add();
                case 2 -> viewAllExpense();
                case 3 -> updateExpense();
                case 4 -> deleteExpense();
                case 5 -> {
                    int id = readInt("Expense id: ");
                    expenseService.getExpenseById(id, loginUser.getId());
                }
                case 6 -> totalExpense();
                case 7 -> {
                    boolean flag = true;
                    while (flag) {
                            flag = categoryWise();
                        if (!flag) break;
                    }
                }
                case 8 -> searchByDateRange();
                case 9 -> viewMonthlyReport();
                case 10 -> setMonthlyBudget();
                case 11 -> showMonthBudget();
                case 12 -> updateMonthBudget();
                case 13 -> showDashBoard();
                case 14 -> logout();
                default -> System.out.println("Not a valid choice!!");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    private static void logout(){
        System.out.println("Logged out successfully.");
        loginUser = null ;
    }

    private static void registerUser(){
        System.out.println("======REGISTER======");
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("\nEnter email: ");
        String email = sc.nextLine();
        System.out.print("\nEnter password: ");
        String password = sc.nextLine();

        User user = new User(name,email,password);
        boolean success = userService.registerUser(user);

        if (success) System.out.println("Registration Successful");
    }

    private static User loginUser(){
        System.out.println("=====LOGIN=======");
        System.out.print("Email: ");
        String email = sc.nextLine();
        System.out.print("\nPassword: ");
        String password = sc.nextLine();

        loginUser = userService.loginUser(email,password);
        if (loginUser != null){
            System.out.println("Welcome "+ loginUser.getName());
            return loginUser ;
        }else{
            System.out.println("No user found !");
        }
        return null ;
    }
    private static void add(){

        double amount = readDouble("Amount: ");

        System.out.print("Category: ");
        String category = sc.nextLine();

        System.out.print("Description: ");
        String description = sc.nextLine();

        LocalDate date1 = readDate("Enter expense date or press Enter to keep today date: ");

        Expense obj = new Expense(loginUser.getId(),amount,category,description,date1);
        boolean success = expenseService.addExpense(obj);
        try {
            if(success){
                System.out.println("Inserting expense......");
                Thread.sleep(1000);
                System.out.println("Expense Inserted Successfully.");
            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    static void viewAllExpense(){
        List<Expense> list = expenseService.getAllExpense(loginUser.getId());
        Iterator<Expense> iterator = list.iterator();
        try {
            System.out.println("Fetching Data from Database....");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        while (iterator.hasNext()){
            Expense expense = iterator.next();
            System.out.println(expense.toString());
        }
    }

    private static void updateExpense(){
        int id = readInt("Enter id of Expense want to update: ");
        Double newAmount ;
        while(true){
            System.out.print("Enter amount or press Enter to keep default amount: ");
            String input = sc.nextLine().trim() ;
            try {
                newAmount = (input.isBlank()) ? null : Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid amount!");
                continue;
            }
            break ;
        }

        System.out.print("\nCategory or press Enter to keep default value: ");
        String InputCategory = sc.nextLine();
        String category = InputCategory.isEmpty() ? null : InputCategory;

        System.out.print("\nDescription or press Enter to keep default value: ");
        String InputDescription = sc.nextLine();

        String description = InputDescription.isEmpty() ? null : InputDescription ;

        LocalDate date = readDate("Enter expense date or press Enter to keep default date: ");

        Expense expense  = new Expense(loginUser.getId(), newAmount,category,description,date) ;
        try {
            System.out.println("Updating.....");
            Thread.sleep(1000);
            boolean success = expenseService.updateExpense(id,expense);
            if(success) System.out.println("Updated");
            else System.out.println("Failed!");
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    static void deleteExpense() {
        try{
            int id = readInt("Enter expense id want to delete: ");
            System.out.println("Deleting Expense....");
            Thread.sleep(1000);
            if(expenseService.deleteExpenseById(id,loginUser.getId()) > 0)
                System.out.println("Expense Deleted Successfully.");
            else
                System.out.println("Failed to delete!");
        } catch (InterruptedException | SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    static void totalExpense(){
        try {
            System.out.println("Calculating.....");
            Thread.sleep(1000);
            System.out.println("Total Expense: "+expenseService.viewTotalExpense(loginUser.getId()));
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

    static boolean categoryWise() throws SQLException {
            System.out.println("---Category Wise Expense Report---");
            System.out.println("1. Total Expense Category Wise");
            System.out.println("2. Record Of Specific Category");
            System.out.println("3. Search Expenses By Date");
            System.out.println("4. Exit");
            System.out.println("----------------------------------");
            int choice = readInt("Enter Choice For Category Wise Report");

            return expenseService.expenseByAttribute(choice, loginUser.getId());
    }

    // Search expense between to dates(EXP: 2026-08-20 to 2026-08-01)
    private static void searchByDateRange(){
        sc.nextLine();
        System.out.println("\n---Search Expenses by Date Range----");
        LocalDate startDate = readDate("Enter start_date(YYYY-MM-DD): ");
        LocalDate endDate = readDate("Enter end_date(YYYY-MM-DD): ");
        try {
            List<Expense> expenses = expenseService.getExpenseByDateRange(loginUser.getId(), startDate,endDate);
            if(expenses.isEmpty()){
                System.out.println("No expense found!!!");
                return ;
            }
            System.out.println("----Results----");
            for (Expense expense : expenses){
                System.out.println(expense);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    // Shows the record of expenses by month
    private static void viewMonthlyReport(){
        YearMonth yearMonth = readYearMonth("Enter Month(YYYY-MM): ");
        System.out.println("\n-----Monthly Expense Report-----");

        LocalDate start_date = yearMonth.atDay(1);
        LocalDate end_date = yearMonth.atEndOfMonth();

        // DateTimeFormatter is a class that displays the dates in specified pattern
        // A pattern: "MMMM yyyy" where MMMM prints the month name and yyyy prints the year
        // EXP: '2026-09' will be display as 'September 2026'
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        System.out.println("Month: "+yearMonth.format(formatter));

        Double total = expenseService.totalExpenseOfMonth(loginUser.getId(), start_date,end_date);
        Map<String,Double> map = expenseService.getMonthlyReport(loginUser.getId(), start_date,end_date);
        if (map.isEmpty()){
            System.out.println("Record not found!");
            return;
        }
        System.out.println("Month Total: "+total);
        System.out.println("-----Category Wise Report------");
        for (Map.Entry<String,Double> i : map.entrySet()){
            System.out.println(i);
        }
    }

    // To add a row in 'Budgets' table
    private static void setMonthlyBudget(){

        YearMonth budgetMonth = readYearMonth("Enter Month(YYYY-MM): ");
        double amount = readDouble("Set Budget: ");

        Budget budget = new Budget(loginUser.getId(), budgetMonth,amount);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        budgetService.addBudget(budget, loginUser.getId());
    }

    private static void showMonthBudget(){

        YearMonth month = readYearMonth("Enter Month(yyyy-mm): ");
        Budget budget = budgetService.getBudgetByMonth(month, loginUser.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy");
        if(budget == null){
            System.out.println("No budget found for "+month.format(formatter));
            return;
        }
        LocalDate start_date = month.atDay(1);
        LocalDate end_date = month.atEndOfMonth();

        Double total = expenseService.totalExpenseOfMonth(loginUser.getId(), start_date,end_date);
        Double amount = budget.getAmount();

        System.out.println("-----Monthly Budget-----");
        System.out.println("Month: "+ month.format(formatter));
        System.out.println("Budget: "+amount);
        System.out.println("Spent: "+total);
        System.out.println("Remaining: " + (amount-total));
        if (amount-total >0)
            System.out.println("Within Budget✅");
        else if(total-amount ==0)
            System.out.println("Have No Budget⚠️");
        else
            System.out.println("Under Budget🚨");
    }

    private static void updateMonthBudget(){

        YearMonth month = readYearMonth("Enter Month(yyyy-mm): ");
        Budget budget = budgetService.getBudgetByMonth(month, loginUser.getId());

        double amount = readDouble("Enter new budget: ");
        int rowAffect = budgetService.updateBudget(month,amount, loginUser.getId());
        if(rowAffect > 0){
            System.out.println("Budget Updated Successfully");
        }else{
            System.out.println("Failed to Update !");
        }
    }

    private static void showDashBoard(){
        System.out.println();
        System.out.println("=========EXPENSE DASHBOARD=======");

        //Total Expense
        double totalExpense = expenseService.viewTotalExpense(loginUser.getId());
        System.out.println("Total Expenses: ₹"+totalExpense);

        // Current Month
        YearMonth currentMonth = YearMonth.now();

        LocalDate start_date = currentMonth.atDay(1);
        LocalDate end_date = currentMonth.atEndOfMonth();

        double monthlyExpenses = expenseService.totalExpenseOfMonth(loginUser.getId(), start_date,end_date);
        System.out.println("This Month: ₹"+monthlyExpenses);

        // Category Totals
        Map<String,Double> map = expenseService.categoryTotal(loginUser.getId());
        if (map.isEmpty()){
            System.out.println("No Expense recorded yet.");
            System.out.println("=============================");
            return;
        }
        String highestCategory = null ;
        double highestAmount = 0;


        for (Map.Entry<String,Double> entry : map.entrySet()){
            if (entry.getValue() > highestAmount){
                highestAmount = entry.getValue();
                highestCategory = entry.getKey() ;
            }
        }
        System.out.println("Highest Category: "+highestCategory);
        System.out.println("Highest Spending: "+highestAmount);
        System.out.println();
        System.out.println("Category Breakdown: ");
        for (Map.Entry<String,Double> entry : map.entrySet()){
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
        System.out.println("==============================");
    }


}