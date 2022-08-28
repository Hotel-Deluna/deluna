package com.hotel.reservation.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hotel.common.CommonResponseVo;
import com.hotel.owner.vo.OwnerVo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public class UnMemberInfoVo {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "비회원 예약정보 조회")
    public static class UnMemberResponse extends CommonResponseVo {
        @Schema(description = "데이터")
        UnMemberInfoVo.UnMemberReservationInfo data;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "비회원 예약조회 파라미터")
    public static class UnMemberReservationRequest {
        @Schema(description = "한글 이름",  required = true, example = "임꺽정")
        String name;

        @Schema(description = "비회원 전화번호", required = true, example = "01012345678")
        String phone_num;

        @Schema(description = "예약번호", required = true, example = "1108668")
        String reservation_num;
    }

    @Data
    @NoArgsConstructor
    @Schema(description = "비회원 예약 조회")
    public static class UnMemberReservationInfo {

        @Schema(description = "예약번호", required = true, example = "1111111")
        String resrvation_num;
        
        @Schema(description = "예약자명", required = true, example = "홍길동")
        String resrvation_name;
        
        @Schema(description = "휴대폰번호", required = true, example = "01033334444")
        String resrvation_phone;
        
        @Schema(description = "호텔명", required = true, example = "호텔 델루나")
        String name;
        
        @Schema(description = "객실명", required = true, example = "스탠다드 룸")
        String room_detail_name;
        
        @Schema(description = "투숙인원", required = true, example = "2")
        Integer resrvation_peple;
        
        @Schema(description = "예약 시작일", required = true, example = "2022-08-28 14:20:00")
        String st_date;
        
        @Schema(description = "예약 종료일", required = true, example = "2022-08-29 11:00:00")
        String ed_date;
        
        @Schema(description = "결제금액", required = true, example = "300000")
        BigDecimal resrvation_price;
        
        @Schema(description = "예약상태", required = true, example = "0")
        String resrvation_status;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "비회원 예약 취소")
    public static class UnMemberWithdrawRequest {
    	
    	@Schema(description = "비회원 예약번호",  required = true, example = "1108668")
        String resrvation_num;
    	
        @Schema(description = "비회원 탈퇴 사유",  required = true, example = "일정변경")
        String reason;

    }


}
