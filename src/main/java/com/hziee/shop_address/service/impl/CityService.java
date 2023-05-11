package com.hziee.shop_address.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hziee.shop_address.entity.City;
import com.hziee.shop_address.mapper.CityMapper;
import com.hziee.shop_address.service.CityIService;
import org.springframework.stereotype.Service;

@Service
public class CityService extends ServiceImpl<CityMapper, City> implements CityIService{
}
