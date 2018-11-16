package com.wtjr.lqg.enums;

/**
 * 开始投资时的投资类型
 * @author JoLie
 *
 */
public enum StartInvestmentType {
    /**
     * 零钱宝投资
     */
    Lqb("1"),
    /**
     * 可用余额投资
     */
    Balance("2");
            
    private String params;
    
    private StartInvestmentType(String params) {
        this.params = params;
    }
    
    public String getParams() {
        return params;
    }
}
