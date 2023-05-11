package com.hziee.shop_address.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceivingInfo {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String ConsigneeName;
    private String consigneeTel;
    private Integer cityId;
    private String postcode;
    private String addressDetail;
    private boolean defaultAddress;
    private Integer status;
    private Integer userId;

}
