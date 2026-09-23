package org.expense.Model;

import java.time.LocalDate ;

public class Expense {
    private int id ;
    private int user_id ;
    private Double amount ;
    private String category ;
    private String description ;
    private LocalDate expense_date;

    public Expense(int user_id,Double amount, String category, String description, LocalDate expense_date) {
        this.user_id = user_id;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.expense_date = expense_date;
    }

    public Expense(int id,int user_id,Double amount, String category, String description, LocalDate expense_date) {
        this.id = id ;
        this.user_id = user_id ;
        this.amount = amount;
        this.category = category;
        this.description = description;
        this.expense_date = expense_date;
    }

    public Expense() {

    }

    public int getId() {return id;}

    public void setId(int id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getExpense_date() {
        return expense_date;
    }

    public void setExpense_date(LocalDate expense_date) {
        this.expense_date = expense_date;
    }

    public int getUser_id(){return user_id;}

    public void setUser_id(int user_id){this.user_id = user_id ;}
    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", expense_date=" + expense_date +
                '}';
    }
}
