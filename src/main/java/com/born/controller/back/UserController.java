package com.born.controller.back;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.born.domain.entity.User;
import com.born.service.UserService;
import com.born.common.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @Description:  考虑post、get、delete、putMapping的使用
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-10-07 21:20:12
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;


    /*
    分页查询数据  相比全查询后交给前端分页单次效率更高，但是由前端分页的话我们希望翻页不再需要再次查询后端，类似缓存，多次使用效率高，因此还是使用前端分页，不要在后端分页了。
     */
    @GetMapping("/listPage")
    public Result listPage(@RequestParam User user, @RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "5") Integer pageSize){
        if (currentPage==null || currentPage<1){
            currentPage=1;
        }
        if (pageSize==null || pageSize<1){
            pageSize=1;
        }
        Page page = new Page(currentPage, pageSize);
        IPage<User> pageData = userService.page(page, new QueryWrapper<User>().orderByDesc("created"));
        return Result.success(pageData);
    }

    @GetMapping("/list")
    public Result list(){
        return Result.success(userService.list());
    }

    @GetMapping("/get/{userId}")
    public Result detail(@PathVariable(name="userId")Long userId){
        User user = userService.getById(userId);
        Assert.notNull(user,"该用户不存在！");
        return Result.success(user);
    }

    @PostMapping("/edit")
    public Result edit(@Validated @RequestBody User user){
        User temp;
        //编辑
        if (null!=user.getUserId()){
            temp=userService.getById(user.getUserId());
        }
        //添加
        else{
            temp=new User();
            //设置新用户的最新登录时间为当前时间
            temp.setUserLastLoginDate(LocalDateTime.now());
        }
        BeanUtils.copyProperties(user,temp,"userId","userLastLoginDate");
        userService.saveOrUpdate(temp);
        return Result.success(temp);
        //return Result.success(user);
    }

    @DeleteMapping("/del/{userId}")
    public Result delete(@PathVariable(name="userId")Long userId){
        return userService.removeById(userId)?Result.success():Result.fail();
    }

}
