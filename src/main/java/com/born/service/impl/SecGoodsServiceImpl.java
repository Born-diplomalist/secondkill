package com.born.service.impl;

import com.born.domain.entity.SecGoods;
import com.born.domain.mapper.SecGoodsMapper;
import com.born.domain.vo.SecGoodsVo;
import com.born.service.SecGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author born
 * @since 2020-10-08
 */
@Service
public class SecGoodsServiceImpl extends ServiceImpl<SecGoodsMapper, SecGoods> implements SecGoodsService {

    @Autowired
    private SecGoodsMapper secGoodsMapper;

    @Override
    public List<SecGoodsVo> listWithGoods() {
        return secGoodsMapper.listWithGoods();
    }

    @Override
    public SecGoodsVo getWithGoods(Long secGoodsId) {
        return secGoodsMapper.getWithGoods(secGoodsId);
    }
}
