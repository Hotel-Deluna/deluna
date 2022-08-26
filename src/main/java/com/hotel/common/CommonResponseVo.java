package com.hotel.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "통합 응답 Vo")

public class CommonResponseVo {
    @Schema(description = "결과", required = true)
    String result = "OK"; // default = OK

    @Schema(description = "메시지", required = true)
    String message;

}
