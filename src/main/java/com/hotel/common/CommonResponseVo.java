package com.hotel.common;

import java.util.Map;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
    스웨거가 제네릭 타입을 인식하지 못해서 response 데이터는 잘 내려가는데
    스웨거에서 해당 응답결과에 대한 설명이 안나오는 문제로 변경
    통합 응답 Vo는 result, message만 제공

    각 Vo에서 통합 응답 Vo를 상속받아 Response데이터 각각 구현.

    ex) 사업자 정보를 리턴해줘야 할때
    Response : OwnerVo 의 OwnerInfoResponse (extends CommonResponseVo) : 상속받았으므로 result, message 구현가능
    data 는 Object나 <T> 같은 제너릭타입은 스웨거가 인식불가하므로 반드시 리턴할 Response 정보를 담고있는 Vo를 같이 넣어줘야함
    (OwnerVo.OwnerInfo data;)

    아래는 구현한 Response 예시

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "Response")
    public static class OwnerInfoResponse extends CommonResponseVo {
        @Schema(description = "사업자 정보 데이터")
        OwnerVo.OwnerInfo data;
    }

 */

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
