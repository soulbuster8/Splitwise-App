package com.splitwise.service;

import com.splitwise.exception.ExpenseDoesNotExistException;
import com.splitwise.exception.UserDoesNotExistException;
import com.splitwise.model.Expense;
import com.splitwise.model.ExpenseGroup;
import com.splitwise.model.User;
import com.splitwise.repository.ExpenseRepository;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ExpenseServiceImpl2Test {

    static UserService userService;
    static ExpenseService expenseService;

    @BeforeClass
    public static void setUp()
    {
        userService = new UserServiceImpl();
        expenseService = new ExpenseServiceImpl();
    }

    @Test
    public void testAddUserInExpense() throws UserDoesNotExistException {
        ExpenseGroup expenseGroup = createExpenseGroup();
        expenseService.addUserToExpenseGroup(expenseGroup, "4");

        assertEquals(expenseGroup.getGroupMembers().size(), 4);
    }

    @Test
    public void testUpdatedAmount() throws UserDoesNotExistException, ExpenseDoesNotExistException {
        ExpenseGroup expenseGroup = createExpenseGroup();

        Expense expense = expenseService.createExpense("Team Lunch", "December Lunch", "1", LocalDateTime.now(),
                BigDecimal.valueOf(300), "EQUAL", Arrays.asList("2", "3"), expenseGroup);

        expenseService.updateExpenseAmount(expense.getExpenseId(), BigDecimal.valueOf(600));

        assertTrue(expense.getExpenseAmount().compareTo(BigDecimal.valueOf(600)) == 0);
        assertTrue(expense.getUserSharesInGivenExpense().get("2")
                    .compareTo(BigDecimal.valueOf(-200)) == 0);
        assertTrue(expense.getUserSharesInGivenExpense().get("1")
                .compareTo(BigDecimal.valueOf(400)) == 0);
        assertTrue(expenseGroup.getAllUsersWithAmount().get("1").compareTo(BigDecimal.valueOf(400)) == 0);
    }

    @Test
    public void testAddUserToExpense() throws UserDoesNotExistException, ExpenseDoesNotExistException {
        ExpenseGroup expenseGroup = createExpenseGroup();
        expenseGroup.getGroupMembers().add(userService.createUser("6", "Aakanksha"));
        Expense expense = expenseService.createExpense("Team Lunch", "December Lunch", "1", LocalDateTime.now(),
                BigDecimal.valueOf(300), "EQUAL", new ArrayList<>(Arrays.asList("2", "3")), expenseGroup);

        expenseService.addUserToExpense(expense.getExpenseId(), "6", BigDecimal.valueOf(500));

        assertTrue(expense.getExpenseAmount().compareTo(BigDecimal.valueOf(500)) == 0);
        assertTrue(expense.getUserSharesInGivenExpense().get("1").compareTo(BigDecimal.valueOf(375)) == 0);
        assertTrue(expense.getUserSharesInGivenExpense().get("2").compareTo(BigDecimal.valueOf(-125)) == 0);

        assertTrue(expenseGroup.getAllUsersWithAmount().get("1").compareTo(BigDecimal.valueOf(375)) == 0);
        assertTrue(expenseGroup.getAllUsersWithAmount().get("6").compareTo(BigDecimal.valueOf(-125)) == 0);


    }

    @Test(expected = UserDoesNotExistException.class)
    public void testUserNotInExpenseGroup() throws UserDoesNotExistException, ExpenseDoesNotExistException {
        ExpenseGroup expenseGroup = createExpenseGroup();
        Expense expense = expenseService.createExpense("Team Lunch", "December Lunch", "1", LocalDateTime.now(),
                BigDecimal.valueOf(300), "EQUAL", Arrays.asList("2", "3"), expenseGroup);

        expenseService.addUserToExpense(expense.getExpenseId(), "4", BigDecimal.valueOf(500));
    }

    private ExpenseGroup createExpenseGroup()
    {
        Set<String> users = createUsers();
        return expenseService.createExpenseGroup(users);
    }

    private Set<String> createUsers()
    {
        userService.createUser("1", "Aman");
        userService.createUser("2", "Vikas");
        userService.createUser("3", "Sharath");

        Set<String> userIds = new HashSet<>();
        userIds.addAll(Arrays.asList("1", "2", "3"));
        return userIds;
    }
}
