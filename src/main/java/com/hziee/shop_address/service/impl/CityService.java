package com.hziee.shop_address.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hziee.shop_address.entity.City;
import com.hziee.shop_address.mapper.CityMapper;
import com.hziee.shop_address.service.CityIService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService extends ServiceImpl<CityMapper, City> implements CityIService{
    public List<City> getCitiesByRelay(Integer relay){
        QueryWrapper<City> queryWrapper = new QueryWrapper<>();
        if(relay!=null) {
            queryWrapper.eq("relay", relay);
        }else{
            queryWrapper.isNull("relay");
        }
        return this.list(queryWrapper);
    }
}
