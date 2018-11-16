package com.wtjr.lqg.enums;

/**
 * 区分是登录app后内部操作进行忘记密码的，还是没登录app在外部进行忘记密码的
 * @author JoLie
 *
 */
public enum ForgetLoginPasswordType {
    /**
     * 登录app后内部进行忘记密码的
     */
    ForgetLoginPasswordHaveLogin,
    /**
     * 没登录app在外部进行忘记密码的(有账号状态)
     */
    ForgetLoginPasswordNotLogin1,
    /**
     * 没登录app在外部进行忘记密码的(无账号状态)
     */
    ForgetLoginPasswordNotLogin2
}
