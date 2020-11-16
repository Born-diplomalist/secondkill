package com.born.domain.mapper;

import com.born.domain.entity.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author born
 * @since 2020-10-08
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
