package com.splitwise.strategy;

import com.splitwise.exception.UserDoesNotExistException;
import com.splitwise.model.Expense;
import com.splitwise.model.ExpenseGroup;
import com.splitwise.model.UserShare;

import java.math.BigDecimal;
import java.util.List;

public interface SplittingStrategy {
    public void split(String ownerId, BigDecimal amount, List<String> userIds, List<BigDecimal> individualShares,
                                 ExpenseGroup expenseGroup, Expense expense);

    public void updateAmount(Expense expense, BigDecimal updateAmount);

    public void addUser(Expense expense, BigDecimal updatedAmount, String userIds);
}
