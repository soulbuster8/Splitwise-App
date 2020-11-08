package com.splitwise.service;

import com.splitwise.exception.ExpenseDoesNotExistException;
import com.splitwise.exception.UserDoesNotExistException;
import com.splitwise.factory.ExpenseSplittingFactory;
import com.splitwise.model.Expense;
import com.splitwise.model.ExpenseGroup;
import com.splitwise.model.ExpenseStatus;
import com.splitwise.model.User;
import com.splitwise.repository.ExpenseRepository;
import com.splitwise.repository.UserRepository;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ExpenseServiceImpl implements ExpenseService{

    @Override
    public Expense createExpense(String title, String description, String ownerId, LocalDateTime expenseDateTime,
                                 BigDecimal expenseAmount, String splittingType, List<String> userIds,
                                 ExpenseGroup expenseGroup)
                                throws UserDoesNotExistException {

        if(checkIfUserDoesNotExists(userIds))
            throw new UserDoesNotExistException("User does not exist. Please add this user before creating the expense.");

        Expense newExpense = Expense.builder()
                .expenseId(UUID.randomUUID().toString())
                .donorId(ownerId)
                .title(title)
                .description(description)
                .receiverIds(userIds)
                .expenseAmount(expenseAmount)
                .splittingStrategy(ExpenseSplittingFactory.getSplittingFactory(splittingType))
                .expenseDateTime(expenseDateTime)
                .expenseStatus(ExpenseStatus.CREATED)
                .expenseGroup(expenseGroup)
                .userSharesInGivenExpense(new HashMap<>())
                .build();

        newExpense.getSplittingStrategy()
                .split(ownerId, expenseAmount, userIds, null, expenseGroup, newExpense);

        ExpenseRepository.expenseHashMap.put(newExpense.getExpenseId(), newExpense);

        return newExpense;
    }


    @Override
    public Expense updateExpenseAmount(String expenseId, BigDecimal updatedAmount) throws ExpenseDoesNotExistException {
        if(!ExpenseRepository.expenseHashMap.containsKey(expenseId))
            throw new ExpenseDoesNotExistException("This expense does not exist");

        Expense expense = ExpenseRepository.expenseHashMap.get(expenseId);
        expense.getSplittingStrategy().updateAmount(expense, updatedAmount);

        return expense;
    }

    @Override
    public Expense addUserToExpense(String expenseId, String userId, BigDecimal updatedAmount)
            throws UserDoesNotExistException, ExpenseDoesNotExistException {

        if(!UserRepository.userHashMap.containsKey(userId))
            throw new UserDoesNotExistException("Please add user before adding him to expense.");

        if(!ExpenseRepository.expenseHashMap.containsKey(expenseId))
            throw new ExpenseDoesNotExistException("This expense does not exist");

        if(!ExpenseRepository.expenseHashMap.get(expenseId).getExpenseGroup().getGroupMembers()
            .contains(UserRepository.userHashMap.get(userId)))
            throw new UserDoesNotExistException("User does not exist in expense group, first add him in group and then add" +
                    " him in expense.");

        Expense expense = ExpenseRepository.expenseHashMap.get(expenseId);
        expense.getSplittingStrategy().addUser(expense, updatedAmount, userId);
        expense.setExpenseAmount(updatedAmount);
        expense.getReceiverIds().add(userId);

        return expense;
    }

    @Override
    public Expense updateExpenseStatus(String expenseId, ExpenseStatus expenseStatus) {


        return null;
    }

    @Override
    public boolean isExpenseSettled(String expenseId) {
        return false;
    }

    @Override
    public ExpenseGroup createExpenseGroup(Set<String> userIds) {

        userIds.stream()
                .filter(userId -> !UserRepository.userHashMap.containsKey(userId))
                .findAny()
                .ifPresent(new Consumer<String>() {
                    @SneakyThrows
                    @Override
                    public void accept(String s) {
                        throw new UserDoesNotExistException("User does not exist for " + s );
                    }
                });

        Set<User> users = userIds.stream()
                .map(userId -> UserRepository.userHashMap.get(userId))
                .collect(Collectors.toSet());

        ExpenseGroup expenseGroup = new ExpenseGroup();
        expenseGroup.setGroupMembers(users);

        return expenseGroup;
    }

    @Override
    public ExpenseGroup addUserToExpenseGroup(ExpenseGroup expenseGroup, String userId) throws UserDoesNotExistException
    {

        if(!UserRepository.userHashMap.containsKey(userId))
            throw new UserDoesNotExistException("User does not exist for " + userId);

        expenseGroup.getGroupMembers().add(UserRepository.userHashMap.get(userId));
        return expenseGroup;
    }

    private boolean checkIfUserDoesNotExists(List<String> userIds)
    {
        return userIds.stream()
                .filter(userId -> !UserRepository.userHashMap.containsKey(userId))
                .findAny()
                .isPresent();
    }
}
