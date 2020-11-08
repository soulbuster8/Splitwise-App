package com.splitwise.service;

import com.splitwise.model.Expense;
import com.splitwise.model.User;

public class NotificationServiceImpl implements NotificationService {
    @Override
    public void notifyUsers(User user, Expense expense) {
        /*NotificationService notificationService = expense.getNotificationService();


        expense.getExpenseGroup()
                .getGroupMembers()
                .stream()
                .filter(ownerUser -> !ownerUser.equals(user))
                .forEach(otherUsers -> System.out.println(user.getUserId() + " has updated something."));*/
    }
}
