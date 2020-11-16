package com.born.domain.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

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
public class SecGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "sec_goods_id", type = IdType.AUTO)
    private Long secGoodsId;

    /**
     * 秒杀商品对应的商品ID
     */
    @NotNull(message = "商品ID不可为空")
    private Long secGoodsGoodsId;

    /**
     * 秒杀商品对应商品名 常用字段 在此处做冗余
     */
    private String secGoodsGoodsName;

    /**
     * 秒杀价格
     */
    @NotNull(message = "秒杀价格不可为空")
    private BigDecimal secGoodsPrice;

    /**
     * 秒杀剩余数量
     */
    private Integer secGoodsStock;

    /**
     * 秒杀开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime secGoodsStartTime;

    /**
     * 秒杀结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime secGoodsEndTime;


}
