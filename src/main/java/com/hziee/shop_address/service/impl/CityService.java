package com.hziee.shop_address.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hziee.shop_address.entity.City;
import com.hziee.shop_address.entity.CityAndIdsDto;
import com.hziee.shop_address.mapper.CityMapper;
import com.hziee.shop_address.service.CityIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CityService extends ServiceImpl<CityMapper, City> implements CityIService{
    private CityMapper cityMapper;
    @Autowired
    public void setCityMapper(CityMapper cityMapper) {
        this.cityMapper = cityMapper;
    }
    public List<City> getCitiesByRelay(Integer relay){
        QueryWrapper<City> queryWrapper = new QueryWrapper<>();
        if(relay!=null) {
            queryWrapper.eq("relay", relay);
        }else{
            queryWrapper.isNull("relay");
        }
        return this.list(queryWrapper);
    }
    @Override
    public CityAndIdsDto getCityNameAndIds(Integer city_id) {
        //北京-北京市-丰台区,给的是丰台区。0：丰台；1，北京市；2，北京  澳门-澳门半岛 0：半岛；1：澳门
        String city_0_name,city_1_name,city_2_name;
        CityAndIdsDto cityAndIdsDto = new CityAndIdsDto();
        ArrayList<Integer> ids = new ArrayList<>();
        StringBuilder cityName = new StringBuilder();
        City city_0 = cityMapper.selectById(city_id);
        if(city_0!=null) {//南湖区，朝阳区，澳门半岛
            ids.add(city_0.getId());
            city_0_name=city_0.getName();
            QueryWrapper<City> city_1Wrapper = new QueryWrapper<>();
            city_1Wrapper.eq("id", city_0.getRelay());
            City city_1 = cityMapper.selectOne(city_1Wrapper);
            if (city_1!=null){//嘉兴市，北京市，澳门特别行政区
                ids.add(city_1.getId());
                city_1_name=city_1.getName();
                QueryWrapper<City> city_2Wrapper = new QueryWrapper<>();
                city_2Wrapper.eq("id", city_1.getRelay());
                City city_2 = cityMapper.selectOne(city_2Wrapper);
                if(city_2!=null){//浙江省，北京
                    ids.add(city_2.getId());
                    city_2_name=city_2.getName();
                    if(city_1_name.contains(city_2_name)){//北京市包含北京 1 2 3
                        cityName.append(city_1_name).append(city_0_name);
                    }else{
                        cityName.append(city_2_name).append(city_1_name).append(city_0_name);//浙江省嘉兴市南湖区 1 2 3
                    }
                } else {
                    cityName.append(city_1_name).append(city_0_name);
                }
            }else{
                cityName.append(city_0_name);
            }
        }
        cityAndIdsDto.setCityName(cityName.toString());
        cityAndIdsDto.setIds(ids);
        return cityAndIdsDto;
    }
}
