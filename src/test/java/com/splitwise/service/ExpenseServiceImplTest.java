package com.splitwise.service;

import com.splitwise.exception.UserDoesNotExistException;
import com.splitwise.model.Expense;
import com.splitwise.model.ExpenseGroup;
import com.splitwise.repository.UserRepository;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class ExpenseServiceImplTest {

    static ExpenseService expenseService;
    static UserService userService;

    @BeforeClass
    public static void setUp()
    {
        expenseService=new ExpenseServiceImpl();
        userService = new UserServiceImpl();
    }

    @Test
    public void testExpenseGroup()
    {
        Set<String> users = createUsers();
        ExpenseGroup expenseGroup = expenseService.createExpenseGroup(users);
        assertNotNull(expenseGroup);
        assertEquals(expenseGroup.getGroupMembers().size(), 3);
    }

    @Test(expected = UserDoesNotExistException.class)
    public void testGroupWithNonExisingUser()
    {
        Set<String> users = new HashSet<>();
        users.add("1");
        expenseService.createExpenseGroup(users);
    }

    @Test
    public void createExpense() throws UserDoesNotExistException
    {
        Set<String> users = createUsers();
        ExpenseGroup expenseGroup = createExpenseGroup();
        List<String> userList = Arrays.asList("2", "3");

        Expense expense = expenseService.createExpense("Team Lunch", "December Lunch", "1", LocalDateTime.now(),
                BigDecimal.valueOf(300), "EQUAL", userList, expenseGroup);

        assertTrue(expense.getUserSharesInGivenExpense().get("1").compareTo(BigDecimal.valueOf(200)) == 0);
        assertTrue(expense.getUserSharesInGivenExpense().get("2").compareTo(BigDecimal.valueOf(-100)) == 0);
        assertTrue(expense.getUserSharesInGivenExpense().get("3").compareTo(BigDecimal.valueOf(-100)) == 0);

        assertTrue(expenseGroup.getAllUsersWithAmount().get("1").compareTo(BigDecimal.valueOf(200)) == 0);
        assertTrue(expenseGroup.getAllUsersWithAmount().get("2").compareTo(BigDecimal.valueOf(-100)) == 0);
        assertTrue(expenseGroup.getAllUsersWithAmount().get("3").compareTo(BigDecimal.valueOf(-100)) == 0);

        assertTrue(UserRepository.userHashMap.get("1").getAllOtherUserWithAmount().get("2").compareTo(BigDecimal.valueOf(100)) == 0);
        assertTrue(UserRepository.userHashMap.get("1").getAllOtherUserWithAmount().get("3").compareTo(BigDecimal.valueOf(100)) == 0);
        assertTrue(UserRepository.userHashMap.get("2").getAllOtherUserWithAmount().get("1").compareTo(BigDecimal.valueOf(-100)) == 0);
        assertTrue(UserRepository.userHashMap.get("3").getAllOtherUserWithAmount().get("1").compareTo(BigDecimal.valueOf(-100)) == 0);

    }

    @Test
    public void createMultipleExpenses() throws UserDoesNotExistException
    {
        Set<String> users = createUsers();
        ExpenseGroup expenseGroup = createExpenseGroup();

        expenseService.createExpense("Team Lunch", "December Lunch", "1", LocalDateTime.now(),
                BigDecimal.valueOf(300), "EQUAL", Arrays.asList("2", "3"), expenseGroup);

        expenseService.createExpense("Team Outing", "December Outing", "2", LocalDateTime.now(),
                                      BigDecimal.valueOf(700), "EQUAL", Arrays.asList("1", "3"), expenseGroup);

        expenseService.createExpense("Team Mini Party", "January Party", "1", LocalDateTime.now(),
                BigDecimal.valueOf(1000), "EQUAL", Arrays.asList("2", "3"), expenseGroup);

        expenseService.createExpense("Team Snacks Party", "March Outing", "3", LocalDateTime.now(),
                BigDecimal.valueOf(500), "EQUAL", Arrays.asList("1", "2"), expenseGroup);

        assertTrue(expenseGroup.getAllUsersWithAmount().get("1").compareTo(BigDecimal.valueOf(466.66)) == 0);
        assertTrue(expenseGroup.getAllUsersWithAmount().get("2").compareTo(BigDecimal.valueOf(133.34)) == 0);
        assertTrue(expenseGroup.getAllUsersWithAmount().get("3").compareTo(BigDecimal.valueOf(333.32)) == 0);

        assertTrue(UserRepository.userHashMap.get("1").getAllOtherUserWithAmount().get("2").compareTo(BigDecimal.valueOf(133.34)) == 0);
        assertTrue(UserRepository.userHashMap.get("1").getAllOtherUserWithAmount().get("3").compareTo(BigDecimal.valueOf(333.32)) == 0);
        assertTrue(UserRepository.userHashMap.get("2").getAllOtherUserWithAmount().get("1").compareTo(BigDecimal.valueOf(-133.34)) == 0);
        assertTrue(UserRepository.userHashMap.get("3").getAllOtherUserWithAmount().get("1").compareTo(BigDecimal.valueOf(-333.32)) == 0);
    }

    @Test
    public void createMultipleExpenseGroup() throws UserDoesNotExistException
    {
        ExpenseGroup expenseGroup1 = createExpenseGroup();

        userService.createUser("4", "Aakanksha");
        userService.createUser("5", "Smriti");
        ExpenseGroup expenseGroup2 = expenseService.createExpenseGroup(new HashSet<>(Arrays.asList("4", "5")));

        userService.createUser("1", "Aman");
        userService.createUser("4", "Aakanksha");
        userService.createUser("8", "Sumit");
        userService.createUser("9", "Hemant");
        ExpenseGroup expenseGroup3 = expenseService.createExpenseGroup(new HashSet<>(Arrays.asList("1", "4", "8", "9")));

        Expense expense1 = expenseService.createExpense("Team Lunch", "December Lunch", "1", LocalDateTime.now(),
                BigDecimal.valueOf(300), "EQUAL", Arrays.asList("2", "3"), expenseGroup1);

        Expense expense2 = expenseService.createExpense("Team Outing", "December Outing", "2", LocalDateTime.now(),
                BigDecimal.valueOf(600), "EQUAL", Arrays.asList("1", "3"), expenseGroup1);

        Expense expense3 = expenseService.createExpense("Team Mini Party", "January Party", "4", LocalDateTime.now(),
                BigDecimal.valueOf(1000), "EQUAL", Arrays.asList("5"), expenseGroup2);

        Expense expense4 = expenseService.createExpense("Team Snacks Party", "March Outing", "4", LocalDateTime.now(),
                BigDecimal.valueOf(900), "EQUAL", Arrays.asList("1", "8"), expenseGroup3);


        assertTrue(expense1.getUserSharesInGivenExpense().get("1").compareTo(BigDecimal.valueOf(200)) == 0);
        assertTrue(expense1.getUserSharesInGivenExpense().get("2").compareTo(BigDecimal.valueOf(-100)) == 0);

        assertTrue(expense2.getUserSharesInGivenExpense().get("2").compareTo(BigDecimal.valueOf(400)) == 0);

        assertTrue(expense4.getUserSharesInGivenExpense().get("4").compareTo(BigDecimal.valueOf(600)) == 0);

        assertTrue(expenseGroup1.getAllUsersWithAmount().get("1").compareTo(BigDecimal.valueOf(0)) == 0);
        assertTrue(expenseGroup1.getAllUsersWithAmount().get("2").compareTo(BigDecimal.valueOf(300)) == 0);

        assertTrue(expenseGroup3.getAllUsersWithAmount().get("1").compareTo(BigDecimal.valueOf(-300)) == 0);

        assertTrue(UserRepository.userHashMap.get("1").getAllOtherUserWithAmount().get("4").compareTo(BigDecimal.valueOf(-300)) == 0);
        assertTrue(UserRepository.userHashMap.get("1").getAllOtherUserWithAmount().get("2").compareTo(BigDecimal.valueOf(-100)) == 0);
        assertTrue(UserRepository.userHashMap.get("2").getAllOtherUserWithAmount().get("1").compareTo(BigDecimal.valueOf(100)) == 0);

        assertTrue(UserRepository.userHashMap.get("4").getAllOtherUserWithAmount().get("8").compareTo(BigDecimal.valueOf(300)) == 0);

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