package org.expense.service;

import org.expense.DAO.ExpenseDAO;
import org.expense.Model.Expense;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.* ;

public class ExpenseServiceTest {

    @Test
    void shouldUpdateExpense(){
        ExpenseDAO expenseDAO = mock(ExpenseDAO.class);
        ExpenseService expenseService = new ExpenseService(expenseDAO);
        int id = 1 ;
        Expense expense = new Expense(1,null,"","",LocalDate.now());

        when(expenseDAO.addExpense(expense)).thenReturn(true);

        when(expenseDAO.updateExpense(expense,id)).thenReturn(true);
        boolean result = expenseService.updateExpense(id,expense);
        assertTrue(result);
        verify(expenseDAO).updateExpense(expense,id);
    }
}
