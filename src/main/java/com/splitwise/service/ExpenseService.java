package com.splitwise.service;

import com.splitwise.exception.ExpenseDoesNotExistException;
import com.splitwise.exception.UserDoesNotExistException;
import com.splitwise.model.Expense;
import com.splitwise.model.ExpenseGroup;
import com.splitwise.model.ExpenseStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface ExpenseService {
    public Expense createExpense(String title, String description, String ownerId, LocalDateTime expenseDateTime,
                                 BigDecimal expenseAmount, String splittingType, List<String> userIds,
                                 ExpenseGroup expenseGroup) throws UserDoesNotExistException;

    public Expense updateExpenseAmount(String expenseId, BigDecimal updatedAmount) throws ExpenseDoesNotExistException;

    public Expense addUserToExpense(String expenseId, String userIds, BigDecimal amount)
            throws UserDoesNotExistException, ExpenseDoesNotExistException;

    public ExpenseGroup createExpenseGroup(Set<String> userIds);

    public ExpenseGroup addUserToExpenseGroup(ExpenseGroup expenseGroup, String userId) throws UserDoesNotExistException;

    public Expense updateExpenseStatus(String expenseId, ExpenseStatus expenseStatus);

    public boolean isExpenseSettled(String expenseId);

}
