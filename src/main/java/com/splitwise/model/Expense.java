package com.splitwise.model;

import com.splitwise.service.NotificationService;
import com.splitwise.strategy.SplittingStrategy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Expense {
    private String expenseId;
    private String title;
    private String description;
    private String donorId;
    private List<String> receiverIds;
    private LocalDateTime expenseDateTime;
    private BigDecimal expenseAmount;
    private ExpenseStatus expenseStatus;
    private SplittingStrategy splittingStrategy;
    private ExpenseGroup expenseGroup;
    private Map<String, BigDecimal> userSharesInGivenExpense;

}
