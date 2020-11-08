package com.splitwise.factory;

import com.splitwise.strategy.EqualSplittingStrategy;
import com.splitwise.strategy.SplittingStrategy;

public class ExpenseSplittingFactory {
    public static SplittingStrategy getSplittingFactory(String splittingType)
    {
        if("EQUAL".equals(splittingType))
            return new EqualSplittingStrategy();
        return null;
    }
}
