package com.hziee.shop_address.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hziee.shop_address.entity.City;
import com.hziee.shop_address.entity.ReceivingInfo;
import com.hziee.shop_address.entity.User;
import com.hziee.shop_address.entity.vo.ReceivingInfoVo;
import com.hziee.shop_address.service.impl.CityService;
import com.hziee.shop_address.service.impl.ReceivingInfoService;
import com.hziee.shop_address.utils.LayuiTableForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AddressController {
    private CityService cityService;
    private ReceivingInfoService receivingInfoService;

    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @Autowired
    public void setReceivingInfoService(ReceivingInfoService receivingInfoService) {
        this.receivingInfoService = receivingInfoService;
    }

    @PostMapping("/city-get")
    @ResponseBody
    public List<City> cityQuery(Integer relay) {
        return this.cityService.getCitiesByRelay(relay);
    }

    @PostMapping("/receiving_info-submit")
    @ResponseBody
    public String submitReceivingInfo(ReceivingInfo receivingInfo, @Param("city_0") Integer city_0, @Param("city_1") Integer city_1, @Param("city_2") Integer city_2) {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        receivingInfo.setUserId(user.getId());

        // if it is the first address, the address will be default.
        if (receivingInfoService.getUserAddressInfos(user,-1,-1).getRecords().isEmpty()) {
            receivingInfo.setDefaultAddress(true);
        }
        // if it set default, others (whatever has) will clean the default address
        if(receivingInfo.isDefaultAddress()){
            receivingInfoService.cleanDefaultAddress(user);
        }


        Integer city_id = city_2 != null ? city_2 : (city_1 != null ? city_1 : city_0);
        receivingInfo.setCityId(city_id);
        System.out.println(receivingInfo);
        boolean save = receivingInfoService.save(receivingInfo);
        return save ? "ok" : "error";
    }

    @PostMapping("/receiving_info-update")
    @ResponseBody
    public String updateReceivingInfo() {

        return null;
    }
    @PostMapping("/receiving_info-get")
    @ResponseBody
    public LayuiTableForm getReceivingInfo(Integer page, Integer limit) {
        User user = new User();
        user.setId(1);
        Page<ReceivingInfo> userAddressInfos = receivingInfoService.getUserAddressInfos(user, page, limit);
        List<ReceivingInfoVo> receivingInfoVos = userAddressInfos.getRecords().stream().map(
                receivingInfo -> new ReceivingInfoVo(
                        receivingInfo.getId(),
                        receivingInfo.getConsigneeName(),
                        receivingInfo.getConsigneeTel(),
                        receivingInfoService.getCityName(receivingInfo.getCityId()),
                        receivingInfo.getPostcode(),
                        receivingInfo.getAddressDetail(),
                        receivingInfo.isDefaultAddress()
                        )
        ).toList();
        return new LayuiTableForm("0", "获取失败",userAddressInfos.getTotal(), receivingInfoVos);
    }
}
