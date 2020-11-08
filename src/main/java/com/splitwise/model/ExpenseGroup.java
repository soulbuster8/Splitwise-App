package com.splitwise.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@AllArgsConstructor
@Getter
@Setter
@Builder
public class ExpenseGroup {

    private String expenseGroupId;
    private Set<User> groupMembers;
    private Map<String, BigDecimal> allUsersWithAmount;

    public ExpenseGroup() {
        this.expenseGroupId = UUID.randomUUID().toString();
        this.groupMembers = new HashSet<>();
        this.allUsersWithAmount = new HashMap<>();
    }

}
