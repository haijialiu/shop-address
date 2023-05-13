package com.hziee.shop_address.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LayuiTableForm {
    private String code;
    private String msg;
    private Long count;
    private List<?> data;
}
