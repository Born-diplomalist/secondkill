package com.born.controller.back;


import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.born.common.Result;
import com.born.common.Status;
import com.born.common.exception.GlobalException;
import com.born.domain.entity.Goods;
import com.born.domain.entity.SecGoods;
import com.born.service.GoodsService;
import com.born.service.SecGoodsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * @Description: 秒杀商品管理
 * @author born
 * @since 2020-10-08
 */
@RestController
@RequestMapping("seckill")
public class SecGoodsController {

    @Autowired
    private SecGoodsService secGoodsService;

    @Autowired
    private GoodsService goodsService;


    @GetMapping("/list")
    public Result list(){
        return Result.success(secGoodsService.list());
    }

    @GetMapping("/get/{secGoodsId}")
    public Result getDetail(@PathVariable(name = "secGoodsId") Long secGoodsId){
        SecGoods secGoods = secGoodsService.getById(secGoodsId);
        Assert.notNull(secGoods,"该秒杀商品不存在！");
        return Result.success(secGoods);
    }

    //RequestBody是从body里接收数据  RequestParam是从请求参数中接收
    @PostMapping("/edit")
    public Result edit(@Validated @RequestBody SecGoods secGoods){
        SecGoods temp = new SecGoods();
        //如果id已存在，证明是修改，从数据库查出原对象以此为基础修改
        if (null!=secGoods.getSecGoodsId()){
            temp = secGoodsService.getById(secGoods.getSecGoodsId());
        }
        BeanUtils.copyProperties(secGoods,temp,"secGoodsId");
        secGoodsService.saveOrUpdate(temp);
        return Result.success(temp);

    }

    /**
     * 删除秒杀商品
     * 需要将库存补回到对应商品中
     * @return
     */
    @DeleteMapping("/del/{secGoodsId}")
    public Result delete(@PathVariable(name = "secGoodsId") Long secGoodsId){
        SecGoods secGoods = secGoodsService.getById(secGoodsId);
        if (null==secGoods){
            throw new GlobalException(Status.SECKILL_NOT_EXIST);
        }
        //回补库存到商品表
        if (0!=secGoods.getSecGoodsStock()){
            @NotNull(message = "商品ID不可为空") Long secGoodsGoodsId = secGoods.getSecGoodsGoodsId();
            Goods goods = goodsService.getById(secGoodsGoodsId);
            if (null!=goods){
                goodsService.update(new UpdateWrapper<Goods>().eq("goods_id",secGoods.getSecGoodsGoodsId()).set("goods_stock",goods.getGoodsStock()+secGoods.getSecGoodsStock()));
            }else {
                throw new GlobalException(Status.GOODS_ABNORMAL_DELETE);
            }
        }
        return Result.success(secGoodsService.removeById(secGoodsId));
    }

}
