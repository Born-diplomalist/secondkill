package com.born.service;

import com.born.common.Result;
import com.born.domain.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.born.domain.vo.LoginVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author born
 * @since 2020-10-07
 */
public interface UserService extends IService<User> {

    Result login(HttpServletResponse response, LoginVo loginVo);

    Result logOut(HttpServletRequest request);
}
