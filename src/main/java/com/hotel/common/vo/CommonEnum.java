package com.hotel.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

public class CommonEnum {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum UserRole{
        MEMBER(1, "회원"),
        OWNER(2, "사업자")
        ;

        Integer code;
        String name;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum HotelTags{
        BUFFET(1, "뷔페"),
        PARKING_LOT(2, "주차장"),
        COFFEE_SHOP(3, "커피숍"),
        SWIMMING_POOL(4, "수영장"),
        FITNESS_CENTER(5, "헬스장"),
        ROOM_SERVICE(6, "룸서비스"),
        WINE_BAR_RESTAURANT(7, "와인바&레스토랑"),
        TWENTY_FOUR_HOUR_DESK(8, "24시간 데스크"),
        COMPANION_DOG(9, "애견동반"),
        SPA_SAUNA(10, "스파&사우나")
        ;

        Integer code;
        String name;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum RoomTags{
        NON_SMOKING_FACILITIES(1, "금연시설"),
        CITY_VIEW(2, "시티뷰"),
        OCEAN_VIEW(3, "오션뷰"),
        BATH(4, "욕조"),
        FREE_WIFI(5, "무료와이파이"),
        BREAKFAST(6, "조식"),
        SPA(7, "스파"),
        EARLY_CHECK_IN(8, "얼리체크인"),
        RATE_CHECK_IN(9, "레이트체크인"),
        OUTSIDE_FOOD_AVAILABLE(10, "외부음식가능")
        ;

        Integer code;
        String name;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum MemberDeleteReason{
        NON_SMOKING_FACILITIES(1, "예약불편"),
        CITY_VIEW(2, "사용이 어려움"),
        OCEAN_VIEW(3, "결재 불편"),
        USE_OTHER_SITES(4, "다른사이트 이용"),
        PERSONAL_REASON(5, "개인사정")
        ;

        Integer code;
        String name;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum OwnerDeleteReason{
        NO_SALES(1, "매출없음"),
        BANKRUPTCY(2, "파산"),
        SERVICE_COMPLAINTS(3, "서비스 불만"),
        USE_OTHER_SITES(4, "다른사이트 이용"),
        PERSONAL_REASON(5, "개인사정")
        ;

        Integer code;
        String name;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum Region {
        SEOUL(1, "서울"),
        BUSAN(2, "부산"),
        DAEGU(3, "대구"),
        INCHEON(4, "인천"),
        GWANGJU(5, "광주"),
        DAEJEON(6, "대전"),
        ULSAN(7, "울산"),
        SEJONG(8, "세종"),
        GYEONGGI(9, "경기"),
        GANGWON(10, "강원"),
        CHUNGBUK(11, "충북"),
        CHUNGNAM(12, "충남"),
        JEONBUK(13, "전북"),
        JEONNAM(14, "전남"),
        GYEONGBUK(15, "경북"),
        GYEONGNAM(16, "경남"),
        JEJU(17, "제주")
        ;

        Integer code;
        String name;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum TouristSpot{
        SEOUL(1, "서울"),
        BUSAN(2, "부산"),
        JEJU(3, "제주"),
        GANGNEUNG(4, "강릉"),
        SOKCHO(5, "속초"),
        YEOSU(6, "여수")
        ;

        Integer code;
        String name;

    }
}
