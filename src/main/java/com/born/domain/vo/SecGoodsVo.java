package com.born.domain.vo;

import com.born.domain.entity.SecGoods;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Description:
 * @Since: jdk1.8
 * @Author: gyk
 * @Date: 2020-10-08 21:37:19
 */
@Data
public class SecGoodsVo extends SecGoods {

    private String goodsDescription;

    private String goodsImg;

    private BigDecimal goodsPrice;

}
