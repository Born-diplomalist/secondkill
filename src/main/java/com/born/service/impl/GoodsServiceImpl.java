package com.born.service.impl;

import com.born.domain.entity.Goods;
import com.born.domain.mapper.GoodsMapper;
import com.born.service.GoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author born
 * @since 2020-10-07
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {

}
