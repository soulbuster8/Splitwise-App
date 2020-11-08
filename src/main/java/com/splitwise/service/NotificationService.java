package com.splitwise.service;

import com.splitwise.model.Expense;
import com.splitwise.model.User;

public interface NotificationService {
    public void notifyUsers(User user, Expense expense);
}
