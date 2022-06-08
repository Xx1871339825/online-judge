package com.crowoj.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.crowoj.api.entity.User;
import com.crowoj.api.entity.UserAuths;
import com.crowoj.api.param.*;
import com.crowoj.api.vo.JWTVO;

/**
 * @author crow
 * @create 2021/10/19 20:15
 * @description
 */
public interface UserAuthsService extends IService<UserAuths> {

    JWTVO adminSignIn(SignInParam param);

    JWTVO signIn(SignInParam signInParam);

    JWTVO sinUp(SignUpParam signUpParam);

    //UserAuths getUserAuthsByIdentifierAndIdentityType(String identifier, IdentityType identityType);

    JWTVO refresh(String token,String identity);

    Boolean updatePwd(UpdatePwdParam param, User user);

    Boolean updateEmail(UpdateEmailParam param, User user);

    Boolean forgetPassword(ForgetPasswordParam param);
}
