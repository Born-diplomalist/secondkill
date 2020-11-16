package com.born.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.born.common.Result;
import com.born.common.Status;
import com.born.common.exception.GlobalException;
import com.born.utils.MD5Util;
import com.born.utils.UUIDUtils;
import com.born.config.redis.UserPrefix;
import com.born.domain.entity.User;
import com.born.domain.mapper.UserMapper;
import com.born.domain.vo.LoginVo;
import com.born.service.RedisService;
import com.born.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author born
 * @since 2020-10-07
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private  UserMapper userMapper;

    @Autowired
    private RedisService redisService;

    public static final String COOKIE_NAME_TOKEN="token";

    @Override
    public Result login(HttpServletResponse response, LoginVo loginVo) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("user_name", loginVo.getUserName()));
        //验证密码
        //模拟前端的MD5加密
        String password = MD5Util.inputPassToFormPass(loginVo.getUserPassword());
        //根据表单密码以及数据库盐计算出应在数据库的密码，作比对
        String calcDbPass = MD5Util.formPassToDBPass(password, user.getUserSalt());
        if (!calcDbPass.equals(user.getUserPassword())) {
            throw new GlobalException(Status.PASSWORD_ERROR);
        }
        //信息存入分布式session，生成Cookie
        /*
        注意此处的一个问题：重复登录同一用户，会生成多个token，除最新的token外，其他token都会被“遗忘”
        TODO：解决方案
        每次生成token后，除了设置token-user这个k-v外，再设置一个key，以userId为key，value为生成的token。
        每次生成token之前，查看对应userId下是否已经有token了，如果有就直接以这个token来addUserCookie（以重新续期redis的token与客户端的cookie）

         */
        String token = generateToken(user.getUserName());
        addUserCookie(response,token, user);
        return Result.success(token);
    }

    /**
     * 登出
     * 找到token对应的cookie，进而找到对应的redis中的token键值对
     * 删除cookie和redis中token相关的两个键值对
     *
     */
    @Override
    public Result logOut(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String token="";
        for (Cookie c : cookies) {
            if ("token".equals(c.getName())){
                token=c.getValue();
                //删除Cookie
                c.setMaxAge(0);
                break;
            }
        }
        if (!"".equals(token)){
            //删除token对应key以及验证token存在的对应key
            User user = redisService.get(UserPrefix.token, token, User.class);
            redisService.delete(UserPrefix.token, token);
            if (null!=user){
                redisService.delete(UserPrefix.checkTokenExist,user.getUserName());
            }
        }
        return new Result(Status.LOGOUT_SUCCESS);
    }

    /**
     * 生成token
     * 如果不存在，使用UUID生成
     * 如果该用户名已有对应的token，复用该token来存储，仅仅续期即可
     *
     * 如果不复用都直接使用UUID生成，可能导致重复登录同一用户时生成多个token，之前的token直接被遗忘，高并发下，大量空间被浪费，不过Redis自有淘汰机制，不复用也是合理的
     */
    private String generateToken(String userName){
        String token = redisService.get(UserPrefix.checkTokenExist, userName, String.class);
        if (token==null){
            token = UUIDUtils.uuid();
        }
        return token;
    }

    /**
     * 添加token到redis，并在客户端生成cookie存储token
     */
    private void addUserCookie(HttpServletResponse response, String token, User user){
        redisService.set(UserPrefix.token, token, user);
        redisService.set(UserPrefix.checkTokenExist, user.getUserName(), token);
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //cookie和redis中对应的k-v同时过期
        cookie.setMaxAge(UserPrefix.token.getExpireSeconds());
        cookie.setPath("/");
        //将cookie写入客户端
        response.addCookie(cookie);
    }

}

