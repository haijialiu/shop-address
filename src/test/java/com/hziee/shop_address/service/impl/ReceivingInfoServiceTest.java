package com.hziee.shop_address.service.impl;

import com.hziee.shop_address.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReceivingInfoServiceTest {
    @Autowired
    private ReceivingInfoService receivingInfoService;
    @Test
    void getUserAddressInfos() {

    }

    @Test
    @DisplayName("清除用户原有的默认地址")
    void cleanDefaultAddress() {
        User user = new User();
        user.setId(1);
        receivingInfoService.cleanDefaultAddress(user);
    }

}