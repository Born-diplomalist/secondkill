package com.born.domain.mapper;

import com.born.domain.entity.SecGoods;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.born.domain.vo.SecGoodsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author born
 * @since 2020-10-08
 */
@Mapper
public interface SecGoodsMapper extends BaseMapper<SecGoods> {

    @Select("select sg.*,g.goods_description,g.goods_img,g.goods_price from sec_goods sg left join goods g on sg.sec_goods_goods_id = g.goods_id")
    List<SecGoodsVo> listWithGoods();

    @Select("select sg.*,g.goods_description,g.goods_img,g.goods_price from sec_goods sg left join goods g on sg.sec_goods_goods_id = g.goods_id where g.goods_id=#{secGoodsId}")
    SecGoodsVo getWithGoods(@Param("secGoodsId") Long secGoodsId);
}
