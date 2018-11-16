package com.wtjr.lqg.base;

import java.io.Serializable;

public class LqbInvestment implements Serializable {
    private static final long serialVersionUID = -4052862451312430923L;

    public String pro_code;

    public double loanMoney;

    public long borrowTime;

    public int periods;

    public int bId;

    public int totalPeriods;

    public int status;
    
    public double lqbRate;

    @Override
    public String toString() {
        return "LqbInvestment [pro_code=" + pro_code + ", loanMoney="
                + loanMoney + ", borrowTime=" + borrowTime + ", periods="
                + periods + ", bId=" + bId + ", totalPeriods=" + totalPeriods
                + ", status=" + status + "]";
    }
    
//    {
//        "loanMoney":10000,
//        "periods":1,
//        "totalPeriods":12,
//        "status":1,
//        "bId":18,
//        "borrowTime":1469584225000,
//        "pro_code":"大零钱贷0014"
//    }
}
