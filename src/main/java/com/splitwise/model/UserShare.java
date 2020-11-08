package com.splitwise.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserShare {
    private String userId;
    private BigDecimal amount;
    private List<Contribution> contributionList;

    public UserShare(String userId, BigDecimal amount) {
        this.userId=userId;
        this.amount=amount;
        this.contributionList=new ArrayList<>();
    }
}
