package com.hziee.shop_address.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hziee.shop_address.entity.City;
import com.hziee.shop_address.entity.CityAndIdsDto;

public interface CityIService extends IService<City> {
    CityAndIdsDto getCityNameAndIds(Integer city_id);
}
