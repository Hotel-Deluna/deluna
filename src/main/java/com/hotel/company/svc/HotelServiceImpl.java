package com.hotel.company.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.vo.CommonEnum;
import com.hotel.company.dto.HotelDto;
import com.hotel.company.dto.HotelMapper;
import com.hotel.util.AES256Util;
import com.hotel.util.DBUtil;
import com.hotel.util.DateUtil;
import com.hotel.company.vo.*;
import com.hotel.util.ImageUtil;
import io.micrometer.core.lang.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HotelServiceImpl implements HotelService {

    @Autowired
    ImageUtil imageUtil;

    @Autowired
    DBUtil dbUtil;

    @Autowired
    AES256Util aes256Util;

    @Autowired
    HotelMapper hotelMapper;

    @Override
    public HotelInfoVo.OwnerHotelListResponse OwnerHotelList(@Nullable HotelInfoVo.OwnerHotelListRequest ownerHotelListRequest) {
        HotelInfoVo.OwnerHotelListResponse result = new HotelInfoVo.OwnerHotelListResponse();

        try{
            List<HotelInfoVo.HotelDetailInfo> hotel_list = new ArrayList<>(); // 해당 사업자 정보로 소유호텔 리스트 조회

            HotelInfoVo.HotelDetailInfo hotelDetailInfo = new HotelInfoVo.HotelDetailInfo();

            // 객실
            List<String> roomImageList = new ArrayList<>();
            roomImageList.add("https://aws.bucket/1");
            roomImageList.add("https://aws.bucket/2");
            List<Integer> roomTagList = new ArrayList<>();
            roomTagList.add(1);
            roomTagList.add(3);

            HotelInfoVo.RoomInfo room_a = new HotelInfoVo.RoomInfo();
            room_a.setRoom_num(12345);
            room_a.setName("스탠다드 트윈룸");
            room_a.setImage(roomImageList);
            room_a.setMinimum_people(2);
            room_a.setMaximum_people(3);
            room_a.setDouble_bed_count(1);
            room_a.setSingle_bed_count(1);
            room_a.setCheck_in_time("15:00");
            room_a.setCheck_out_time("11:00");
            room_a.setPrice(130000);
            room_a.setReservable_room_count(5);
            room_a.setAvailable_yn(true);
            room_a.setTags(roomTagList);


            HotelInfoVo.RoomInfo room_b = new HotelInfoVo.RoomInfo();
            room_b.setRoom_num(45678);
            room_b.setName("디럭스 더블");
            room_b.setImage(roomImageList);
            room_b.setMinimum_people(2);
            room_b.setMaximum_people(3);
            room_b.setDouble_bed_count(2);
            room_b.setSingle_bed_count(1);
            room_b.setCheck_in_time("15:00");
            room_b.setCheck_out_time("11:00");
            room_b.setPrice(330000);
            room_b.setReservable_room_count(2);
            room_b.setAvailable_yn(false);
            room_b.setTags(roomTagList);

            List<HotelInfoVo.RoomInfo> room_list = new ArrayList<>();
            room_list.add(room_a);
            room_list.add(room_b);

            //성수기
            HotelInfoVo.PeakSeason season_a = new HotelInfoVo.PeakSeason();
            season_a.setPeak_season_start(DateUtil.stringToDate("2022/07/01"));
            season_a.setPeak_season_end(DateUtil.stringToDate("2022/08/31"));

            HotelInfoVo.PeakSeason season_b = new HotelInfoVo.PeakSeason();
            season_b.setPeak_season_start(DateUtil.stringToDate("2022/11/01"));
            season_b.setPeak_season_end(DateUtil.stringToDate("2022/01/31"));

            // 호텔
            List<String> hotelImageList = new ArrayList<>();
            hotelImageList.add("https://aws.bucket/45");
            hotelImageList.add("https://aws.bucket/67");

            List<HotelInfoVo.PeakSeason> hotelPeakSeasonList = new ArrayList<>();
            hotelPeakSeasonList.add(season_a);
            hotelPeakSeasonList.add(season_b);

            List<Integer> hotelTagList = new ArrayList<>();
            hotelTagList.add(4);
            hotelTagList.add(5);

            hotelDetailInfo.setHotel_num(123);
            hotelDetailInfo.setName("신라스테이");
            hotelDetailInfo.setEng_name("Shilla Stay");
            hotelDetailInfo.setAddress("서울특별시 강남구");
            hotelDetailInfo.setPhone_num("0212345678");
            hotelDetailInfo.setStar(5);
            hotelDetailInfo.setImage(hotelImageList);
            hotelDetailInfo.setInfo("서울 강남에 위치한 본 호텔은...");
            hotelDetailInfo.setRule("라멘 서비스 및 대욕장 이용안내...");
            hotelDetailInfo.setPeak_season_list(hotelPeakSeasonList);
            hotelDetailInfo.setTags(hotelTagList);
            hotelDetailInfo.setRoom_list(room_list);

            hotel_list.add(hotelDetailInfo);

            result.setMessage("호텔 리스트 조회 완료");
            result.setData(hotel_list);

        }catch (Exception e){

        }

        return result;
    }

    @Override
    @Transactional
    public CommonResponseVo RegisterHotel(HotelInfoVo.RegisterHotelRequest registerHotelRequest) {
        CommonResponseVo result = new CommonResponseVo();
        int hotelNum = 0;

        try{
            // registerHotelParamVo 호텔정보 + 성수기 + 호텔태그 + 이미지 정보 DB 저장
            log.info("호텔 등록 시작");

            String userRole = String.valueOf(CommonEnum.UserRole.OWNER.getCode());
            hotelNum = dbUtil.getAutoIncrementNext(CommonEnum.TableName.D_BUSINESS_MEMBER.getName());

            // 사업자번호 - JWT 파싱 임시조치로 하드코딩
            // TODO JWT 완성되면 변경
            int business_user_num = 3;

            String userPk = userRole + business_user_num; // 유저 구분 코드 + pk

            // 호텔등록 파라미터에서 사업자번호, 위도, 경도, 공휴일 가격상태 생성자, 변경자 정보 추가
            registerHotelRequest.setBusiness_user_num(business_user_num);
            registerHotelRequest.setPhone_num(aes256Util.encrypt(registerHotelRequest.getPhone_num())); // 전화번호 암호화
            registerHotelRequest.setLatitude(registerHotelRequest.getLocation().get(0));
            registerHotelRequest.setLongitude(registerHotelRequest.getLocation().get(1));
            registerHotelRequest.setHoliday_price_status(1); // 기본셋팅 - 비성수기 주말가격 객실수정에서 변경가능
            registerHotelRequest.setInsert_user(userPk);
            registerHotelRequest.setUpdate_user(userPk);
            hotelMapper.insertHotelInfo(registerHotelRequest);

            // Image Resizing & S3 Upload & DB insert
            if(registerHotelRequest.getImage() != null){
                InsertImage(registerHotelRequest.getImage(), CommonEnum.ImageType.HOTEL.getCode(), hotelNum,  userPk);
            }

            // 성수기정보 저장
            if(registerHotelRequest.getPeak_season_list() != null){
                for(HotelInfoVo.PeakSeason peakSeason : registerHotelRequest.getPeak_season_list()) {
                    HotelDto.PeekSeasonTable peekSeasonTable = new HotelDto.PeekSeasonTable();
                    peekSeasonTable.setPeak_season_std(peakSeason.getPeak_season_start());
                    peekSeasonTable.setPeak_season_end(peakSeason.getPeak_season_end());
                    peekSeasonTable.setHotel_num(hotelNum);
                    peekSeasonTable.setInsert_user(userPk);
                    peekSeasonTable.setUpdate_user(userPk);
                    hotelMapper.insertPeekSeason(peekSeasonTable);
                }
            }

            // 호텔 태그 저장
            if(registerHotelRequest.getTags() != null){
                for(int tag : registerHotelRequest.getTags()){
                    HotelInfoVo.Tags tags = new HotelInfoVo.Tags();
                    tags.setPk_num(hotelNum);
                    tags.setTag_num(tag);
                    tags.setInsert_user(userPk);
                    tags.setUpdate_user(userPk);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        log.info("호텔 등록 완료 hotel_num : " + hotelNum);
        result.setMessage("호텔 등록 완료");
        return result;
    }

    @Override
    public HotelSearchVo.TouristSpotInfoResponse TouristSpotInfo() {
        HotelSearchVo.TouristSpotInfoResponse result = new HotelSearchVo.TouristSpotInfoResponse();
        HotelSearchVo.TouristSpotInfo touristSpotInfo_a = new HotelSearchVo.TouristSpotInfo();
        HotelSearchVo.TouristSpotInfo touristSpotInfo_b = new HotelSearchVo.TouristSpotInfo();
        HotelSearchVo.TouristSpotInfo touristSpotInfo_c = new HotelSearchVo.TouristSpotInfo();

        touristSpotInfo_a.setTourist_spot_name("서울");
        touristSpotInfo_a.setImage("https://aws.bucket/");
        touristSpotInfo_a.setHotel_count(2500);

        touristSpotInfo_b.setTourist_spot_name("부산");
        touristSpotInfo_b.setImage("https://aws.bucket/");
        touristSpotInfo_b.setHotel_count(1500);

        touristSpotInfo_c.setTourist_spot_name("제주");
        touristSpotInfo_c.setImage("https://aws.bucket/");
        touristSpotInfo_c.setHotel_count(500);

        List<HotelSearchVo.TouristSpotInfo> touristSpotInfoList = new ArrayList<>();
        touristSpotInfoList.add(touristSpotInfo_a);
        touristSpotInfoList.add(touristSpotInfo_b);
        touristSpotInfoList.add(touristSpotInfo_c);

        result.setMessage("여행지 정보 조회 완료");
        result.setData(touristSpotInfoList);

        return result;
    }

    @Override
    public HotelSearchVo.SearchBarResponse SearchBar(HotelSearchVo.SearchBarRequest searchBarVoRequest) {
        // 검색어에 일치하는 지역명, 호텔명, 호텔주소 있는지 DB 조회

        /*
         검색어에 가장 가까운 호텔 정보 나타내주는거 가능할듯
         카카오 해당 검색어에 가장 가까운 검색어를 리턴해주는 api가 존재함 대신 공식 api는 아님 https://devtalk.kakao.com/t/api/108598
         검색어 정보 api로 검색어에 가장 가까운 장소 3개 정도 리턴. 비공식 api라 그런가 선정기준이 있진않아보임..
         */

        HotelSearchVo.SearchBarResponse result = new HotelSearchVo.SearchBarResponse();
        HotelSearchVo.HotelSearchData HotelSearchData = new HotelSearchVo.HotelSearchData();

        List<String> region_list = new ArrayList<>();
        region_list.add("서울");

        List<String> place_list = new ArrayList<>();
        place_list.add("서울역");
        place_list.add("서울대입구");

       List<String> hotel_address_list = new ArrayList<>();
        hotel_address_list.add("노보텔 엠비시티 서울 용산");

        List<String> hotel_name_list = new ArrayList<>();
        hotel_name_list.add("서울신라호텔");
        hotel_name_list.add("밀레니엄 힐튼 서울");

        HotelSearchData.setRegion_list(region_list);
        HotelSearchData.setHotel_address_list(hotel_address_list);
        HotelSearchData.setHotel_name_list(hotel_name_list);
        HotelSearchData.setPlace_list(place_list);

        result.setMessage("검색 완료");
        result.setData(HotelSearchData);

        return result;

    }

    @Override
    public HotelSearchVo.SearchListResponse SearchHotelList(HotelSearchVo.HotelSearchListRequest searchBarVoSearchBarRequest) {
        HotelSearchVo.SearchListResponse result = new HotelSearchVo.SearchListResponse();
        List<HotelSearchVo.HotelSearchList> hotelSearchList = new ArrayList<>();
        HotelSearchVo.HotelSearchList hotel_a = new HotelSearchVo.HotelSearchList();
        HotelSearchVo.HotelSearchList hotel_b = new HotelSearchVo.HotelSearchList();
        List<Integer> tags = new ArrayList<>();
        tags.add(1);
        tags.add(3);
        tags.add(5);

        hotel_a.setHotel_num(12345);
        hotel_a.setImage("https://aws.bucket/123");
        hotel_a.setName("신라스테이 서초점");
        hotel_a.setEng_name("Shilla Stay Seocho");
        hotel_a.setStar(5);
        hotel_a.setMinimum_price(330000);
        hotel_a.setTags(tags);

        hotel_b.setHotel_num(45678);
        hotel_b.setImage("https://aws.bucket/456");
        hotel_b.setName("힐튼");
        hotel_b.setEng_name("Hilton");
        hotel_b.setStar(5);
        hotel_b.setMinimum_price(230000);
        hotel_b.setTags(tags);

        hotelSearchList.add(hotel_a);
        hotelSearchList.add(hotel_b);

        result.setMessage("호텔 검색 리스트 조회 완료");
        result.setData(hotelSearchList);

        return result;
    }

    @Override
    public HotelSearchVo.SearchListResponse SearchHotelListFilter(HotelSearchVo.HotelSearchListFilterRequest hotelSearchListFilterRequest) {

        // 전달받은 호텔 번호 목록을 필터 조건에 따라 필터링

        HotelSearchVo.SearchListResponse result = new HotelSearchVo.SearchListResponse();
        List<HotelSearchVo.HotelSearchList> hotelSearchFilterList = new ArrayList<>();
        HotelSearchVo.HotelSearchList hotel_a = new HotelSearchVo.HotelSearchList();
        HotelSearchVo.HotelSearchList hotel_b = new HotelSearchVo.HotelSearchList();
        List<Integer> tags = new ArrayList<>();
        tags.add(1);
        tags.add(3);
        tags.add(5);

        hotel_a.setHotel_num(12345);
        hotel_a.setImage("https://aws.bucket/123");
        hotel_a.setName("신라스테이 서초점");
        hotel_a.setEng_name("Shilla Stay Seocho");
        hotel_a.setStar(5);
        hotel_a.setMinimum_price(330000);
        hotel_a.setTags(tags);

        hotel_b.setHotel_num(45678);
        hotel_b.setImage("https://aws.bucket/456");
        hotel_b.setName("밀레니엄 힐튼 서울");
        hotel_b.setEng_name("Hilton");
        hotel_b.setStar(5);
        hotel_b.setMinimum_price(230000);
        hotel_b.setTags(tags);

        hotelSearchFilterList.add(hotel_b);
        hotelSearchFilterList.add(hotel_a);

        result.setMessage("호텔 검색 필터 조회 완료");
        result.setData(hotelSearchFilterList);

        return result;
    }

    @Override
    public HotelInfoVo.HotelDetailInfoResponse HotelDetailInfo(HotelInfoVo.HotelDetailInfoRequest hotelDetailInfoRequest) {
        HotelInfoVo.HotelDetailInfoResponse result = new HotelInfoVo.HotelDetailInfoResponse();

        try{
            HotelInfoVo.HotelDetailInfo hotelDetailInfo = new HotelInfoVo.HotelDetailInfo();
            // 객실
            List<String> roomImageList = new ArrayList<>();
            roomImageList.add("https://aws.bucket/1");
            roomImageList.add("https://aws.bucket/2");
            List<Integer> roomTagList = new ArrayList<>();
            roomTagList.add(1);
            roomTagList.add(3);

            HotelInfoVo.RoomInfo room_a = new HotelInfoVo.RoomInfo();
            room_a.setRoom_num(12345);
            room_a.setName("스탠다드 트윈룸");
            room_a.setImage(roomImageList);
            room_a.setMinimum_people(2);
            room_a.setMaximum_people(3);
            room_a.setDouble_bed_count(1);
            room_a.setSingle_bed_count(1);
            room_a.setCheck_in_time("15:00");
            room_a.setCheck_out_time("11:00");
            room_a.setPrice(130000);
            room_a.setReservable_room_count(5);
            room_a.setAvailable_yn(true);
            room_a.setTags(roomTagList);


            HotelInfoVo.RoomInfo room_b = new HotelInfoVo.RoomInfo();
            room_b.setRoom_num(45678);
            room_b.setName("디럭스 더블");
            room_b.setImage(roomImageList);
            room_b.setMinimum_people(2);
            room_b.setMaximum_people(3);
            room_b.setDouble_bed_count(2);
            room_b.setSingle_bed_count(1);
            room_b.setCheck_in_time("15:00");
            room_b.setCheck_out_time("11:00");
            room_b.setPrice(330000);
            room_b.setReservable_room_count(2);
            room_b.setAvailable_yn(false);
            room_b.setTags(roomTagList);

            List<HotelInfoVo.RoomInfo> room_list = new ArrayList<>();
            room_list.add(room_a);
            room_list.add(room_b);

            //성수기
            HotelInfoVo.PeakSeason season_a = new HotelInfoVo.PeakSeason();
            season_a.setPeak_season_start(DateUtil.stringToDate("2022/07/01"));
            season_a.setPeak_season_end(DateUtil.stringToDate("2022/08/31"));

            HotelInfoVo.PeakSeason season_b = new HotelInfoVo.PeakSeason();
            season_b.setPeak_season_start(DateUtil.stringToDate("2022/11/01"));
            season_b.setPeak_season_end(DateUtil.stringToDate("2022/01/31"));

            // 호텔
            List<String> hotelImageList = new ArrayList<>();
            hotelImageList.add("https://aws.bucket/45");
            hotelImageList.add("https://aws.bucket/67");

            List<HotelInfoVo.PeakSeason> hotelPeakSeasonList = new ArrayList<>();
            hotelPeakSeasonList.add(season_a);
            hotelPeakSeasonList.add(season_b);

            List<Integer> hotelTagList = new ArrayList<>();
            hotelTagList.add(4);
            hotelTagList.add(5);

            hotelDetailInfo.setHotel_num(hotelDetailInfoRequest.getHotel_num());
            hotelDetailInfo.setName("신라스테이");
            hotelDetailInfo.setEng_name("Shilla Stay");
            hotelDetailInfo.setAddress("서울특별시 강남구");
            hotelDetailInfo.setPhone_num("0212345678");
            hotelDetailInfo.setStar(5);
            hotelDetailInfo.setImage(hotelImageList);
            hotelDetailInfo.setInfo("서울 강남에 위치한 본 호텔은...");
            hotelDetailInfo.setRule("라멘 서비스 및 대욕장 이용안내...");
            hotelDetailInfo.setPeak_season_list(hotelPeakSeasonList);
            hotelDetailInfo.setTags(hotelTagList);
            hotelDetailInfo.setRoom_list(room_list);

            result.setMessage("호텔 상세정보 조회 완료");
            result.setData(hotelDetailInfo);
        }catch (Exception e){

        }

        return result;
    }

    @Override
    public CommonResponseVo DeleteHotel(HotelInfoVo.DeleteHotelRequest deleteHotelRequest) {
        // 삭제 사유 DB 저장 후 소프트 삭제

        CommonResponseVo result = new CommonResponseVo();
        result.setMessage("호텔 삭제 완료");
        return result;
    }

    /**
     * 호텔 정보 수정
     * @param editHotelRequest
     * @return
     */
    @Override
    @Transactional
    public CommonResponseVo EditHotel(HotelInfoVo.EditInfoHotelRequest editHotelRequest) {
        CommonResponseVo result = new CommonResponseVo();
        int hotelNum = 0;
        log.info("호텔 정보 수정 시작");
        try{
            // registerHotelParamVo 호텔정보 + 성수기 + 호텔태그 + 이미지 정보 DB 저장
            String userRole = String.valueOf(CommonEnum.UserRole.OWNER.getCode());
            hotelNum = editHotelRequest.getHotel_num();

            // 사업자번호 - JWT 파싱 임시조치로 하드코딩
            // TODO JWT 완성되면 변경
            int business_user_num = 3;

            String userPk = userRole + business_user_num; // 유저 구분 코드 + pk

            // 호텔정보 수정 파라미터에 사업자번호, 위도, 경도, 변경자 정보 추가
            editHotelRequest.setBusiness_user_num(business_user_num);
            editHotelRequest.setPhone_num(aes256Util.encrypt(editHotelRequest.getPhone_num())); // 전화번호 암호화
            editHotelRequest.setLatitude(editHotelRequest.getLocation().get(0));
            editHotelRequest.setLongitude(editHotelRequest.getLocation().get(1));
            editHotelRequest.setUpdate_user(userPk);
            hotelMapper.updateHotelInfo(editHotelRequest);

            // Image Resizing & S3 Upload & DB insert
            if(editHotelRequest.getImage() != null){
                // 현재 DB에 저장된 호텔 이미지 정보 전부 Delete
                HotelDto.ImageTable deleteImageParam = new HotelDto.ImageTable();
                deleteImageParam.setPrimary_key(hotelNum);
                deleteImageParam.setSelect_type(CommonEnum.ImageType.HOTEL.getCode());
                hotelMapper.deleteHotelImage(deleteImageParam);

                // Image Resizing & S3 Upload & DB insert
                if(editHotelRequest.getImage() != null){
                    InsertImage(editHotelRequest.getImage(), CommonEnum.ImageType.HOTEL.getCode(), hotelNum,  userPk);
                }
            }

            if(editHotelRequest.getPeak_season_list() != null){
                // 현재 DB에 저장된 성수기정보 전부 soft Delete
                hotelMapper.softDeletePeekSeason(hotelNum);
                for(HotelInfoVo.PeakSeason peakSeason : editHotelRequest.getPeak_season_list()){
                    HotelDto.PeekSeasonTable peekSeasonTable = new HotelDto.PeekSeasonTable();
                    peekSeasonTable.setPeak_season_std(peakSeason.getPeak_season_start());
                    peekSeasonTable.setPeak_season_end(peakSeason.getPeak_season_end());
                    peekSeasonTable.setHotel_num(hotelNum);
                    peekSeasonTable.setInsert_user(userPk);
                    peekSeasonTable.setUpdate_user(userPk);
                    hotelMapper.insertPeekSeason(peekSeasonTable);
                }
            }

            if(editHotelRequest.getTags() != null){
                // 현재 DB에 저장된 호텔 태그 정보 전부 Delete
                hotelMapper.deleteHotelTags(hotelNum);

                // 호텔 태그 저장
                for(int tag : editHotelRequest.getTags()){
                    HotelInfoVo.Tags tags = new HotelInfoVo.Tags();
                    tags.setPk_num(hotelNum);
                    tags.setTag_num(tag);
                    tags.setInsert_user(userPk);
                    tags.setUpdate_user(userPk);
                    hotelMapper.insertHotelTags(tags);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        log.info("호텔 정보 수정 완료 hotel_num : "+hotelNum);
        result.setMessage("호텔 정보 수정 완료");

        return result;
    }

    @Override
    @Transactional
    public CommonResponseVo RegisterRoom(HotelInfoVo.RegisterRoomRequest registerRoomRequest) {
        CommonResponseVo result = new CommonResponseVo();
        int roomNum = 0;
        log.info("객실 등록 시작");
        try{
            // registerHotelParamVo 호텔정보 + 성수기 + 호텔태그 + 이미지 정보 DB 저장
            String userRole = String.valueOf(CommonEnum.UserRole.OWNER.getCode());
            roomNum = dbUtil.getAutoIncrementNext(CommonEnum.TableName.D_ROOM.getName());

            // 사업자번호 - JWT 파싱 임시조치로 하드코딩
            // TODO JWT 완성되면 변경
            int business_user_num = 3;
            String userPk = userRole + business_user_num; // 유저 구분 코드 + pk

            // 객실 정보 등록
            registerRoomRequest.setInsert_user(userPk);
            registerRoomRequest.setUpdate_user(userPk);
            hotelMapper.insertRoomInfo(registerRoomRequest);

            // 이미지 등록
            if(registerRoomRequest.getImage() != null){
                InsertImage(registerRoomRequest.getImage(), CommonEnum.ImageType.ROOM.getCode(), roomNum,  userPk);
            }

            // 객실 태그 등록
            if(registerRoomRequest.getTags() != null){
                for(int tag : registerRoomRequest.getTags()){
                    HotelInfoVo.Tags tags = new HotelInfoVo.Tags();
                    tags.setPk_num(roomNum);
                    tags.setTag_num(tag);
                    tags.setInsert_user(userPk);
                    tags.setUpdate_user(userPk);
                    hotelMapper.insertRoomTags(tags);
                }
            }

            // 객실 호실 등록
            if(registerRoomRequest.getRoom_detail_list() != null){
                for(HotelInfoVo.RegisterRoomDetailRequest roomDetail : registerRoomRequest.getRoom_detail_list()){
                    roomDetail.setRoom_num(roomNum);
                    // 만약 호실 사용금지 날짜가 존재하면 호실 상태값 = 예약불가 (2) 아니면 예약가능
                    if(roomDetail.getRoom_closed_start() != null){
                        roomDetail.setRoom_detail_status(CommonEnum.RoomDetailStatus.UNAVAILABLE.getCode());
                    }else {
                        roomDetail.setRoom_detail_status(CommonEnum.RoomDetailStatus.AVAILABLE.getCode());
                    }
                    roomDetail.setInsert_user(userPk);
                    roomDetail.setUpdate_user(userPk);
                    hotelMapper.insertRoomDetailInfo(roomDetail);
                }
            }

            // 공휴일 가격상태 업데이트 - 호텔 테이블에 컬럼존재
            hotelMapper.updateHolidayPriceStatus(registerRoomRequest);

        }catch (Exception e){
            e.printStackTrace();
            result.setResult("ERROR");
            result.setMessage("");
            return result;
        }

        log.info("객실 등록 완료 room_num : " + roomNum);
        result.setMessage("객실 등록 완료");
        return result;
    }

    @Override
    public HotelInfoVo.RoomInfoResponse RoomInfo(HotelInfoVo.RoomInfoRequest roomInfoRequest) {
        HotelInfoVo.RoomInfoResponse result = new HotelInfoVo.RoomInfoResponse();

        try {
            // 객실
            List<String> roomImageList = new ArrayList<>();
            roomImageList.add("https://aws.bucket/1");
            roomImageList.add("https://aws.bucket/2");
            List<Integer> roomTagList = new ArrayList<>();
            roomTagList.add(1);
            roomTagList.add(3);

            HotelInfoVo.RoomInfo room_a = new HotelInfoVo.RoomInfo();
            room_a.setRoom_num(12345);
            room_a.setName("스탠다드 트윈룸");
            room_a.setImage(roomImageList);
            room_a.setMinimum_people(2);
            room_a.setMaximum_people(3);
            room_a.setDouble_bed_count(1);
            room_a.setSingle_bed_count(1);
            room_a.setCheck_in_time("15:00");
            room_a.setCheck_out_time("11:00");
            room_a.setPrice(130000);
            room_a.setReservable_room_count(5);
            room_a.setAvailable_yn(true);
            room_a.setTags(roomTagList);

            // 호실
            HotelInfoVo.RoomDetailInfo room_detail_a = new HotelInfoVo.RoomDetailInfo();
            HotelInfoVo.RoomDetailInfo room_detail_b = new HotelInfoVo.RoomDetailInfo();
            List<HotelInfoVo.RoomDetailInfo> roomDetailInfoList = new ArrayList<>();
            room_detail_a.setRoom_detail_num(123);
            room_detail_a.setName("101호");
            room_detail_a.setStatus(0);

            room_detail_b.setRoom_detail_num(123);
            room_detail_b.setName("102호");
            room_detail_b.setStatus(1);
            room_detail_b.setRoom_closed_start(DateUtil.stringToDate("2022/08/01"));
            room_detail_b.setRoom_closed_end(DateUtil.stringToDate("2022/08/05"));
            room_detail_b.setDelete_date(DateUtil.stringToDate("2022/08/30"));

            roomDetailInfoList.add(room_detail_a);
            roomDetailInfoList.add(room_detail_b);
            room_a.setRoom_detail_info(roomDetailInfoList);

            result.setMessage("객실 정보 제공 완료");
            result.setData(room_a);

        }catch (Exception e){

        }

        return result;
    }

    @Override
    public CommonResponseVo EditRoom(HotelInfoVo.EditRoomRequest registerRoomRequest) {
        CommonResponseVo result = new CommonResponseVo();

        result.setMessage("객실 정보 수정 완료");
        return result;
    }

    @Override
    public HotelInfoVo.CheckDuplicateRoomNameResponse CheckDuplicateRoomName(HotelInfoVo.CheckDuplicateRoomNameRequest checkDuplicateRoomNameRequest) {
        HotelInfoVo.CheckDuplicateRoomNameResponse result = new HotelInfoVo.CheckDuplicateRoomNameResponse();
        Boolean CheckDuplicateResult = true;
        result.setMessage("객실명 중복 조회 완료");
        result.setData(CheckDuplicateResult);
        return result;
    }

    @Override
    public CommonResponseVo DeleteRoom(HotelInfoVo.DeleteRoomRequest deleteRoomRequest) {
        CommonResponseVo result = new CommonResponseVo();

        result.setMessage("객실 삭제처리 완료");
        return result;
    }

    @Override
    public HotelInfoVo.DeleteRoomInfoResponse DeleteRoomInfo(HotelInfoVo.DeleteRoomRequest deleteRoomRequest) {
        HotelInfoVo.DeleteRoomInfoResponse result = new HotelInfoVo.DeleteRoomInfoResponse();

        try{
            // 객실
            List<String> roomImageList = new ArrayList<>();
            roomImageList.add("https://aws.bucket/1");
            roomImageList.add("https://aws.bucket/2");
            List<Integer> roomTagList = new ArrayList<>();
            roomTagList.add(1);
            roomTagList.add(3);

            HotelInfoVo.DeleteRoomInfo room_a = new HotelInfoVo.DeleteRoomInfo();
            room_a.setRoom_num(12345);
            room_a.setName("스탠다드 트윈룸");
            room_a.setImage(roomImageList);
            room_a.setMinimum_people(2);
            room_a.setMaximum_people(3);
            room_a.setDouble_bed_count(1);
            room_a.setSingle_bed_count(1);
            room_a.setCheck_in_time("15:00");
            room_a.setCheck_out_time("11:00");
            room_a.setPrice(130000);
            room_a.setReservable_room_count(5);
            room_a.setAvailable_yn(true);
            room_a.setTags(roomTagList);
            room_a.setLast_reservation_date(DateUtil.stringToDate("2022/08/01"));

            result.setMessage("객실 삭제추가정보 조회 완료");
            result.setData(room_a);
        }catch (Exception e){

        }

        return result;
    }

    @Override
    public HotelInfoVo.RoomInfoListResponse RoomInfoList(HotelInfoVo.RoomInfoRequest roomInfoRequest) {
        HotelInfoVo.RoomInfoListResponse result = new HotelInfoVo.RoomInfoListResponse();

        try{
            // 객실
            List<String> roomImageList = new ArrayList<>();
            roomImageList.add("https://aws.bucket/1");
            roomImageList.add("https://aws.bucket/2");
            List<Integer> roomTagList = new ArrayList<>();
            roomTagList.add(1);
            roomTagList.add(3);

            HotelInfoVo.RoomInfo room_a = new HotelInfoVo.RoomInfo();
            room_a.setRoom_num(12345);
            room_a.setName("스탠다드 트윈룸");
            room_a.setImage(roomImageList);
            room_a.setMinimum_people(2);
            room_a.setMaximum_people(3);
            room_a.setDouble_bed_count(1);
            room_a.setSingle_bed_count(1);
            room_a.setCheck_in_time("15:00");
            room_a.setCheck_out_time("11:00");
            room_a.setPrice(130000);
            room_a.setReservable_room_count(5);
            room_a.setAvailable_yn(true);
            room_a.setTags(roomTagList);

            HotelInfoVo.RoomInfo room_b = new HotelInfoVo.RoomInfo();
            room_b.setRoom_num(45678);
            room_b.setName("디럭스 더블");
            room_b.setImage(roomImageList);
            room_b.setMinimum_people(2);
            room_b.setMaximum_people(3);
            room_b.setDouble_bed_count(2);
            room_b.setSingle_bed_count(1);
            room_b.setCheck_in_time("15:00");
            room_b.setCheck_out_time("11:00");
            room_b.setPrice(330000);
            room_b.setReservable_room_count(2);
            room_b.setAvailable_yn(false);
            room_b.setTags(roomTagList);

            // 호실
            HotelInfoVo.RoomDetailInfo room_detail_a = new HotelInfoVo.RoomDetailInfo();
            HotelInfoVo.RoomDetailInfo room_detail_b = new HotelInfoVo.RoomDetailInfo();
            List<HotelInfoVo.RoomDetailInfo> roomDetailInfoList = new ArrayList<>();
            room_detail_a.setRoom_detail_num(123);
            room_detail_a.setName("101호");
            room_detail_a.setStatus(0);

            room_detail_b.setRoom_detail_num(123);
            room_detail_b.setName("102호");
            room_detail_b.setStatus(1);
            room_detail_b.setRoom_closed_start(DateUtil.stringToDate("2022/08/01"));
            room_detail_b.setRoom_closed_end(DateUtil.stringToDate("2022/08/05"));
            room_detail_b.setDelete_date(DateUtil.stringToDate("2022/08/30"));

            roomDetailInfoList.add(room_detail_a);
            roomDetailInfoList.add(room_detail_b);
            room_a.setRoom_detail_info(roomDetailInfoList);

            List<HotelInfoVo.RoomInfo> room_list = new ArrayList<>();
            room_list.add(room_a);
            room_list.add(room_b);

            result.setMessage("객실 리스트 조회 완료");
            result.setData(room_list);
        }catch (Exception e){

        }

        return result;
    }

    @Override
    public CommonResponseVo AddRoomDetail(HotelInfoVo.AddRoomDetailRequest addRoomDetailRequest) {
        CommonResponseVo result = new CommonResponseVo();
        result.setMessage("호실 추가 완료");

        return result;
    }

    @Override
    public CommonResponseVo EditRoomDetail(HotelInfoVo.EditRoomDetailRequest editRoomDetailRequest) {
        CommonResponseVo result = new CommonResponseVo();
        result.setMessage("호실 정보 수정 완료");

        return result;
    }

    @Override
    public CommonResponseVo DeleteRoomDetail(HotelInfoVo.DeleteRoomDetailRequest deleteRoomDetailRequest) {
        CommonResponseVo result = new CommonResponseVo();
        result.setMessage("호실 삭제 완료");

        return result;
    }

    @Override
    public HotelInfoVo.DeleteRoomDetailInfoResponse DeleteRoomDetailInfo(HotelInfoVo.DeleteRoomDetailRequest deleteRoomDetailRequest) {
        HotelInfoVo.DeleteRoomDetailInfoResponse result = new HotelInfoVo.DeleteRoomDetailInfoResponse();

        try{
            HotelInfoVo.DeleteRoomDetailInfo deleteRoomDetailInfo = new HotelInfoVo.DeleteRoomDetailInfo();

            deleteRoomDetailInfo.setRoom_detail_num(123);
            deleteRoomDetailInfo.setName("101호");
            deleteRoomDetailInfo.setStatus(0);
            deleteRoomDetailInfo.setLast_reservation_date(DateUtil.stringToDate("2022/08/01"));

            result.setData(deleteRoomDetailInfo);
            result.setMessage("호실 삭제 추가정보 조회 완료");
        }catch (Exception e){

        }

        return result;
    }

    @Override
    public HotelInfoVo.HotelReservationListResponse HotelReservationList(HotelInfoVo.HotelReservationListRequest hotelReservationListRequest) {
        HotelInfoVo.HotelReservationListResponse result = new HotelInfoVo.HotelReservationListResponse();
        List<HotelInfoVo.HotelReservation> reservationsList = new ArrayList<>();

        try{
            HotelInfoVo.HotelReservation reservation_a = new HotelInfoVo.HotelReservation();
            HotelInfoVo.HotelReservation reservation_b = new HotelInfoVo.HotelReservation();


            reservation_a.setReservation_num(12345);
            reservation_a.setHotel_name("신라스테이");
            reservation_a.setRoom_name("스탠다드 트윈");
            reservation_a.setCustomer_name("홍길동");
            reservation_a.setCustomer_phone_num("01012345678");
            reservation_a.setReservation_date(DateUtil.stringToDate("2022/08/01"));
            reservation_a.setReservation_status(0);

            reservation_b.setReservation_num(45678);
            reservation_b.setHotel_name("신라스테이");
            reservation_b.setRoom_name("디럭스 더블");
            reservation_b.setCustomer_name("홍길동");
            reservation_b.setCustomer_phone_num("01012345678");
            reservation_b.setReservation_date(DateUtil.stringToDate("2022/08/20"));
            reservation_b.setReservation_status(1);

            reservationsList.add(reservation_a);
            reservationsList.add(reservation_b);

        }catch (Exception e){

        }
        result.setMessage("호텔 예약정보 조회 완료");
        result.setData(reservationsList);

        return result;
    }

    /**
     * 해당 리스트에 중복이 있으면 키값을 기준으로 중복제거
     * @return list
     */

    private static class DeduplicationUtils {

        public static <T> List<T> deduplication(final List<T> list, Function<? super T, ?> key) {
            final Set<Object> set = ConcurrentHashMap.newKeySet();

            return list.stream()
                    .filter(
                            predicate ->  set.add(key.apply(predicate))
                    )
                    .collect(Collectors.toList());
        }
    }

    /**
     * 이미지 AWS 업로드, 이미지 리사이징, 이미지 정보 DB 저장
     * @return
     */
    private void InsertImage(List<MultipartFile> multipartFile, int selectType, int PK, String userPk) throws Exception{

        List<String> imageUrlList = imageUtil.uploadImage(multipartFile);
        for(int i=0; i < imageUrlList.size(); i++){
            String imageName = imageUrlList.get(i);
            String bucket_url = imageUtil.getImageUrl(imageName);
            HotelDto.ImageTable imageTable = new HotelDto.ImageTable();
            imageTable.setSelect_type(selectType);
            imageTable.setPrimary_key(PK);
            imageTable.setPicture_name(imageName);
            imageTable.setBucket_url(bucket_url);
            imageTable.setPicture_sequence(i+1); // 사진순서 1부터 시작
            imageTable.setInsert_user(userPk);
            imageTable.setUpdate_user(userPk);
            hotelMapper.insertImage(imageTable);
        }

    }

}
