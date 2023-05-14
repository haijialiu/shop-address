package com.hziee.shop_address.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReceivingInfoVoWebRes extends WebResponse{

    ReceivingInfoVo data;
    public ReceivingInfoVoWebRes(Integer code,String message,ReceivingInfoVo receivingInfoVo){
        super(code, message);
        this.data =receivingInfoVo;
    }
}
