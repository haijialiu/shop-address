package com.hziee.shop_address.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hziee.shop_address.entity.ReceivingInfo;
import com.hziee.shop_address.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ReceivingInfoMapper extends BaseMapper<ReceivingInfo> {

}
