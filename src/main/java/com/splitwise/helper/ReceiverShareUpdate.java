package com.splitwise.helper;

import com.splitwise.model.Expense;
import com.splitwise.model.ExpenseGroup;
import com.splitwise.model.User;
import com.splitwise.repository.UserRepository;

import java.math.BigDecimal;

public class ReceiverShareUpdate {

    public static void updateReceiverUserShare(ExpenseGroup expenseGroup, Expense expense, String userId,
                                        String ownerId, BigDecimal amount)
    {
        BigDecimal receiverWithAmount = expenseGroup.getAllUsersWithAmount()
                .getOrDefault(userId, BigDecimal.ZERO);

        BigDecimal receiverAmountFromExpense = expense.getUserSharesInGivenExpense()
                                                .getOrDefault(userId, BigDecimal.ZERO);

        expenseGroup.getAllUsersWithAmount().put(userId, receiverWithAmount.subtract(amount));

        expense.getUserSharesInGivenExpense().put(userId, receiverAmountFromExpense.subtract(amount));

        updateReceiverUserShares(userId, ownerId, amount);

        updateDonorUserShares(ownerId, userId, amount);
    }

    private static User updateReceiverUserShares(String receiverId, String ownerId, BigDecimal amount)
    {
        User receiverUser = UserRepository.userHashMap.get(receiverId);
        BigDecimal ownerAmount = receiverUser.getAllOtherUserWithAmount()
                .getOrDefault(ownerId, BigDecimal.ZERO);

        receiverUser.getAllOtherUserWithAmount().put(ownerId, ownerAmount.subtract(amount));
        return receiverUser;
    }

    private static User updateDonorUserShares(String ownerId, String receiverId, BigDecimal amount)
    {
        User ownerUser = UserRepository.userHashMap.get(ownerId);
        BigDecimal receiverAmount = ownerUser.getAllOtherUserWithAmount()
                .getOrDefault(receiverId, BigDecimal.ZERO);

        ownerUser.getAllOtherUserWithAmount().put(receiverId, receiverAmount.add(amount));
        return ownerUser;
    }
}
