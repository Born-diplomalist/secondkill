package com.born.service;

import com.born.domain.entity.SecGoods;
import com.baomidou.mybatisplus.extension.service.IService;
import com.born.domain.vo.SecGoodsVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author born
 * @since 2020-10-08
 */
public interface SecGoodsService extends IService<SecGoods> {

    List<SecGoodsVo> listWithGoods();

    SecGoodsVo getWithGoods(Long secGoodsId);
}
