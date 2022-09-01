package com.hotel.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommonEnum {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum HotelTags{
        CODE1(1);

        Integer code;

    }
}
