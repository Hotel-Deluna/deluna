package com.hotel.owner.dto;

import com.hotel.owner.vo.OwnerVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OwnerMapper {
    int ownerSignUp(OwnerVo.OwnerSignUpRequest ownerSignUpRequest) throws Exception;

    void ownerEditInfo(OwnerVo.OwnerInfo ownerInfo);

    OwnerVo.OwnerInfo selectOwnerInfo(int business_user_num);

    int ownerWithdraw(int business_user_num);

    int insertOwnerWithdrawReason(OwnerVo.OwnerWithdrawRequest ownerWithdrawRequest);

    String checkDuplicationOwner(String business_num);

    String checkDuplicationEmail(String email);
}
