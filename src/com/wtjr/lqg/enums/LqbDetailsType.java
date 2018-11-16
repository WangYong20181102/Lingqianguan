package com.wtjr.lqg.enums;

public enum LqbDetailsType{
    /**
     * 转入
     */
    RollIn("转入","1"),
    /**
     * 转出
     */
    RollOut("转出","2"),
    /**
     * 投资
     */
    Investment("投资","3"),
    /**
     * 收益
     */
    Earnings("收益","4"),
    /**
     * 体验金
     */
    TYJ("体验金","5"),
    /**
     * 全部
     */
    All("零钱宝资金记录","3");
    
    private String title;
    private String params;
    
    private LqbDetailsType(String title,String params) {
        this.title = title;
        this.params = params;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getParams() {
        return params;
    }
}
