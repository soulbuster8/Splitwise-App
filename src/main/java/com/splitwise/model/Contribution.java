package com.splitwise.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class Contribution {
    private String contributionId;
    private LocalDateTime contributionTime;
    private BigDecimal contributionAmount;
    private String fromUserId;
    private String toUserId;
    private String description;
}
