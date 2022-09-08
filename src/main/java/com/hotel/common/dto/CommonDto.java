package com.hotel.common.dto;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * 테이블에서 공통으로 쓰이는 컬럼
 * 타 vo에서 Mapper로 조회, 업데이트시 상속받아서 사용하려고 생성
 * (vo는 이런 데이터가 없으므로..)
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonDto {

    @ApiParam(value = "삭제여부", required = false)
    String is_delete;

    @ApiParam(value = "생성일", required = false)
    Date dt_inert;

    @ApiParam(value = "변경일", required = false)
    Date dt_update;

    @ApiParam(value = "생성자", required = false)
    String insert_user;

    @ApiParam(value = "변경자", required = false)
    String update_user;

}
