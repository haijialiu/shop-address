package com.hziee.shop_address.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CityAndIdsDto {
    private String cityName;
    private List<Integer> ids;
}
