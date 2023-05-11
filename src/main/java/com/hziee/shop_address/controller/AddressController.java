package com.hziee.shop_address.controller;

import com.hziee.shop_address.entity.City;
import com.hziee.shop_address.service.impl.CityService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AddressController {
    private CityService cityService;
    @Autowired
    public void setCityService(CityService cityService) {
        this.cityService = cityService;
    }

    @PostMapping("/city-get")
    @ResponseBody
    public List<City> cityQuery(@RequestBody Integer relay){
        System.out.println(relay);
        return this.cityService.getCitiesByRelay(relay);
    }
}
