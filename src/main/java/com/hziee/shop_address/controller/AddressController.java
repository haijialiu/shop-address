package com.hziee.shop_address.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hziee.shop_address.entity.City;
import com.hziee.shop_address.entity.CityAndIdsDto;
import com.hziee.shop_address.entity.ReceivingInfo;
import com.hziee.shop_address.entity.User;
import com.hziee.shop_address.entity.vo.ReceivingInfoVo;
import com.hziee.shop_address.entity.vo.ReceivingInfoVoWebRes;
import com.hziee.shop_address.entity.vo.WebResponse;
import com.hziee.shop_address.service.impl.CityService;
import com.hziee.shop_address.service.impl.ReceivingInfoService;
import com.hziee.shop_address.utils.LayuiTableForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.List;

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
    public WebResponse submitReceivingInfo(ReceivingInfo receivingInfo, @Param("city_0") Integer city_0, @Param("city_1") Integer city_1, @Param("city_2") Integer city_2) {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        receivingInfo.setUserId(user.getId());
        //不是临时保存
        if (receivingInfo.getStatus() != 1) {
            if (receivingInfo.getId() != null
                    && !receivingInfo.isDefaultAddress()
                    && receivingInfoService.getById(receivingInfo.getId()).isDefaultAddress()) {
                //check if attempt remove the default address.
                return new WebResponse(400, "至少得保留一个默认地址！");
            }

            // if it is the first address, the address will be default.
            if (receivingInfoService.getUserAddressInfos(user, -1, -1).getRecords().isEmpty()) {
                receivingInfo.setDefaultAddress(true);
            }

            // if it set default, others (whatever has) will clean the default address.
            if (receivingInfo.isDefaultAddress()) {
                receivingInfoService.cleanDefaultAddress(user);
            }
        }
        Integer city_id = city_2 != null ? city_2 : (city_1 != null ? city_1 : city_0);
        receivingInfo.setCityId(city_id);

        Integer oldId = receivingInfo.getId();
        boolean saveOrUpdate = receivingInfoService.saveOrUpdate(receivingInfo);
        if(oldId==null){
            //第一次插入，如果是临时保存的就需要返回主键让前台更新
            ReceivingInfoVo receivingInfoVo = new ReceivingInfoVo();
            receivingInfoVo.setId(receivingInfo.getId());
            //只塞了id
            return new ReceivingInfoVoWebRes(201,"临时保存成功", receivingInfoVo);
        }
        return new WebResponse(200, saveOrUpdate ? "更新成功" : "添加成功");
    }

    @PostMapping("/receiving_info-get")
    @ResponseBody
    public LayuiTableForm getReceivingInfo(Integer page, Integer limit) {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        Page<ReceivingInfo> userAddressInfos = receivingInfoService.getUserAddressInfos(user, page, limit);
        List<ReceivingInfoVo> receivingInfoVos = userAddressInfos.getRecords().stream().map(
                receivingInfo -> {
                    CityAndIdsDto cityNameAndIds = cityService.getCityNameAndIds(receivingInfo.getCityId());
                    return new ReceivingInfoVo(
                            receivingInfo.getId(),
                            receivingInfo.getConsigneeName(),
                            receivingInfo.getConsigneeTel(),
                            cityNameAndIds.getIds(),
                            receivingInfo.getStatus(),
                            cityNameAndIds.getCityName(),
                            receivingInfo.getPostcode(),
                            receivingInfo.getAddressDetail(),
                            receivingInfo.isDefaultAddress()
                    );
                }
        ).toList();
        return new LayuiTableForm("0", "获取失败", userAddressInfos.getTotal(), receivingInfoVos);
    }

    @PostMapping("/tem_receiving_info-get")
    @ResponseBody
    public ReceivingInfoVo getTemReceivingInfo() {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        QueryWrapper<ReceivingInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId()).eq("status", 1);
        ReceivingInfo receivingInfo = receivingInfoService.getOne(queryWrapper);
        if (receivingInfo != null) {
            CityAndIdsDto cityNameAndIds = cityService.getCityNameAndIds(receivingInfo.getCityId());
            return new ReceivingInfoVo(receivingInfo.getId(),
                    receivingInfo.getConsigneeName(),
                    receivingInfo.getConsigneeTel(),
                    cityNameAndIds.getIds(),
                    receivingInfo.getStatus(),
                    cityNameAndIds.getCityName(),
                    receivingInfo.getPostcode(),
                    receivingInfo.getAddressDetail(),
                    receivingInfo.isDefaultAddress()
            );
        } else {
            return null;
        }
    }
//    @PostMapping("/tem_receiving_info-delete")
//    @ResponseBody
//    public String temReceivingInfoSubmit(){
//        User user = new User();
//        user.setId(1);
//        return null;
//    }

    @PostMapping("/receiving_info-delete")
    @ResponseBody
    @Transactional
    public WebResponse deleteReceivingInfo(Integer id) {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User user = (User) auth.getPrincipal();
        ReceivingInfo receivingInfo = receivingInfoService.getById(id);
        receivingInfo.setStatus(2);
        if (receivingInfo.isDefaultAddress()) {
            QueryWrapper<ReceivingInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("status", 0);
            List<ReceivingInfo> list = receivingInfoService.list(queryWrapper);
            if (!list.isEmpty()) {
                ReceivingInfo info = list.get(0);
                System.out.println(info);
                info.setDefaultAddress(true);

                receivingInfoService.updateById(receivingInfo);
                receivingInfoService.updateById(info);
            }else{
                throw new RuntimeException("没有其他地址了");
            }
        }
        return new WebResponse(200, "删除成功");
    }
}
