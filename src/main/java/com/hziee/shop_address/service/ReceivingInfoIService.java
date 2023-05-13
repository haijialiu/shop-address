package com.hziee.shop_address.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hziee.shop_address.entity.ReceivingInfo;
import com.hziee.shop_address.entity.User;

import java.util.List;

public interface ReceivingInfoIService extends IService<ReceivingInfo> {
    Page<ReceivingInfo> getUserAddressInfos(User user, Integer page, Integer limit);
    void cleanDefaultAddress(User user);
    String getCityName(Integer city_id);
}
