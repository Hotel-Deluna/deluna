package com.hotel.common.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CommonEnum {

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum UserRole{
        MEMBER(1, "회원"),
        OWNER(2, "사업자"),
        NONMEMBER(3, "비회원"),
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

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum TableName{
        D_BUSINESS_DELETE("d_business_delete"),
        D_BUSINESS_MEMBER("d_business_member"),
        D_EMAIL_AUTH("d_email_auth"),
        D_HOLIDAY_INFO("d_holiday_info"),
        D_HOTEL("d_hotel"),
        D_HOTEL_DELETE("d_hotel_delete"),
        D_HOTEL_REFERENCE_TAG("d_hotel_reference_tag"),
        D_HOTEL_TAG("d_hotel_tag"),
        D_IMAGE("d_image"),
        D_MEMBER("d_member"),
        D_MEMBER_DELETE("d_member_delete"),
        D_PAYMENT("d_payment"),
        D_PAYMENT_DETAIL("d_payment_detail"),
        D_PEAK_SEASON("d_peak_season"),
        D_PEAK_SEASON_DELETE("d_peak_season_delete"),
        D_PHONE_AUTH("d_phone_auth"),
        D_RESERVATION("d_reservation"),
        D_RESERVATION_DELETE("d_reservation_delete"),
        D_ROOM("d_room"),
        D_ROOM_DELETE("d_room_delete"),
        D_ROOM_DETAIL("d_room_detail"),
        D_ROOM_DETAIL_DELETE("d_room_detail_delete"),
        D_ROOM_REFERENCE_TAG("d_room_reference_tag"),
        D_ROOM_TAG("d_room_tag"),
        D_TOURIST_SPOT("d_tourist_spot")
        ;

        String name;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum ImageType{
        HOTEL(1, "호텔"),
        ROOM(2, "객실"),
        TOURIST_SPOT(3, "여행지")
        ;

        Integer code;
        String name;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum RoomDetailStatus{
        AVAILABLE(1, "예약가능"),
        UNAVAILABLE(2, "예약불가"),
        ;

        Integer code;
        String name;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum HolidayPriceStatus{
        NO_PEAK_SEASON_PRICE(1, "비성수기 주말가격"),
        PEAK_SEASON_PRICE(2, "성수기 주말가격"),
        ;

        Integer code;
        String name;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum PriceType{

        WEEKDAY_PRICE(1, "평일"),
        WEEKEND_PRICE(2, "주말"),
        P_WEEKDAY_PRICE(3, "성수기 평일"),
        P_WEEKEND_PRICE(4, "성수기 주말"),
       ;

        Integer code;
        String name;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum DayCode{
        SUN(1, "일요일", false),
        MON(2, "월요일", false),
        TUE(3, "화요일", false),
        WED(4, "수요일", false),
        THU(5, "목요일", false),
        FRI(6, "금요일", true),
        SAT(7, "토요일", true),
        ;

        Integer code;
        String name;
        Boolean isWeekend;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum HotelReservationListRankCode{
        TOTAL(1, "전체"),
        RESERVATION_CONFIRMATION(2, "예약확정"),
        RESERVATION_CANCEL(3, "예약취소"),
        COMPLETED_USE(4, "이용완료"),
        ;

        Integer code;
        String name;
    }

}
