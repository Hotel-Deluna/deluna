package com.hotel.owner.dto;

import com.hotel.owner.vo.OwnerVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface OwnerMapper {
    int ownerSignUp(OwnerVo.OwnerSignUpRequest ownerSignUpRequest) throws Exception;

    String getOwnerTableAutoIncrementNext();

    String checkDuplicationOwner(String business_num);
}
