package com.splitwise.helper;

import com.splitwise.model.Expense;
import com.splitwise.model.ExpenseGroup;

import java.math.BigDecimal;

public class DonorShareUpdate {

    public static void addDonorShareInGroup(ExpenseGroup expenseGroup, Expense expense, String userId, BigDecimal amount)
    {
        BigDecimal donorWithAmount = expenseGroup.getAllUsersWithAmount()
                .getOrDefault(userId, BigDecimal.ZERO);

        BigDecimal donorAmountInExpense =
                expense.getUserSharesInGivenExpense().getOrDefault(userId, BigDecimal.ZERO);


        expenseGroup.getAllUsersWithAmount().put(userId, donorWithAmount.add(amount));

        expense.getUserSharesInGivenExpense().put(userId, donorAmountInExpense.add(amount));

    }

    public static void updateDonorShareInGroup(ExpenseGroup expenseGroup, Expense expense, String userId, BigDecimal updatedAmount)
    {
        BigDecimal existingAmountInGroup = expenseGroup.getAllUsersWithAmount()
                                    .getOrDefault(userId, BigDecimal.ZERO);

        expenseGroup.getAllUsersWithAmount().put(userId, existingAmountInGroup.add(updatedAmount));

        BigDecimal existingAmountInExpense = expense.getUserSharesInGivenExpense()
                                                .getOrDefault(userId, BigDecimal.ZERO);

        expense.getUserSharesInGivenExpense().put(userId, existingAmountInExpense.add(updatedAmount));

    }
}
