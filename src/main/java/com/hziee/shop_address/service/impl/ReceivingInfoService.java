package com.hziee.shop_address.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hziee.shop_address.entity.City;
import com.hziee.shop_address.entity.ReceivingInfo;
import com.hziee.shop_address.entity.User;
import com.hziee.shop_address.entity.vo.ReceivingInfoVo;
import com.hziee.shop_address.mapper.CityMapper;
import com.hziee.shop_address.mapper.ReceivingInfoMapper;
import com.hziee.shop_address.service.ReceivingInfoIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReceivingInfoService extends ServiceImpl<ReceivingInfoMapper, ReceivingInfo> implements ReceivingInfoIService {
    private ReceivingInfoMapper receivingInfoMapper;
    @Autowired
    public void setReceivingInfoMapper(ReceivingInfoMapper receivingInfoMapper) {
        this.receivingInfoMapper = receivingInfoMapper;
    }
    @Override
    public Page<ReceivingInfo> getUserAddressInfos(User user,Integer page,Integer limit) {
        Page<ReceivingInfo> iPage = new Page<>(page,limit);
        QueryWrapper<ReceivingInfo> queryWrapper = new QueryWrapper<>();
        //TODO deal exception
        assert user != null;
        queryWrapper.eq("user_id", user.getId()).eq("status",0).orderByDesc("default_address");
        return  receivingInfoMapper.selectPage(iPage, queryWrapper);
    }
    @Override
    public void cleanDefaultAddress(User user) {
        ReceivingInfo info = new ReceivingInfo();
        info.setDefaultAddress(false);
        int update = receivingInfoMapper.update(
                info,
                new QueryWrapper<ReceivingInfo>()
                        .eq("user_id",user.getId())
                        .eq("default_address",true)
        );
        System.out.println(update);
        if (update != 0 && update != 1)
            throw new RuntimeException("用户数据：默认地址的数量有误");
    }

}
