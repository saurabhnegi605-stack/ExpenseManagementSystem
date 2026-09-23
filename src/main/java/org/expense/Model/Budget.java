package org.expense.Model;

import java.time.YearMonth;

public class Budget {
    private int id;
    private int user_id ;
    private YearMonth budgetsMonth;
    private double amount ;

    public Budget(){}
    public Budget(int user_id,YearMonth budgetsMonth,double amount){
        this.user_id = user_id;
        this.amount = amount;
        this.budgetsMonth = budgetsMonth ;
    }
    public Budget(int id,int user_id,YearMonth budgetsMonth,double amount){
        this.id = id;
        this.user_id =user_id;
        this.budgetsMonth = budgetsMonth;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id(){return user_id;}

    public void setUser_id(int user_id){this.user_id = user_id;}

    public YearMonth getBudgetsMonth() {
        return budgetsMonth;
    }

    public void setBudgetsMonth(YearMonth budgetsMonth) {
        this.budgetsMonth = budgetsMonth;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "id=" + id +
                ", budgetsMonth=" + budgetsMonth +
                ", amount=" + amount +
                '}';
    }
}
