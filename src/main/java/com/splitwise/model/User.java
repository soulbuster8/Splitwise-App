package com.splitwise.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class User {
    public String userId;
    public String name;
    public Map<String, BigDecimal> allOtherUserWithAmount;

    public User(@NonNull String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.allOtherUserWithAmount = new HashMap<>();
    }
}
