package org.expense.service;

import org.expense.DAO.BudgetDAO;
import org.expense.Model.Budget;

import java.time.YearMonth;

public class BudgetService {
    private final BudgetDAO budgetDAO ;
    public BudgetService(){
        budgetDAO = new BudgetDAO() ;
    }

    public void addBudget(Budget budget,int user_id){
        if(budget.getAmount() <= 0) {
            System.out.println("Budget can not be less than 0 or 0!!!");
            return;
        }
        budgetDAO.addBudget(budget,user_id);
    }

    public Budget getBudgetByMonth(YearMonth month,int user_id){
        return budgetDAO.getBudgetByMonth(month,user_id);
    }

    public int updateBudget(YearMonth month, double Budget,int user_id){
        return budgetDAO.updateBudget(month,Budget,user_id);
    }
}
