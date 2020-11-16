package com.born.domain.entity;

import java.math.BigDecimal;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author born
 * @since 2020-10-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
//order作为mysql关键字，需要使用``包起来
@TableName("`order`")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "order_id")
    private String orderId;

    private Long orderSecGoodsId;

    private Long orderGoodsId;

    private Long orderUserId;

    private BigDecimal orderSecGoodsPrice;

    private Integer orderStatus;


}
