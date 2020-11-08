package com.splitwise.service;

import com.splitwise.exception.ExpenseDoesNotExistException;
import com.splitwise.exception.UserDoesNotExistException;
import com.splitwise.helper.DonorShareUpdate;
import com.splitwise.helper.ReceiverShareUpdate;
import com.splitwise.model.Expense;
import com.splitwise.model.User;
import com.splitwise.repository.ExpenseRepository;
import com.splitwise.repository.UserRepository;

import java.math.BigDecimal;

public class UserServiceImpl implements UserService{
    @Override
    public User createUser(String userId, String name) {
        if(!UserRepository.userHashMap.containsKey(userId))
        {
            UserRepository.userHashMap.put(userId, new User(userId, name));
        }
        return UserRepository.userHashMap.get(userId);
    }

    @Override
    public void contributeToExpense(String expenseId, String userId, BigDecimal amount)
            throws UserDoesNotExistException, ExpenseDoesNotExistException
    {
        if(!UserRepository.userHashMap.containsKey(userId))
            throw new UserDoesNotExistException("Please create user first for this id.");

        if (!ExpenseRepository.expenseHashMap.containsKey(expenseId))
            throw new ExpenseDoesNotExistException("This expense does not exist.");

        Expense expense = ExpenseRepository.expenseHashMap.get(expenseId);

        ReceiverShareUpdate.updateReceiverUserShare(expense.getExpenseGroup(), expense, userId,
                                                    expense.getDonorId(), amount.multiply(BigDecimal.valueOf(-1)));

        DonorShareUpdate.updateDonorShareInGroup(expense.getExpenseGroup(), expense, expense.getDonorId(),
                                                 amount.multiply(BigDecimal.valueOf(-1)));

    }

}
