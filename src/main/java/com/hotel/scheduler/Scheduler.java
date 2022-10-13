package com.hotel.scheduler;

import com.hotel.common.svc.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    CommonService commonService;

    // 공휴일 정보 수집
    @Scheduled(cron = "0 0 1 * * ?")
    public void holidayCrawling() {
        commonService.HolidayCrawling();
    }

    // 여행지 정보 저장
    @Scheduled(cron = "0 1 * * * ?")
    public void SaveTouristSpotHotelCount() {
        commonService.SaveTouristSpotHotelCount();
    }
    
    // 예약일 만료 상태값 변경
    @Scheduled(cron = "0 0 1 * * ?")
    public void updateReservationEndDate() {
        commonService.updateReservationEndDate();
    }

    // 객실, 호실 예약 삭제
    @Scheduled(cron = "0 2 * * * ?")
    public void deleteRoom() {
        commonService.deleteRoom();
    }
}
