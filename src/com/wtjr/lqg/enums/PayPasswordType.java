package com.wtjr.lqg.enums;

/**
 * 有没有设置过支付密码
 * @author JoLie
 *
 */
public enum PayPasswordType {
    /**
     * 有设置过支付密码
     */
    PasswordHaveSetPay,
    /**
     * 没有设置过支付密码
     */
    PayPasswordNotHaveSet,
    /**
     * 不明确是否设置过支付密码
     */
    PayPasswordNotUnequivocal
}
