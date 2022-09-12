package com.hotel.util;

import com.hotel.common.dto.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DBUtil {

    @Autowired
    CommonMapper commonMapper;

    @Value("${spring.datasource.schema-name}")
    String schema_name;

    /**
     * 해당 테이블에서 다음에 생성될 PK숫자 조회
     * 호텔등록, 사용자 등록등 등록시에 pk를 알지못해 insert_user, update_user를 넣지 못하는 문제 때문에 생성
     * @param : 테이블명
     * @return
     */
    public int getAutoIncrementNext(String tableName) throws Exception{
        Map<String, String> data = new HashMap<>();
        data.put("schema_name",schema_name);
        data.put("table_name",tableName);

        return commonMapper.getAutoIncrementNext(data);
    }
}
