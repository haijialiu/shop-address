package com.hziee.shop_address.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceivingInfoVo {
    private Integer id;
    private String consigneeName;
    private String consigneeTel;
    private List<Integer> cityIds;
    private Integer status;
    private String cityName;
    private String postcode;
    private String addressDetail;
    private boolean defaultAddress;

}
