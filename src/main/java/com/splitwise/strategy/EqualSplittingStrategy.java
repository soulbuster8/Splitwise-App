package com.splitwise.strategy;

import com.splitwise.helper.DonorShareUpdate;
import com.splitwise.helper.ReceiverShareUpdate;
import com.splitwise.model.Expense;
import com.splitwise.model.ExpenseGroup;
import com.splitwise.model.User;
import com.splitwise.repository.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class EqualSplittingStrategy implements SplittingStrategy {

    @Override
    public void split(String ownerId, BigDecimal amount, List<String> userIds, List<BigDecimal> individualShares,
                                 ExpenseGroup expenseGroup, Expense expense) {
        BigDecimal splittedAmount = amount.divide(BigDecimal.valueOf(userIds.size()+1), 2, RoundingMode.HALF_UP);

        userIds.stream()
                .forEach(userId -> ReceiverShareUpdate.updateReceiverUserShare(expenseGroup, expense, userId, ownerId, splittedAmount));

        DonorShareUpdate.addDonorShareInGroup(expenseGroup, expense, ownerId, splittedAmount.multiply(BigDecimal.valueOf(userIds.size())));

        return;
    }

    @Override
    public void updateAmount(Expense expense, BigDecimal updateAmount) {
        BigDecimal remainingAmount = updateAmount.subtract(expense.getExpenseAmount());
        if(remainingAmount.compareTo(BigDecimal.ZERO)==0)
            return;

        split(expense.getDonorId(), remainingAmount, getAllUserExceptDonor(expense),
              null, expense.getExpenseGroup(), expense);
        expense.setExpenseAmount(updateAmount);
    }

    @Override
    public void addUser(Expense expense, BigDecimal updatedAmount, String userId) {
        int userSize = expense.getUserSharesInGivenExpense().size();
        BigDecimal updatedAmountPerUser = updatedAmount.divide(BigDecimal.valueOf(userSize+1), 2, RoundingMode.HALF_UP);

        BigDecimal updatedAmountForExistingUser = updatedAmountPerUser.multiply(BigDecimal.valueOf(userSize));
        BigDecimal existingAmount = expense.getExpenseAmount();
        split(expense.getDonorId(), updatedAmountForExistingUser.subtract(existingAmount), getAllUserExceptDonor(expense),
              null, expense.getExpenseGroup(), expense);

        ReceiverShareUpdate.updateReceiverUserShare(expense.getExpenseGroup(), expense, userId, expense.getDonorId(), updatedAmountPerUser);
        DonorShareUpdate.updateDonorShareInGroup(expense.getExpenseGroup(), expense, expense.getDonorId(), updatedAmountPerUser);
    }

    private List<String> getAllUserExceptDonor(Expense expense)
    {
        return expense.getUserSharesInGivenExpense().entrySet().stream()
                .map(Map.Entry::getKey)
                .filter(userId -> !expense.getDonorId().equals(userId))
                .collect(Collectors.toList());
    }



}
