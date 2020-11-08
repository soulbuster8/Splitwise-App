package com.splitwise.service;

import com.splitwise.exception.ExpenseDoesNotExistException;
import com.splitwise.exception.UserDoesNotExistException;
import com.splitwise.model.User;

import java.math.BigDecimal;

public interface UserService {
    public User createUser(String userId, String name);

    public void contributeToExpense(String expenseId, String userId, BigDecimal amount)
            throws UserDoesNotExistException, ExpenseDoesNotExistException;

}
