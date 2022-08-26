package com.hotel.company.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@ApiModel(value = "성수기 정보", description = "호텔의 성수기 정보")
@Data
public class PeakSeasonVo {

    @ApiModelProperty(value = "성수기 시작일", example = "2022/07/25")
    String peak_season_start;
    @ApiModelProperty(value = "성수기 종료일", example = "2022/09/01")
    String peak_season_end;
}
