package com.hziee.shop_address.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hziee.shop_address.entity.ReceivingInfo;
import com.hziee.shop_address.mapper.ReceivingInfoMapper;
import com.hziee.shop_address.service.ReceivingInfoIService;
import org.springframework.stereotype.Service;

@Service
public class ReceivingInfoService extends ServiceImpl<ReceivingInfoMapper, ReceivingInfo> implements ReceivingInfoIService {
}
