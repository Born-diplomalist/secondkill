package com.born.controller.back;


import cn.hutool.core.lang.Assert;
import com.born.common.Result;
import com.born.domain.entity.Goods;
import com.born.domain.entity.Goods;
import com.born.service.GoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author born
 * @since 2020-10-07
 */
@RestController
@RequestMapping("goods")
public class GoodsController {
    
    @Autowired
    private GoodsService goodsService;
    
    @GetMapping("/list")
    public Result list(){
        return Result.success(goodsService.list());
    }

    @GetMapping("/get/{goodsId}")
    public Result detail(@PathVariable(name="goodsId")Long goodsId){
        Goods goods = goodsService.getById(goodsId);
        Assert.notNull(goods,"该商品不存在！");
        return Result.success(goods);
    }

    @PostMapping("/edit")
    public Result edit(@Validated @RequestBody Goods goods){
        Goods temp;
        //编辑
        if (null!=goods.getGoodsId()){
            temp=goodsService.getById(goods.getGoodsId());
        }
        //添加
        else{
            temp=new Goods();
        }
        BeanUtils.copyProperties(goods,temp,"goodsId");
        goodsService.saveOrUpdate(temp);
        return Result.success(temp);
    }

    @DeleteMapping("/del/{goodsId}")
    public Result delete(@PathVariable(name="goodsId")Long goodsId){
        return goodsService.removeById(goodsId)?Result.success():Result.fail();
    }

}
