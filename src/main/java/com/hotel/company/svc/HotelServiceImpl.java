package com.hotel.company.svc;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.vo.CommonEnum;
import com.hotel.common.vo.JwtTokenDto;
import com.hotel.company.dto.HotelDto;
import com.hotel.company.dto.HotelMapper;
import com.hotel.jwt.JwtTokenProvider;
import com.hotel.util.*;
import com.hotel.company.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional(rollbackFor = {RuntimeException.class, Exception.class})
public class HotelServiceImpl implements HotelService {

    @Autowired
    ImageUtil imageUtil;

    @Autowired
    DBUtil dbUtil;

    @Autowired
    AES256Util aes256Util;

    @Autowired
    HotelMapper hotelMapper;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.secretKey}")
    String kakaoKey;

    @Value("${search.distance}")
    Integer allowedDistance;

    @Autowired
    KaKaoUtil kaKaoUtil;

    @Override
    public HotelInfoVo.OwnerHotelListResponse OwnerHotelList(HotelInfoVo.OwnerHotelListRequest ownerHotelListRequest, String jwtToken) {
        HotelInfoVo.OwnerHotelListResponse result = new HotelInfoVo.OwnerHotelListResponse();
        int total_cnt = 0;

        try{
            log.info("사업자 소유 호텔리스트 조회 시작");
            List<HotelInfoVo.HotelDetailInfo> ownerHotelList = new ArrayList<>();
            int business_user_num = getPk(jwtToken);
            Integer page = ownerHotelListRequest.getPage();
            Integer pageCnt = ownerHotelListRequest.getPage_cnt();

            // 해당 사업자 소유 호텔번호 조회
            List<Integer> ownerHotelNumList = hotelMapper.selectOwnerHotelList(business_user_num);

            Pattern pattern = Pattern.compile(".*"+ownerHotelListRequest.getText()+".*");

            for(int hotel_num : ownerHotelNumList){
                HotelInfoVo.HotelDetailInfoResponse hotelDetailInfoResponse = getHotelInfo(hotel_num);
                HotelInfoVo.HotelDetailInfo hotelDetailInfo = hotelDetailInfoResponse.getData();

                if(hotelDetailInfoResponse.getData() == null){
                    continue;
                }

                // 만약 검색어가 존재하면 해당 사업자 소유 호텔들 중 검색어에 해당하는 호텔만 리턴
                if(ownerHotelListRequest.getText() != null && !"".equals(ownerHotelListRequest.getText())){
                    if(! (pattern.matcher(hotelDetailInfo.getName()).matches() || pattern.matcher(hotelDetailInfo.getEng_name()).matches())){
                        continue;
                    }
                }
                ownerHotelList.add(hotelDetailInfo);
            }

            total_cnt = ownerHotelList.size();

            // pagination
            ownerHotelList = PageUtil.paginationList(page, pageCnt, total_cnt, ownerHotelList);

            result.setMessage("사업자 소유 호텔 리스트 조회 완료");
            result.setTotal_cnt(total_cnt);
            result.setData(ownerHotelList);

        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    public HotelInfoVo.OwnerHotelNameListResponse OwnerHotelNameList(String jwtToken) {
        HotelInfoVo.OwnerHotelNameListResponse result = new HotelInfoVo.OwnerHotelNameListResponse();

        try{
            log.info("사업자 소유 호텔명 간략 리스트 조회 시작");
            List<HotelInfoVo.HotelDetailInfo> ownerHotelList = new ArrayList<>();
            int business_user_num = getPk(jwtToken);

            // 해당 사업자 소유 호텔번호, 호텔명 조회
            List<HotelInfoVo.OwnerHotelName> ownerHotelNameList = hotelMapper.selectOwnerHotelNameList(business_user_num);

            if(CollectionUtils.isEmpty(ownerHotelNameList)){
                ownerHotelNameList = new ArrayList<>();
            }

            result.setMessage("사업자 소유 호텔명 리스트 조회 완료");
            result.setData(ownerHotelNameList);

        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    public CommonResponseVo RegisterHotel(HotelInfoVo.RegisterHotelRequest registerHotelRequest, String jwtToken) {
        CommonResponseVo result = new CommonResponseVo();
        int hotelNum = 0;

        try{
            // registerHotelParamVo 호텔정보 + 성수기 + 호텔태그 + 이미지 정보 DB 저장
            log.info("호텔 등록 시작");

            String userRole = String.valueOf(CommonEnum.UserRole.OWNER.getCode());
            hotelNum = dbUtil.getAutoIncrementNext(CommonEnum.TableName.D_HOTEL.getName());

            // 사업자번호
            int business_user_num = getPk(jwtToken);

            String userPk = userRole + business_user_num; // 유저 구분 코드 + pk

            // 호텔등록 파라미터에서 사업자번호, 위도, 경도, 공휴일 가격상태 생성자, 변경자 정보 추가
            registerHotelRequest.setBusiness_user_num(business_user_num);
            registerHotelRequest.setPhone_num(aes256Util.encrypt(registerHotelRequest.getPhone_num())); // 전화번호 암호화
            registerHotelRequest.setLongitude(registerHotelRequest.getLocation().get(0));
            registerHotelRequest.setLatitude(registerHotelRequest.getLocation().get(1));
            registerHotelRequest.setHoliday_price_status(1); // 기본셋팅 - 비성수기 주말가격 객실수정에서 변경가능
            registerHotelRequest.setInsert_user(userPk);
            registerHotelRequest.setUpdate_user(userPk);
            hotelMapper.insertHotelInfo(registerHotelRequest);

            // Image Resizing & S3 Upload & DB insert
            if(!CollectionUtils.isEmpty(registerHotelRequest.getImage())){
                insertImage(registerHotelRequest.getImage(), CommonEnum.ImageType.HOTEL.getCode(), hotelNum,  userPk);
            }

            // 성수기정보 저장
            if(!CollectionUtils.isEmpty(registerHotelRequest.getPeak_season_list())){
                for(HotelInfoVo.PeakSeason peakSeason : registerHotelRequest.getPeak_season_list()) {
                    HotelDto.PeekSeasonTable peekSeasonTable = new HotelDto.PeekSeasonTable();
                    peekSeasonTable.setPeak_season_std(peakSeason.getPeak_season_start());
                    peekSeasonTable.setPeak_season_end(peakSeason.getPeak_season_end());
                    peekSeasonTable.setHotel_num(hotelNum);
                    peekSeasonTable.setInsert_user(userPk);
                    peekSeasonTable.setUpdate_user(userPk);
                    hotelMapper.insertPeakSeason(peekSeasonTable);
                }
            }

            // 호텔 태그 저장
            if(!CollectionUtils.isEmpty(registerHotelRequest.getTags())){
                for(int tag : registerHotelRequest.getTags()){
                    HotelInfoVo.Tags tags = new HotelInfoVo.Tags();
                    tags.setPk_num(hotelNum);
                    tags.setTag_num(tag);
                    tags.setInsert_user(userPk);
                    tags.setUpdate_user(userPk);
                    hotelMapper.insertHotelTags(tags);
                }
            }
            log.info("호텔 등록 완료 hotel_num : " + hotelNum);
            result.setMessage("호텔 등록 완료");
        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    public HotelSearchVo.TouristSpotInfoResponse TouristSpotInfo(HotelSearchVo.TouristSpotInfoRequest touristSpotInfoRequest) {
        HotelSearchVo.TouristSpotInfoResponse result = new HotelSearchVo.TouristSpotInfoResponse();
        int total_cnt = 0;

        try{
            log.info("여행지 정보 조회 시작");
            Integer page = touristSpotInfoRequest.getPage();
            Integer pageCnt = touristSpotInfoRequest.getPage_cnt();

            List<HotelSearchVo.TouristSpotInfo> touristSpotList = hotelMapper.selectTouristSpotList();

            if(!CollectionUtils.isEmpty(touristSpotList)){
                total_cnt = touristSpotList.size();
                //pagination
                touristSpotList = PageUtil.paginationList(page, pageCnt, total_cnt, touristSpotList);
            }

            result.setMessage("여행지 정보 조회 완료");
            result.setData(touristSpotList);
            result.setTotal_cnt(total_cnt);

        }catch (Exception e){
            e.printStackTrace();

        }

        return result;
    }

    @Override
    public HotelSearchVo.SearchBarResponse SearchBar(HotelSearchVo.SearchBarRequest searchBarVoRequest) {
        HotelSearchVo.SearchBarResponse result = new HotelSearchVo.SearchBarResponse();
        HotelSearchVo.HotelSearchData HotelSearchData = new HotelSearchVo.HotelSearchData();
        String text = searchBarVoRequest.getText();
        String dbLikeSearchText = "%"+searchBarVoRequest.getText()+"%";
        Set<String> hotel_address_list = new HashSet<>();
        Set<String> hotel_name_list = new HashSet<>();
        Set<String> region_list = new HashSet<>();
        Set<String> place_list = new HashSet<>();

        try{
            log.info("검색바 조회 시작");
            hotel_name_list = hotelMapper.selectHotelSearchBarName(dbLikeSearchText);
            hotel_address_list = hotelMapper.selectHotelSearchBarAddress(dbLikeSearchText);
            region_list = hotelMapper.selectHotelSearchBarRegionCode(dbLikeSearchText);

            // 카카오 api호출로 장소 리스트 받아옴 3개까지만
            URL url = new URL("https://dapi.kakao.com/v2/local/search/keyword.json?size=3&query="+URLEncoder.encode(text, StandardCharsets.UTF_8));

            // GET 전송
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-type", "application/json");
            con.setRequestProperty("Authorization", kakaoKey);
            con.setDoOutput(true);
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            StringBuilder sb = new StringBuilder();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                // 받아온 response 데이터 json 형식으로 파싱 -> 필요한 결과값 추출
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(sb.toString());
                JSONObject jsonObj = (JSONObject) obj;
                JSONArray jsonArrayData = (JSONArray) jsonObj.get("documents");

                for(int i = 0; i< jsonArrayData.size(); i++){
                    place_list.add((String) ((JSONObject) jsonArrayData.get(i)).get("place_name"));
                }

            } else {
                log.info("카카오 API 연동에 실패하였습니다");
                result.setMessage("KAKAO-0001");
                result.setResult("ERROR");
                return result;
            }

            HotelSearchData.setHotel_name_list(hotel_name_list);
            HotelSearchData.setHotel_address_list(hotel_address_list);
            HotelSearchData.setRegion_list(region_list);
            HotelSearchData.setPlace_list(place_list);

            result.setMessage("검색 완료");
            result.setData(HotelSearchData);
        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }
        return result;
    }

    @Override
    public HotelSearchVo.SearchListResponse SearchHotelList(HotelSearchVo.HotelSearchListRequest searchBarVoSearchBarRequest) {
        HotelSearchVo.SearchListResponse result = new HotelSearchVo.SearchListResponse();
        List<HotelSearchVo.HotelSearchInfo> hotelSearchList = new ArrayList<>();

        try{
            log.info("검색하기 - 사용자가 요청한 검색어, 조건에 일치하는 호텔조회 시작");
            String text = searchBarVoSearchBarRequest.getText();
            String dbText = searchBarVoSearchBarRequest.getText();
            int page = searchBarVoSearchBarRequest.getPage();
            int pageCnt = searchBarVoSearchBarRequest.getPage_cnt();

            List<Integer> hotel_num_list = new ArrayList<>();

            //Search_type = 4  카카오 api를 통한 별도처리필요
            if(searchBarVoSearchBarRequest.getSearch_type() != 4){
                hotel_num_list = hotelMapper.selectSearchList(searchBarVoSearchBarRequest);
            }else {
                try{
                    // 카카오 키워드 검색 API호출 가장 위에있는 검색결과의 address_name의 첫번째 소절 (서울, 경기등 시,도정보임) 위도,경도정보 추출
                    JSONArray jsonArrayData = kaKaoUtil.getKeywordMapData(text);
                    if(!CollectionUtils.isEmpty(jsonArrayData)){
                        JSONObject jsonObject = (JSONObject) jsonArrayData.get(0);
                        String kakaoRegion = (String) jsonObject.get("address_name");
                        kakaoRegion = kakaoRegion.substring(0, 2);
                        double kakaoLongitude = Double.parseDouble((String) jsonObject.get("x"));
                        double kakaoLatitude = Double.parseDouble((String) jsonObject.get("y"));

                        // DB에서 address_name의 첫번째 소절에 해당하는 region_code 의 호텔만 추출
                        List<HotelInfoVo.HotelDetailInfo> hotelList = hotelMapper.selectHotelByRegionCode(kakaoRegion);
                        // 해당 호텔들의 위도 경도와 가장 위에있는 검색결과의 위도, 경도 비교해서 5km이내의 호텔만 추출
                        for(HotelInfoVo.HotelDetailInfo hotelDetailInfo : hotelList){
                            if(DistanceUtil.getDistance(
                                    kakaoLatitude,
                                    kakaoLongitude,
                                    hotelDetailInfo.getLatitude(),
                                    hotelDetailInfo.getLongitude())
                                    <= allowedDistance){
                                hotel_num_list.add(hotelDetailInfo.getHotel_num());
                            }
                        }
                    }else {
                        // 검색된 장소없음
                        log.info("카카오 API 키워드검색에 검색된 장소가 존재하지 않습니다");
                    }
                }catch (Exception e){
                    log.info("카카오 API 호출 에러");
                    throw new Exception();
                }
            }
            int total_cnt = 0;
            int roomDetailTotalCount = 0;

            for(int hotel_num : hotel_num_list){
                boolean available_yn = true;
                boolean isPeopleCountAvailable = false;
                int isAllRoomNotAvailable = 0;
                List<Integer> roomDetailAvailableList = new ArrayList<>();
                List<Integer> priceList = new ArrayList<>();
                List<Integer> room_detail_num_list = new ArrayList<>();
                HotelSearchVo.HotelSearchInfo hotelSearchInfo = new HotelSearchVo.HotelSearchInfo();
                HotelInfoVo.HotelDetailInfoResponse hotelDetailInfoResponse = getHotelInfo(hotel_num);
                HotelInfoVo.HotelDetailInfo hotelDetailInfo = hotelDetailInfoResponse.getData();
                List<HotelInfoVo.RoomInfo> roomInfoList = hotelDetailInfo.getRoom_list();
                for(HotelInfoVo.RoomInfo roomInfo : roomInfoList){
                    int roomDetailAvailableCnt = 0; // 사용가능한 호실 수 체크

                    // 투숙인원 조건이 있으면 투숙인원 체크. 조건에 해당되는 방이 한개라도 있으면 true. 최대인원수 조건에 맞는지만 체크
                    if(searchBarVoSearchBarRequest.getPeople_count() != null){
                        if(searchBarVoSearchBarRequest.getPeople_count() <= roomInfo.getMaximum_people()){
                            isPeopleCountAvailable = true;
                        }
                    }
                    List<HotelInfoVo.RoomDetailInfo> roomDetailInfoList = roomInfo.getRoom_detail_info();
                    for(HotelInfoVo.RoomDetailInfo roomDetailInfo : roomDetailInfoList){
                        // 사용가능한 호실수 체크
                        if(roomDetailInfo.getAvailable_yn()){
                            roomDetailAvailableCnt += 1;
                        }
                    }

                    if(roomDetailAvailableCnt == 0){ // 모든 호실이 사용불가인 객실 카운트
                        isAllRoomNotAvailable += 1;
                    }

                    // 객실 가격 수집
                    priceList.add(roomInfo.getPrice());

                    // 사용가능한 호실 갯수 수집
                    roomDetailAvailableList.add(roomDetailAvailableCnt);
                }

                // 해당 호텔의 모든 객실이 사용불가능 이라면 available_yn = false
                if(roomInfoList.size() == isAllRoomNotAvailable){
                    available_yn = false;
                }

                // 예약범위 & 투숙인원 데이터가 있으면 체크해서 넣어줌

                // 투숙인원조건이 있는데 이에 해당하는 객실이 없으면 해당호텔 패스
                if(searchBarVoSearchBarRequest.getPeople_count() != null){
                    if(!isPeopleCountAvailable){
                        continue;
                    }
                }

                // 예약범위 조건이 있는데 이에 해당하는 객실이 없으면 해당호텔 패스
                if(searchBarVoSearchBarRequest.getReservation_start_date() != null &&
                        searchBarVoSearchBarRequest.getReservation_end_date() != null){
                    HotelDto.SelectHotelReservationListForSearch param = new HotelDto.SelectHotelReservationListForSearch();
                    param.setHotel_num(hotel_num);
                    param.setReservation_start_date(searchBarVoSearchBarRequest.getReservation_start_date());
                    param.setReservation_start_date(searchBarVoSearchBarRequest.getReservation_end_date());
                    List<Integer> hotelReservationAvailableList =
                            hotelMapper.selectHotelReservationListForSearch(param);
                    if(hotelReservationAvailableList == null){
                        continue;
                    }
                }

                // 호실 수 조건이 존재하는데 모든 객실의 사용가능한 호실이 요청한 호실수보다 적으면 해당 호텔 패스
                if(searchBarVoSearchBarRequest.getRoom_count() != null){
                    boolean isRoomCountAvailable = false;
                    for(int availableRoomDetailCnt : roomDetailAvailableList){
                        if(searchBarVoSearchBarRequest.getRoom_count() <= availableRoomDetailCnt){
                            isRoomCountAvailable = true;
                            break;
                        }
                    }
                    if(!isRoomCountAvailable){
                        continue;
                    }
                }

                hotelSearchInfo.setHotel_num(hotel_num);
                hotelSearchInfo.setName(hotelDetailInfo.getName());
                hotelSearchInfo.setEng_name(hotelDetailInfo.getEng_name());
                hotelSearchInfo.setStar(hotelDetailInfo.getStar());
                hotelSearchInfo.setTags(hotelDetailInfo.getTags());
                if(!CollectionUtils.isEmpty(hotelDetailInfo.getImage())){
                    hotelSearchInfo.setImage(hotelDetailInfo.getImage().get(0)); // 메인이미지만 제공
                }
                hotelSearchInfo.setAvailable_yn(available_yn);
                if(priceList.size() <= 0){ // 객실이 존재하지않으면
                    hotelSearchInfo.setMinimum_price(0);
                }else {
                    hotelSearchInfo.setMinimum_price(Collections.min(priceList)); // 객실들 가격중 최저가
                }
                hotelSearchInfo.setLocation(hotelDetailInfo.getLocation()); // 경도, 위도 정보
                hotelSearchList.add(hotelSearchInfo);
            }

            total_cnt = hotelSearchList.size();

            // 필터조건에 걸러진 호텔 전체리스트
            List<Integer> result_hotel_num_list = hotelSearchList
                    .stream()
                    .map(HotelSearchVo.HotelSearchInfo::getHotel_num)
                    .toList();

            // 호텔 등급순으로 역순 정렬
            hotelSearchList.sort(Comparator.comparing(HotelSearchVo.HotelSearchInfo::getStar).reversed());

            // pagination
            hotelSearchList = PageUtil.paginationList(page, pageCnt, total_cnt, hotelSearchList);

            result.setData(hotelSearchList);
            result.setTotal_cnt(total_cnt);
            result.setMessage("호텔 검색 리스트 조회 완료");
            result.setHotel_num_list(result_hotel_num_list); // 필터조건에 걸러진 호텔 전체리스트 추가

        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    public HotelSearchVo.SearchListResponse SearchHotelListFilter(HotelSearchVo.HotelSearchListFilterRequest hotelSearchListFilterRequest) {

        HotelSearchVo.SearchListResponse result = new HotelSearchVo.SearchListResponse();
        List<HotelSearchVo.HotelSearchInfo> hotelSearchList = new ArrayList<>();
        int total_cnt = 0;
        log.info("검색한 호텔 리스트 필터 조회 시작");

        try{

            // 페이지
            int page = hotelSearchListFilterRequest.getPage();
            int pageCnt = hotelSearchListFilterRequest.getPage_cnt();

            // 정렬조건
            int rank_num = hotelSearchListFilterRequest.getRank_num();

            // 해당 호텔 정보 조회
            List<Integer> hotel_num_list = hotelSearchListFilterRequest.getHotel_num();


            // 필터조건
            List<Double> location = hotelSearchListFilterRequest.getLocation();
            List<Integer> room_tags = hotelSearchListFilterRequest.getRoom_tags();
            List<Integer> hotel_tags = hotelSearchListFilterRequest.getHotel_tags();
            Integer minimum_price = hotelSearchListFilterRequest.getMinimum_price(); // null 체크를 위해 Integer사용
            Integer maximum_price = hotelSearchListFilterRequest.getMaximum_price();
            Integer star = hotelSearchListFilterRequest.getStar();

            /*
             * 필터조건 여러개 존재할때 우선순위
             * 1. 위도,경도 (위치)
             * 2. 성급
             * 3. 호텔 태그
             * 4. 객실 태그
             * 5. 가격
             */

            for(int hotel_num : hotel_num_list){
                boolean available_yn = true;
                int isAllRoomNotAvailable = 0;
                List<Integer> priceList = new ArrayList<>();
                List<Integer> room_detail_num_list = new ArrayList<>();
                List<List<Integer>> roomTagList = new ArrayList<>();
                HotelSearchVo.HotelSearchInfo hotelSearchInfo = new HotelSearchVo.HotelSearchInfo();
                HotelInfoVo.HotelDetailInfoResponse hotelDetailInfoResponse = getHotelInfo(hotel_num);
                HotelInfoVo.HotelDetailInfo hotelDetailInfo = hotelDetailInfoResponse.getData();
                List<HotelInfoVo.RoomInfo> roomInfoList = hotelDetailInfo.getRoom_list();

                for(HotelInfoVo.RoomInfo roomInfo : roomInfoList){

                    boolean isRoomDetailAvailable = false; // 호실 전부 사용불가인지 체크용

                    List<HotelInfoVo.RoomDetailInfo> roomDetailInfoList = roomInfo.getRoom_detail_info();
                    for(HotelInfoVo.RoomDetailInfo roomDetailInfo : roomDetailInfoList){
                        // 사용가능한 호실이 1개라도 있으면 isRoomDetailAvailable = ture
                        if(roomDetailInfo.getAvailable_yn()){
                            isRoomDetailAvailable = true;
                        }
                    }

                    if(!isRoomDetailAvailable){ // 모든 호실이 사용불가인 객실 카운트
                        isAllRoomNotAvailable += 1;
                    }

                    // 객실 가격 수집
                    if(roomInfo.getPrice() != null){
                        priceList.add(roomInfo.getPrice());
                    }

                    // 객실 태그정보 수집
                    if(roomInfo.getTags() != null){
                        roomTagList.add(roomInfo.getTags());
                    }
                }

                // 필터조건
                // 위도, 경도. 해당 위도경도에 반경 5km 이내여야 검색됨
                if(location != null){
                    if(DistanceUtil.getDistance(
                            location.get(1),
                            location.get(0),
                            hotelDetailInfo.getLatitude(),
                            hotelDetailInfo.getLongitude())
                            > allowedDistance){
                        continue; // 5km 넘으면 패스
                    }
                }

                // 성급
                if(star != null){
                    if(!star.equals(hotelDetailInfo.getStar())){
                        continue;
                    }
                }
                // 호텔 태그 조건 있고 호텔 태그도 존재하면 호텔태그 필터 체크
                if(!CollectionUtils.isEmpty(hotel_tags)){
                    if(!CollectionUtils.isEmpty(hotelDetailInfo.getTags())){
                        // 호텔 태그가 요청 태그조건을 모두 만족하면 그대로 진행, 아니면 패스
                        if(!hotelDetailInfo.getTags().containsAll(hotel_tags)){
                            continue;
                        }
                    }else {
                        continue;
                    } // 호텔 태그가 null 이면 필터조건에 맞지 않는걸로 간주

                }

                // 객실 태그 필터 조건 체크
                if(!CollectionUtils.isEmpty(room_tags)){
                    if(!CollectionUtils.isEmpty(roomTagList)){
                        // 객실중 해당 객실 필터 조건에 해당되는 객실이 1개라도 있으면 true
                        roomTagList = roomTagList
                                .stream()
                                .filter(roomTags -> roomTags.containsAll(room_tags))
                                .collect(Collectors.toList());
                        if(roomTagList.size() <= 0){
                            continue;
                        }
                    }else { // 해당 호탤 객실들에 태그가 전혀 없으면 필터조건에 맞지않는것으로 간주
                        continue;
                    }
                }

                // 가격 필터조건 체크
                if(minimum_price != null && maximum_price != null && priceList.size() > 0){
                    priceList = priceList
                            .stream()
                            .filter(price-> (minimum_price <= price) && (price <= maximum_price))
                            .collect(Collectors.toList());
                    if(priceList.size() <= 0){
                        continue;
                    }
                }

                // 해당 호텔의 모든 객실이 사용불가능 이라면 available_yn = false
                if(roomInfoList.size() == isAllRoomNotAvailable){
                    available_yn = false;
                }

                hotelSearchInfo.setHotel_num(hotel_num);
                hotelSearchInfo.setName(hotelDetailInfo.getName());
                hotelSearchInfo.setEng_name(hotelDetailInfo.getEng_name());
                hotelSearchInfo.setStar(hotelDetailInfo.getStar());
                hotelSearchInfo.setTags(hotelDetailInfo.getTags());
                if(!CollectionUtils.isEmpty(hotelDetailInfo.getImage())){
                    hotelSearchInfo.setImage(hotelDetailInfo.getImage().get(0)); // 메인이미지만 제공
                }
                hotelSearchInfo.setAvailable_yn(available_yn);
                if(priceList.size() <= 0){ // 객실이 존재하지않으면 최소가 0원
                    hotelSearchInfo.setMinimum_price(0);
                }else {
                    hotelSearchInfo.setMinimum_price(Collections.min(priceList)); // 객실들 가격중 최저가
                }
                hotelSearchInfo.setLocation(hotelDetailInfo.getLocation()); // 경도, 위도 정보

                hotelSearchList.add(hotelSearchInfo);
            }

            total_cnt = hotelSearchList.size();

            // pagination
            hotelSearchList = PageUtil.paginationList(page, pageCnt, total_cnt, hotelSearchList);

            // 정렬
            if(rank_num == 1){
                // 호텔 등급 높은순
                hotelSearchList.sort(Comparator.comparing(HotelSearchVo.HotelSearchInfo::getStar).reversed());
            }else if(rank_num == 2){
                // 최소가 높은순
                hotelSearchList.sort(Comparator.comparing(HotelSearchVo.HotelSearchInfo::getMinimum_price).reversed());
            }else if(rank_num == 3){
                // 최소가 낮은순
                hotelSearchList.sort(Comparator.comparing(HotelSearchVo.HotelSearchInfo::getMinimum_price));
            }
            result.setTotal_cnt(total_cnt);
            result.setMessage("호텔 검색 필터 조회 완료");
            result.setData(hotelSearchList);

        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    /**
     * 호텔 상세정보 조회 - 호텔정보, 객실정보, 호실정보
     * @param hotelNum
     * @return
     */
    @Override
    public HotelInfoVo.HotelDetailInfoResponse HotelDetailInfo(int hotelNum) {
        HotelInfoVo.HotelDetailInfoResponse result = new HotelInfoVo.HotelDetailInfoResponse();
        log.info("호텔 상세정보 조회 시작");
        try{

            // 호텔정보 조회
            HotelInfoVo.HotelDetailInfo hotelDetailInfo = hotelMapper.selectHotelInfo(hotelNum);

            // 조회된 호텔정보 없으면 빈값 리턴
            if(hotelDetailInfo == null){
                result.setMessage("호텔 상세정보 조회 완료");
                result.setData(null);
                return result;
            }

            // 위도 경도값 / 호텔 전화번호 복호화
            List<Double> location = new ArrayList<>();
            location.add(hotelDetailInfo.getLongitude());
            location.add(hotelDetailInfo.getLatitude());
            hotelDetailInfo.setLocation(location);
            hotelDetailInfo.setPhone_num(aes256Util.decrypt(hotelDetailInfo.getPhone_num()));

            // 호텔 이미지 조회
            List<String> hotelImageList = selectImage(CommonEnum.ImageType.HOTEL.getCode() ,hotelNum);
            if (!CollectionUtils.isEmpty(hotelImageList)){
                hotelDetailInfo.setImage(hotelImageList);
            }

            // 성수기 정보 조회
            List<HotelInfoVo.PeakSeason> peakSeasonList = hotelMapper.selectPeakSeasonList(hotelNum);
            if(!CollectionUtils.isEmpty(peakSeasonList)){
                hotelDetailInfo.setPeak_season_list(peakSeasonList);
            }

            // 호텔 태그 정보 조회
            List<Integer> hotelTagList = hotelMapper.selectHotelTags(hotelNum);
            if(!CollectionUtils.isEmpty(hotelTagList)){
                hotelDetailInfo.setTags(hotelTagList);
            }

            // 객실정보
            List<HotelInfoVo.RoomInfo> roomInfoList = hotelMapper.selectRoomInfoList(hotelNum);
            List<HotelInfoVo.RoomInfo> RoomInfoListData = new ArrayList<>();
            for(HotelInfoVo.RoomInfo roomInfo : roomInfoList){
                addRoomInfoData(roomInfo); // 객실, 호실정보등 필요한 데이터 추가
                RoomInfoListData.add(roomInfo);
            }

            hotelDetailInfo.setRoom_list(RoomInfoListData);
            result.setData(hotelDetailInfo);
            result.setMessage("호텔 상세정보 조회 완료");
        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    public CommonResponseVo DeleteHotel(HotelInfoVo.DeleteHotelRequest deleteHotelRequest, String jwtToken) {
        CommonResponseVo result = new CommonResponseVo();
        log.info("호텔 삭제 시작");

        try{
            int hotel_num = deleteHotelRequest.getHotel_num();
            // 호텔 상세 정보 조회
            HotelInfoVo.HotelDetailInfoResponse HotelDetailInfo = getHotelInfo(hotel_num);
            if(HotelDetailInfo.getData() == null){
                log.info("호텔 삭제 - 조회된 호텔 없음");
                result.setResult("ERROR");
                result.setMessage("DEL-0001");
                return result;
            }

            int business_user_num = getPk(jwtToken);
            String insert_user = Integer.toString(CommonEnum.UserRole.OWNER.getCode())+business_user_num;
            deleteHotelRequest.setInsert_user(insert_user); // insert_user 정보저장
            // 호텔 삭제 사유 DB 저장 후 소프트 삭제
            hotelMapper.insertHotelDeleteReason(deleteHotelRequest);
            hotelMapper.deleteHotel(hotel_num);

            List<HotelInfoVo.RoomInfo> roomInfoList = HotelDetailInfo.getData().getRoom_list();

            List<Integer> room_num_list = new ArrayList<>();
            List<Integer> room_detail_num_list = new ArrayList<>();

            // 객실과 호실 pk저장 & 삭제테이블에 저장
            if(!CollectionUtils.isEmpty(roomInfoList)){
                for(HotelInfoVo.RoomInfo roomInfo : roomInfoList){
                    HotelInfoVo.DeleteTable roomDeleteTable = new HotelInfoVo.DeleteTable();
                    roomDeleteTable.setPk(roomInfo.getRoom_num());
                    roomDeleteTable.setInsert_user(insert_user);
                    hotelMapper.insertRoomDelete(roomDeleteTable);

                    List<HotelInfoVo.RoomDetailInfo> roomDetailInfoList = roomInfo.getRoom_detail_info();
                    for(HotelInfoVo.RoomDetailInfo roomDetailInfo : roomDetailInfoList){
                        HotelInfoVo.DeleteTable roomDetailDeleteTable = new HotelInfoVo.DeleteTable();
                        roomDetailDeleteTable.setPk(roomDetailInfo.getRoom_detail_num());
                        roomDetailDeleteTable.setInsert_user(insert_user);
                        hotelMapper.insertRoomDetailDelete(roomDetailDeleteTable);
                        room_detail_num_list.add(roomDetailInfo.getRoom_detail_num());
                    }
                    room_num_list.add(roomInfo.getRoom_num());
                }
            }

            // 해당 호텔 호실의 예약건 전부 취소 & 삭제처리
            if(room_detail_num_list.size() > 0){
                hotelMapper.deleteRoomDetail(room_detail_num_list);
                hotelMapper.deleteHotelCancelReservation(room_detail_num_list);
            }

            // 호텔의 객실, 호실도 소프트 삭제처리
            if(room_num_list.size() > 0){
                hotelMapper.deleteRoom(room_num_list);
            }

            // 호텔 태그 삭제
            hotelMapper.deleteHotelTags(hotel_num);

            result.setMessage("호텔 삭제 완료");
        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    /**
     * 호텔 정보 수정
     * @param editHotelRequest
     * @param jwtToken
     * @return
     */
    @Override
    public CommonResponseVo EditHotel(HotelInfoVo.EditInfoHotelRequest editHotelRequest, String jwtToken) {
        CommonResponseVo result = new CommonResponseVo();
        int hotelNum = 0;
        log.info("호텔 정보 수정 시작");
        try{
            // 호텔정보 + 성수기 + 호텔태그 + 이미지 정보 DB 저장
            String userRole = String.valueOf(CommonEnum.UserRole.OWNER.getCode());
            hotelNum = editHotelRequest.getHotel_num();

            int business_user_num = getPk(jwtToken);

            String userPk = userRole + business_user_num; // 유저 구분 코드 + pk

            // 호텔정보 수정 파라미터에 사업자번호, 위도, 경도, 변경자 정보 추가
            editHotelRequest.setBusiness_user_num(business_user_num);
            editHotelRequest.setPhone_num(aes256Util.encrypt(editHotelRequest.getPhone_num())); // 전화번호 암호화
            editHotelRequest.setLongitude(editHotelRequest.getLocation().get(0));
            editHotelRequest.setLatitude(editHotelRequest.getLocation().get(1));
            editHotelRequest.setUpdate_user(userPk);
            hotelMapper.updateHotelInfo(editHotelRequest);

            // 태그, 이미지, 성수기가 null 이거나 배열 사이즈가 0 이면 DB, AWS 에 저장된 데이터 삭제처리만 진행됨

            // 이미지 정보 저장
            // 현재 DB에 저장된 호텔 이미지 정보 전부 Delete & AWS에 저장된 이미지 삭제
            deleteImage(CommonEnum.ImageType.HOTEL.getCode(), hotelNum);
            if(!CollectionUtils.isEmpty(editHotelRequest.getImage())){
                // Image Resizing & S3 Upload & DB insert
                insertImage(editHotelRequest.getImage(), CommonEnum.ImageType.HOTEL.getCode(), hotelNum,  userPk);
            }

            // 성수기정보 저장
            // 현재 DB에 저장된 성수기정보 전부 soft Delete
            hotelMapper.softDeletePeakSeason(hotelNum);
            if(!CollectionUtils.isEmpty(editHotelRequest.getPeak_season_list())){
                for(HotelInfoVo.PeakSeason peakSeason : editHotelRequest.getPeak_season_list()){
                    HotelDto.PeekSeasonTable peekSeasonTable = new HotelDto.PeekSeasonTable();
                    peekSeasonTable.setPeak_season_std(peakSeason.getPeak_season_start());
                    peekSeasonTable.setPeak_season_end(peakSeason.getPeak_season_end());
                    peekSeasonTable.setHotel_num(hotelNum);
                    peekSeasonTable.setInsert_user(userPk);
                    peekSeasonTable.setUpdate_user(userPk);
                    hotelMapper.insertPeakSeason(peekSeasonTable);
                }
            }

            // 호텔 태그 저장
            // 현재 DB에 저장된 호텔 태그 정보 전부 Delete
            hotelMapper.deleteHotelTags(hotelNum);
            if(!CollectionUtils.isEmpty(editHotelRequest.getTags())){
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
            ErrorResult(result);
            return result;
        }

        log.info("호텔 정보 수정 완료 hotel_num : "+hotelNum);
        result.setMessage("호텔 정보 수정 완료");

        return result;
    }

    @Override
    public CommonResponseVo RegisterRoom(HotelInfoVo.RegisterRoomRequest registerRoomRequest, String jwtToken) {
        CommonResponseVo result = new CommonResponseVo();
        int roomNum = 0;
        log.info("객실 등록 시작");
        try{
            // registerHotelParamVo 호텔정보 + 성수기 + 호텔태그 + 이미지 정보 DB 저장
            String userRole = String.valueOf(CommonEnum.UserRole.OWNER.getCode());
            roomNum = dbUtil.getAutoIncrementNext(CommonEnum.TableName.D_ROOM.getName());

            int business_user_num = getPk(jwtToken);
            String userPk = userRole + business_user_num; // 유저 구분 코드 + pk

            // 객실 정보 등록
            registerRoomRequest.setInsert_user(userPk);
            registerRoomRequest.setUpdate_user(userPk);
            hotelMapper.insertRoomInfo(registerRoomRequest);

            // 이미지 등록
            if(!CollectionUtils.isEmpty(registerRoomRequest.getImage())){
                insertImage(registerRoomRequest.getImage(), CommonEnum.ImageType.ROOM.getCode(), roomNum,  userPk);
            }

            // 객실 태그 등록
            if(!CollectionUtils.isEmpty(registerRoomRequest.getTags())){
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
            if(!CollectionUtils.isEmpty(registerRoomRequest.getRoom_detail_list())){
                for(HotelInfoVo.RegisterRoomDetailForRoom roomDetail : registerRoomRequest.getRoom_detail_list()){
                    roomDetail.setRoom_num(roomNum);
                    // 만약 호실 사용금지 날짜가 존재하면 호실 상태값 = 예약불가 (2) 아니면 예약가능
                    if(roomDetail.getRoom_closed_start() != null){
                        roomDetail.setRoom_detail_status(CommonEnum.RoomDetailStatus.UNAVAILABLE.getCode());
                    }else {
                        roomDetail.setRoom_detail_status(CommonEnum.RoomDetailStatus.AVAILABLE.getCode());
                    }
                    roomDetail.setInsert_user(userPk);
                    roomDetail.setUpdate_user(userPk);
                    hotelMapper.insertRoomDetailInfoForRoom(roomDetail);
                }
            }

            // 공휴일 가격상태 업데이트 - 호텔 테이블에 컬럼존재
            hotelMapper.updateHolidayPriceStatus(registerRoomRequest);

        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        log.info("객실 등록 완료 room_num : " + roomNum);
        result.setMessage("객실 등록 완료");
        return result;
    }

    @Override
    public HotelInfoVo.RoomInfoResponse RoomInfo(int room_num) {
        log.info("객실 상세정보 조회 시작");
        HotelInfoVo.RoomInfoResponse result = new HotelInfoVo.RoomInfoResponse();

        try {
            HotelInfoVo.RoomInfo roomInfo = hotelMapper.selectRoomInfo(room_num);

            if(roomInfo == null){
                result.setMessage("객실 상세 정보 조회 완료");
                result.setData(null);
                return result;
            }

            // 호실정보등 필요한 정보 추가
            addRoomInfoData(roomInfo);

            result.setMessage("객실 상세 정보 조회 완료");
            result.setData(roomInfo);

        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    public CommonResponseVo EditRoom(HotelInfoVo.EditRoomRequest editRoomRequest, String jwtToken) {
        CommonResponseVo result = new CommonResponseVo();
        log.info("객실 수정 시작");

        try{
            String userRole = String.valueOf(CommonEnum.UserRole.OWNER.getCode());
            int roomNum = editRoomRequest.getRoom_num();
            int hotelNum = editRoomRequest.getHotel_num();

            int business_user_num = getPk(jwtToken);
            String userPk = userRole + business_user_num; // 유저 구분 코드 + pk

            // 객실 정보 등록
            editRoomRequest.setUpdate_user(userPk);
            hotelMapper.updateRoomInfo(editRoomRequest);

            // 태그, 이미지가 null 이거나 배열 사이즈가 0 이면 DB, AWS 에 저장된 데이터 삭제처리만 진행됨

            // 이미지 업데이트
            // 현재 DB에 저장된 객실 이미지 정보 전부 Delete & AWS에 저장된 이미지 삭제
            deleteImage(CommonEnum.ImageType.ROOM.getCode(), roomNum);
            if(!CollectionUtils.isEmpty(editRoomRequest.getImage())){
                // Image Resizing & S3 Upload & DB insert
                insertImage(editRoomRequest.getImage(), CommonEnum.ImageType.ROOM.getCode(), roomNum,  userPk);
            }

            // 객실 태그 업데이트
            // 현재 DB에 저장된 객실 태그 정보 전부 Delete
            hotelMapper.deleteRoomTags(roomNum);
            if(!CollectionUtils.isEmpty(editRoomRequest.getTags())){
                for(int tag : editRoomRequest.getTags()){
                    HotelInfoVo.Tags tags = new HotelInfoVo.Tags();
                    tags.setPk_num(roomNum);
                    tags.setTag_num(tag);
                    tags.setInsert_user(userPk);
                    tags.setUpdate_user(userPk);
                    hotelMapper.insertRoomTags(tags);
                }
            }

            // 공휴일 가격상태 업데이트 - 호텔 테이블에 컬럼존재
            HotelInfoVo.RegisterRoomRequest registerRoomRequest = new HotelInfoVo.RegisterRoomRequest();
            registerRoomRequest.setHotel_num(hotelNum);
            registerRoomRequest.setHoliday_price_status(editRoomRequest.getHoliday_price_status());
            registerRoomRequest.setUpdate_user(userPk);
            hotelMapper.updateHolidayPriceStatus(registerRoomRequest);

            result.setMessage("객실 정보 수정 완료");

        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }
        return result;
    }

    @Override
    public HotelInfoVo.CheckDuplicateRoomNameResponse CheckDuplicateRoomName(HotelInfoVo.CheckDuplicateRoomNameRequest checkDuplicateRoomNameRequest) {
        HotelInfoVo.CheckDuplicateRoomNameResponse result = new HotelInfoVo.CheckDuplicateRoomNameResponse();
        boolean CheckDuplicateResult = false;
        log.info("객실명 중복조회 시작");
        try{
            String selectDuplicateRoomName = hotelMapper.selectDuplicateRoomName(checkDuplicateRoomNameRequest);

            if(selectDuplicateRoomName != null){
                CheckDuplicateResult = true;
            }

            result.setMessage("객실명 중복 조회 완료");
            result.setData(CheckDuplicateResult);
        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    public CommonResponseVo DeleteRoom(HotelInfoVo.DeleteRoomRequest deleteRoomRequest, String jwtToken) {
        CommonResponseVo result = new CommonResponseVo();
        log.info("객실 삭제 시작");
        try{
            int business_user_num = getPk(jwtToken);
            String insert_user = Integer.toString(CommonEnum.UserRole.OWNER.getCode())+business_user_num;
            int room_num = deleteRoomRequest.getRoom_num();

            // 객실 정보 조회
            HotelInfoVo.RoomInfoResponse roomInfoResponse = RoomInfo(room_num);
            HotelInfoVo.RoomInfo roomInfo = roomInfoResponse.getData();

            if(roomInfo == null){
                result.setResult("ERROR");
                result.setMessage("DEL-0001");
                return result;
            }

            List<Integer> room_num_list = new ArrayList<>();
            room_num_list.add(room_num);

            List<Integer> room_detail_num_list = roomInfo.getRoom_detail_info()
                    .stream()
                    .map(HotelInfoVo.RoomDetailInfo::getRoom_detail_num)
                    .collect(Collectors.toList());

            // 호실 정보가 존재하지 않을경우 예외처리 : 호실번호에 0추가 (pk 0인 호실은 존재하지않음)
            if(CollectionUtils.isEmpty(room_detail_num_list)){
                room_detail_num_list.add(0);
            }

            // 해당 호실들의 마지막 예약날짜 조회
            Date lastReservationDate = hotelMapper.selectLastReservationDate(room_detail_num_list);

            // 마지막 예약일 없거나 오늘날짜면 삭제테이블 저장 하고 즉시 삭제
            if(lastReservationDate == null || lastReservationDate.equals(DateUtil.DateToDayFormat(new Date()))){
                lastReservationDate = DateUtil.DateToDayFormat(new Date());

                // 객실 & 호실 삭제테이블 저장
                HotelInfoVo.DeleteTable insertRoomDelete = new HotelInfoVo.DeleteTable();
                insertRoomDelete.setPk(room_num);
                insertRoomDelete.setInsert_user(insert_user);
                hotelMapper.insertRoomDelete(insertRoomDelete);

                for(int room_detail_num : room_detail_num_list){
                    HotelInfoVo.DeleteTable insertRoomDetailDelete = new HotelInfoVo.DeleteTable();
                    insertRoomDetailDelete.setPk(room_detail_num);
                    insertRoomDetailDelete.setInsert_user(insert_user);
                    hotelMapper.insertRoomDetailDelete(insertRoomDetailDelete);
                }

                // 객실 & 호실 삭제
                hotelMapper.deleteRoom(room_num_list);
                hotelMapper.deleteRoomDetail(room_detail_num_list);

                // 객실 태그 삭제
                hotelMapper.deleteRoomTags(room_num);

            }

            HotelInfoVo.UpdateDeleteDateRequest deleteDateRoom = new HotelInfoVo.UpdateDeleteDateRequest();
            deleteDateRoom.setDelete_date(DateUtil.plusSomeDay(lastReservationDate, 1)); // 삭제일은 마지막 예약날짜 + 1일
            deleteDateRoom.setPk(room_num_list);

            HotelInfoVo.UpdateDeleteDateRequest deleteDateRoomDetail = new HotelInfoVo.UpdateDeleteDateRequest();
            deleteDateRoomDetail.setDelete_date(DateUtil.plusSomeDay(lastReservationDate, 1)); // 삭제일은 마지막 예약날짜 + 1일
            deleteDateRoomDetail.setPk(room_detail_num_list);

            // 객실 & 호실 삭제 날짜 지정
            hotelMapper.updateRoomDeleteDate(deleteDateRoom);
            hotelMapper.updateRoomDetailDeleteDate(deleteDateRoomDetail);

            result.setMessage("객실 삭제처리 완료");
        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
//            throw new RuntimeException("DB 롤백용 Exception. 핸들러로 처리필요");
        }
        return result;
    }

    @Override
    public HotelInfoVo.DeleteRoomInfoResponse DeleteRoomInfo(HotelInfoVo.DeleteRoomRequest deleteRoomRequest, String jwtToken) {
        HotelInfoVo.DeleteRoomInfoResponse result = new HotelInfoVo.DeleteRoomInfoResponse();
        log.info("객실 삭제추가정보 조회 시작");
        try{
            // 해당 객실정보 조회
            HotelInfoVo.RoomInfoResponse roomInfoResponse = RoomInfo(deleteRoomRequest.getRoom_num());

            if(roomInfoResponse.getData() == null){
                log.info("해당 객실정보 없음");
                result.setMessage("객실 삭제추가정보 조회 완료");
                result.setData(null);
                return result;
            }

            HotelInfoVo.RoomInfo roomInfo = roomInfoResponse.getData();
            List<Integer> room_detail_num_list = roomInfo.getRoom_detail_info()
                    .stream()
                    .map(HotelInfoVo.RoomDetailInfo::getRoom_detail_num)
                    .collect(Collectors.toList());

            // 해당 호실들의 마지막 예약날짜 조회
            Date lastReservationDate = hotelMapper.selectLastReservationDate(room_detail_num_list);

            // 최종 예약날짜 정보 넣어줌
            roomInfo.setLast_reservation_date(lastReservationDate);

            result.setData(roomInfo);
            result.setMessage("객실 삭제추가정보 조회 완료");
        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
//            throw new RuntimeException("DB 롤백용");
        }

        return result;
    }

    @Override
    public HotelInfoVo.RoomInfoListResponse RoomInfoList(HotelInfoVo.RoomInfoListRequest roomInfoRequest) {
        HotelInfoVo.RoomInfoListResponse result = new HotelInfoVo.RoomInfoListResponse();
        List<HotelInfoVo.RoomInfo> RoomInfoListData = new ArrayList<>();
        int total_cnt = 0;
        log.info("객실 리스트 조회 시작");
        try{
            List<HotelInfoVo.RoomInfo> roomInfoList = hotelMapper.selectRoomInfoList(roomInfoRequest.getHotel_num());
            Integer page = roomInfoRequest.getPage();
            Integer pageCnt = roomInfoRequest.getPage_cnt();
            for(HotelInfoVo.RoomInfo roomInfo : roomInfoList){
                addRoomInfoData(roomInfo); // 객실, 호실정보등 필요한 데이터 추가
                RoomInfoListData.add(roomInfo);
            }

            total_cnt = RoomInfoListData.size();

            //pagination
            RoomInfoListData = PageUtil.paginationList(page, pageCnt, total_cnt, RoomInfoListData);

            result.setData(RoomInfoListData);
            result.setTotal_cnt(total_cnt);
            result.setMessage("객실 리스트 조회 완료");
        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    public CommonResponseVo AddRoomDetail(HotelInfoVo.RegisterRoomDetailRequest registerRoomDetailRequest, String jwtToken) {
        CommonResponseVo result = new CommonResponseVo();
        log.info("호실 추가 시작");
        try{
            int business_user_num = getPk(jwtToken);
            String insert_user = Integer.toString(CommonEnum.UserRole.OWNER.getCode())+Integer.toString(business_user_num);
            registerRoomDetailRequest.setInsert_user(insert_user);
            registerRoomDetailRequest.setUpdate_user(insert_user);

            // 호실 등록시 사용금지 요청이 있으면 객실상태 예약불가로 설정
            if(registerRoomDetailRequest.getRoom_closed_start() != null || registerRoomDetailRequest.getRoom_closed_end() != null){
                registerRoomDetailRequest.setRoom_detail_status(CommonEnum.RoomDetailStatus.UNAVAILABLE.getCode());
            }else {
                registerRoomDetailRequest.setRoom_detail_status(CommonEnum.RoomDetailStatus.AVAILABLE.getCode());
            }

            hotelMapper.insertRoomDetailInfo(registerRoomDetailRequest);

            result.setMessage("호실 추가 완료");

        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    public CommonResponseVo DisableSettingRoomDetail(HotelInfoVo.DisableSettingRoomDetailRequest disableSettingRoomDetailRequest, String jwtToken) {
        CommonResponseVo result = new CommonResponseVo();
        log.info("호실 이용불가 처리 시작");

        try{
            int room_detail_num = disableSettingRoomDetailRequest.getRoom_detail_num();

            // 호실 예약기간 조회
            List<HotelDto.RoomDetailReservationDate> roomDetailReservationDateList = hotelMapper.selectRoomDetailReservationDate(room_detail_num);

            // 호실의 예약기간과 요청한 이용불가 요청기간이 겹치는지 체크
            if(roomDetailReservationDateList !=null){
                for(HotelDto.RoomDetailReservationDate roomDetailReservationDate : roomDetailReservationDateList){
                    boolean isDuplication = DateUtil.checkDatePeriodsDuplication(
                            roomDetailReservationDate.getSt_date(),
                            roomDetailReservationDate.getEd_date(),
                            disableSettingRoomDetailRequest.getRoom_closed_start(),
                            disableSettingRoomDetailRequest.getRoom_closed_end()
                    );
                    if(isDuplication){
                        result.setResult("ERROR");
                        result.setMessage("DUP-0003");
                        return result;
                    }
                }
            }else {
                // 호실존재하지않으면 결과없음 리턴
                noSearchResult(result);
                return result;
            }

            // 호실 이용불가 처리
            hotelMapper.updateDisableRoomDetail(disableSettingRoomDetailRequest);

            result.setMessage("호실 이용불가 처리 완료");

        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }
        return result;
    }

    @Override
    public CommonResponseVo DeleteRoomDetail(HotelInfoVo.DeleteRoomDetailRequest deleteRoomDetailRequest, String jwtToken) {
        CommonResponseVo result = new CommonResponseVo();
        log.info("호실 삭제 시작");

        try {
            int business_user_num = getPk(jwtToken);
            String insert_user = Integer.toString(CommonEnum.UserRole.OWNER.getCode())+business_user_num;

            int room_detail_num = deleteRoomDetailRequest.getRoom_detail_num();
            //호실존재여부 파악
            HotelInfoVo.RoomDetailInfo roomDetailInfo = hotelMapper.selectRoomDetailByDetailNum(room_detail_num);
            if(roomDetailInfo == null){
                noSearchResult(result);
                return result;
            }

            List<Integer> room_detail_num_list = new ArrayList<>();
            room_detail_num_list.add(room_detail_num);

            // 해당 호실들의 마지막 예약날짜 조회
            Date lastReservationDate = hotelMapper.selectLastReservationDate(room_detail_num_list);

            // 마지막 예약일 없거나 오늘날짜면 삭제테이블 저장 하고 즉시 삭제
            if(lastReservationDate == null || lastReservationDate.equals(DateUtil.DateToDayFormat(new Date()))){
                lastReservationDate = DateUtil.DateToDayFormat(new Date());

                // 호실 삭제테이블 저장
                HotelInfoVo.DeleteTable insertRoomDetailDelete = new HotelInfoVo.DeleteTable();
                insertRoomDetailDelete.setPk(room_detail_num);
                insertRoomDetailDelete.setInsert_user(insert_user);
                hotelMapper.insertRoomDetailDelete(insertRoomDetailDelete);

                // 호실 삭제
                hotelMapper.deleteRoomDetail(room_detail_num_list);
            }

            HotelInfoVo.UpdateDeleteDateRequest deleteDateRoomDetail = new HotelInfoVo.UpdateDeleteDateRequest();
            deleteDateRoomDetail.setDelete_date(DateUtil.plusSomeDay(lastReservationDate, 1)); // 삭제일은 마지막 예약날짜 + 1일
            deleteDateRoomDetail.setPk(room_detail_num_list);

            // 호실 삭제 날짜 지정
            hotelMapper.updateRoomDetailDeleteDate(deleteDateRoomDetail);

            result.setMessage("호실 삭제 완료");
        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    public HotelInfoVo.DeleteRoomDetailInfoResponse DeleteRoomDetailInfo(HotelInfoVo.DeleteRoomDetailRequest deleteRoomDetailRequest, String jwtToken) {
        HotelInfoVo.DeleteRoomDetailInfoResponse result = new HotelInfoVo.DeleteRoomDetailInfoResponse();
        log.info("호실 삭제 추가정보 조회 시작");

        try{
            // 해당 호실정보 조회
            int room_detail_num = deleteRoomDetailRequest.getRoom_detail_num();
            List<Integer> room_detail_num_list = new ArrayList<>();
            room_detail_num_list.add(room_detail_num);
            HotelInfoVo.RoomDetailInfo roomDetailInfo = hotelMapper.selectRoomDetailByDetailNum(room_detail_num);

            if(roomDetailInfo == null){
                result.setData(null);
                result.setMessage("호실 삭제 추가정보 조회 완료");
                return result;
            }

            // 해당 호실의 마지막 예약날짜 조회
            Date lastReservationDate = hotelMapper.selectLastReservationDate(room_detail_num_list);

            // 호실 사용 불가여부
            if(roomDetailInfo.getRoom_closed_start() != null && roomDetailInfo.getRoom_closed_end() != null){
                if(DateUtil.checkDateBetween(roomDetailInfo.getRoom_closed_start(), roomDetailInfo.getRoom_closed_end(), new Date())){
                    // 오늘날짜가 사용금지일에 해당하면 Available_yn = false
                    roomDetailInfo.setAvailable_yn(false);
                }
            }
            roomDetailInfo.setAvailable_yn(true);

            // 최종 예약날짜 정보 넣어줌
            roomDetailInfo.setLast_reservation_date(lastReservationDate);

            result.setData(roomDetailInfo);
            result.setMessage("호실 삭제 추가정보 조회 완료");
        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    @Override
    public HotelInfoVo.HotelReservationListResponse HotelReservationList(HotelInfoVo.HotelReservationListRequest hotelReservationListRequest, String jwtToken) {
        HotelInfoVo.HotelReservationListResponse result = new HotelInfoVo.HotelReservationListResponse();
        log.info("호텔 예약정보 조회 시작");

        try{
//            HotelInfoVo.HotelReservationInfo hotelReservationInfo = new HotelInfoVo.HotelReservationInfo();
            // 해당 호텔 조회
            int hotel_num = hotelReservationListRequest.getHotel_num();
            HotelInfoVo.HotelDetailInfoResponse hotelDetailInfoResponse = getHotelInfo(hotel_num);
            int total_cnt = 0;
            int page = hotelReservationListRequest.getPage();
            int pageCnt = hotelReservationListRequest.getPage_cnt();

            if(hotelDetailInfoResponse.getData() == null){
                result.setMessage("호텔 예약정보 조회 완료");
                result.setData(new ArrayList<>());
                result.setTotal_cnt(total_cnt);
                return result;
            }
            // rank_num = default 1
            if(hotelReservationListRequest.getRank_num() == null){
                hotelReservationListRequest.setRank_num(1);
            }

//            HotelInfoVo.HotelDetailInfo hotelDetailInfo = hotelDetailInfoResponse.getData();
//            List<HotelInfoVo.RoomInfo> roomInfoList = hotelDetailInfo.getRoom_list();
//            List<Integer> room_detail_num_list = new ArrayList<>();
//
//            // 호실 번호 수집
//            for(HotelInfoVo.RoomInfo roomInfo : roomInfoList){
//                for(HotelInfoVo.RoomDetailInfo roomDetailInfo : roomInfo.getRoom_detail_info()){
//                    room_detail_num_list.add(roomDetailInfo.getRoom_detail_num());
//                }
//            }

            // 해당 호텔의 호실정보 조회. 이미 삭제된 호실도 조회해서 예약정보 있으면 정보 리턴
            List<Integer> room_detail_num_list = hotelMapper.selectRoomDetailNumForAll(hotel_num);

            // 해당 호텔의 예약이 없을경우 예외처리
            if(CollectionUtils.isEmpty(room_detail_num_list)){
                result.setMessage("호텔 예약정보 조회 완료");
                result.setData(new ArrayList<>());
                result.setTotal_cnt(total_cnt);
                return result;
            }

//            // MySql offset이 0부터 시작이므로 page값에 -1
//            hotelReservationListRequest.setPage(hotelReservationListRequest.getPage()-1);

           //휴대폰번호 요청 존재하면 휴대폰번호 암호화해서 비교
            if(hotelReservationListRequest.getCustomer_phone_num() != null){
                hotelReservationListRequest.setCustomer_phone_num(aes256Util.encrypt(hotelReservationListRequest.getCustomer_phone_num()));
            }

            hotelReservationListRequest.setRoom_detail_num_list(room_detail_num_list);
            List<HotelInfoVo.HotelReservationDetailInfo> hotelReservationList = hotelMapper.selectHotelReservationList(hotelReservationListRequest);
//            total_cnt = hotelMapper.selectHotelReservationListCount(hotelReservationListRequest);

            for(HotelInfoVo.HotelReservationDetailInfo hotelReservationDetailInfo : hotelReservationList){
                hotelReservationDetailInfo.setCustomer_phone_num(aes256Util.decrypt(hotelReservationDetailInfo.getCustomer_phone_num())); //휴대폰 복호화
                hotelReservationDetailInfo.setReservation_date(
                        DateUtil.dateToString(hotelReservationDetailInfo.getSt_date())
                        + " ~ "
                        + DateUtil.dateToString(hotelReservationDetailInfo.getEd_date())); // 예약일 넣어줌 yyyy/MM/dd ~ yyyy/MM/dd
            }

            //pagination
            if(!CollectionUtils.isEmpty(hotelReservationList)){
                total_cnt = hotelReservationList.size();
                hotelReservationList = PageUtil.paginationList(page, pageCnt, total_cnt, hotelReservationList);
            }

//            hotelReservationInfo.setHotel_reservation_info_list(hotelReservationList);
//            hotelReservationInfo.setTotal_cnt(total_cnt);

            result.setMessage("호텔 예약정보 조회 완료");
            result.setData(hotelReservationList);
            result.setTotal_cnt(total_cnt);
        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    /**
     * 사업자가 소유한 모든 호텔 예약정보 제공 - 사업자 예약관리페이지 처음 진입시 필요
     *
     * @param ownerReservationListRequest
     * @param jwtToken
     * @return
     */
    @Override
    public HotelInfoVo.HotelReservationListResponse OwnerReservationList(HotelInfoVo.OwnerReservationListRequest ownerReservationListRequest, String jwtToken) {
        HotelInfoVo.HotelReservationListResponse result = new HotelInfoVo.HotelReservationListResponse();
        List<HotelInfoVo.HotelReservationDetailInfo> ownerHotelReservationList = new ArrayList<>();
        log.info("사업자가 소유한 모든 호텔 예약정보 조회 시작");
        int total_cnt = 0;

        try{
            int business_user_num = getPk(jwtToken);
            int page = ownerReservationListRequest.getPage();
            int pageCnt = ownerReservationListRequest.getPage_cnt();

            // 해당 사업자 소유 호텔번호 조회
            List<Integer> ownerHotelNumList = hotelMapper.selectOwnerHotelList(business_user_num);

            if(CollectionUtils.isEmpty(ownerHotelNumList)){
                result.setMessage("호텔 예약정보 조회 완료");
                result.setData(new ArrayList<>());
                result.setTotal_cnt(total_cnt);
                return result;
            }

            for(Integer hotel_num : ownerHotelNumList){
                // 해당 호텔의 호실정보 조회. 이미 삭제된 호실도 조회해서 예약정보 있으면 정보 리턴
                List<Integer> room_detail_num_list = hotelMapper.selectRoomDetailNumForAll(hotel_num);

                // 예약정보 없을경우 예외처리
                if(CollectionUtils.isEmpty(room_detail_num_list)){
                    continue;
                }

                // sql 재활용을 위한..
                HotelInfoVo.HotelReservationListRequest hotelReservationListRequest = new HotelInfoVo.HotelReservationListRequest();
                hotelReservationListRequest.setRoom_detail_num_list(room_detail_num_list);
                hotelReservationListRequest.setRank_num(1);

                List<HotelInfoVo.HotelReservationDetailInfo> hotelReservationList = hotelMapper.selectHotelReservationList(hotelReservationListRequest);

                for(HotelInfoVo.HotelReservationDetailInfo hotelReservationDetailInfo : hotelReservationList){
                    hotelReservationDetailInfo.setCustomer_phone_num(aes256Util.decrypt(hotelReservationDetailInfo.getCustomer_phone_num())); //휴대폰 복호화
                    hotelReservationDetailInfo.setReservation_date(
                            DateUtil.dateToString(hotelReservationDetailInfo.getSt_date())
                                    + " ~ "
                                    + DateUtil.dateToString(hotelReservationDetailInfo.getEd_date())); // 예약일 넣어줌 yyyy/MM/dd ~ yyyy/MM/dd
                    ownerHotelReservationList.add(hotelReservationDetailInfo);
                }
            }

            //pagination
            if(!CollectionUtils.isEmpty(ownerHotelReservationList)){
                total_cnt = ownerHotelReservationList.size();
                ownerHotelReservationList = PageUtil.paginationList(page, pageCnt, total_cnt, ownerHotelReservationList);
            }

            result.setMessage("사업자가 소유한 모든 호텔 예약정보 조회 완료");
            result.setData(ownerHotelReservationList);
            result.setTotal_cnt(total_cnt);
        }catch (Exception e){
            e.printStackTrace();
            ErrorResult(result);
            return result;
        }

        return result;
    }

    /**
     * 해당 리스트에 중복이 있으면 키값을 기준으로 중복제거
     * @return list
     */

    private static class deduplicationUtils {

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
    private void insertImage(List<MultipartFile> multipartFile, int selectType, int PK, String userPk) throws Exception{

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

    /**
     * 이미지 정보 조회
     * @param selectType
     * @param PK
     * @return
     */
    private List<String> selectImage(int selectType, int PK) throws Exception{
        List<String> result;
        HotelInfoVo.ImageInfo imageParams = new HotelInfoVo.ImageInfo();
        imageParams.setSelect_type(selectType);
        imageParams.setPrimary_key(PK);
        result = hotelMapper.selectImageList(imageParams);

        return result;
    }

    /**
     * AWS에 저장된 이미지 삭제 & 이미지 테이블 정보 삭제
     * @param selectType
     * @param PK
     * @throws Exception
     */
    private void deleteImage(int selectType, int PK) throws Exception{
        List<String> imageList = selectImage(selectType, PK);
        imageUtil.deleteImage(imageList);

        HotelDto.ImageTable deleteImageParam = new HotelDto.ImageTable();
        deleteImageParam.setPrimary_key(PK);
        deleteImageParam.setSelect_type(selectType);
        hotelMapper.deleteImage(deleteImageParam);
    }

    /**
     * 성수기, 주말 등 가격상태 판별 여부 판별
     * @param hotelNum - 호텔번호
     * @return 1: 평일  2: 주말  3: 성수기평일  4: 성수기주말
     */
    private int getPriceType(int hotelNum) throws Exception {

        // 오늘 날짜 평일 주말 판별. 평일 : (일~목)  주말 : (금~토)
        int today = DateUtil.getDayCode(new Date());
        boolean isWeekend = false;
        for(CommonEnum.DayCode dayCode : CommonEnum.DayCode.values()){
            if(dayCode.getCode() == today){
                isWeekend = dayCode.getIsWeekend(); // 주말(금,토) 이면 ture
                break;
            }
        }

        /* 테스트용 - 오늘날짜 공휴일로 만듬 */
//        Calendar cal = Calendar.getInstance();
//        cal.set(2022, 8, 9);
//        Date now = new Date(cal.getTimeInMillis());
        /* *** *** *** */
        Date now = new Date();

        // 오늘날짜 공휴일인지 조회
        boolean isHoliday = hotelMapper.selectHoliday(now) != null; // null 이 아니면 공휴일 (true)

        // 호텔의 공휴일 가격상태 조회 1: 비성수기 주말가격 2: 성수기 주말가격
        HotelInfoVo.HotelDetailInfo hotelDetailInfo = hotelMapper.selectHotelInfo(hotelNum);
        int holidayPriceStatus = hotelDetailInfo.getHoliday_price_status();

        // 오늘날짜 성수기 여부 판별
        boolean isPeakSeason = hotelMapper.selectTodayPeakSeason() != null; // null 이 아니면 성수기 (true)

        if(isPeakSeason){
            if(isWeekend){
                return CommonEnum.PriceType.P_WEEKEND_PRICE.getCode(); // 성수기 주말
            }
            else {
                return CommonEnum.PriceType.P_WEEKDAY_PRICE.getCode(); // 성수기 평일
            }
        }

        // 오늘이 성수기가 아닌 공휴일이면 평일, 주말여부, 공휴일 가격상태 판별해서 리턴
        if(isHoliday){
            if(holidayPriceStatus == 1){ // 공휴일 가격상태 판별
                return CommonEnum.PriceType.WEEKEND_PRICE.getCode(); // 주말가격
            }
            else {
                return CommonEnum.PriceType.P_WEEKEND_PRICE.getCode(); // 성수기 주말가격
            }
        }

        // 공휴일도 성수기도아니면 평일, 주말값 리턴
        return isWeekend ? CommonEnum.PriceType.WEEKEND_PRICE.getCode() : CommonEnum.PriceType.WEEKDAY_PRICE.getCode();
    }

    /**
     * 객실 상세정보에 필요한 데이터 추가
     * @param roomInfo - 객실정보 객체
     * @return
     */
    private HotelInfoVo.RoomInfo addRoomInfoData(HotelInfoVo.RoomInfo roomInfo) throws Exception {
        // 객실정보
        int reservable_room_count = 0; // 예약가능한 방 갯수

            int todayPrice = 0;
            // 오늘의 객실 가격 정보 조회
            int priceType = getPriceType(roomInfo.getHotel_num());

            // 해당 객실의 성수기 가격정보가 null이 아니면서 성수기면 성수기 가격정보 적용
            if(roomInfo.getP_weekday_price() != null && roomInfo.getP_weekend_price() != null){
                if(priceType == CommonEnum.PriceType.P_WEEKDAY_PRICE.getCode()){ // 성수기 평일
                    todayPrice = roomInfo.getP_weekday_price();
                }if(priceType == CommonEnum.PriceType.P_WEEKEND_PRICE.getCode()){ // 성수기 주말
                    todayPrice = roomInfo.getP_weekend_price();
                }
            }else { // 만약 성수기 가격정보가 null인데 성수기면 그냥 평일, 주말가격으로 적용
                if(priceType == CommonEnum.PriceType.WEEKDAY_PRICE.getCode()){ // 평일
                    todayPrice = roomInfo.getWeekday_price();
                }else if(priceType == CommonEnum.PriceType.WEEKEND_PRICE.getCode()){ // 주말
                    todayPrice = roomInfo.getWeekend_price();
                }
            }
            // 객실의 오늘 가격정보 저장
            roomInfo.setPrice(todayPrice);

            // 호실정보
            List<HotelInfoVo.RoomDetailInfo> roomDetailInfoList = hotelMapper.selectRoomDetailInfo(roomInfo.getRoom_num());
            List<HotelInfoVo.RoomDetailInfo> roomDetailInfoListResult = new ArrayList<>();

            // 호실정보 저장
            for(HotelInfoVo.RoomDetailInfo roomDetailInfo : roomDetailInfoList){
                // 호실 사용 금지 판별
                if(roomDetailInfo.getRoom_closed_start() != null && roomDetailInfo.getRoom_closed_end() != null){
                    if(DateUtil.checkDateBetween(roomDetailInfo.getRoom_closed_start(), roomDetailInfo.getRoom_closed_end(), new Date())){
                        // 오늘날짜가 사용금지일에 해당하면 Available_yn = false
                        roomDetailInfo.setAvailable_yn(false);
                    }
                }
                roomDetailInfo.setAvailable_yn(true);

                // 예약테이블의 예약상태가 1(예약중) 이 아니면 예약가능한 상태
                if(roomDetailInfo.getReservation_status() == null){ // 예약정보가 존재하지않으면 예약가능
                    roomDetailInfo.setStatus(CommonEnum.RoomDetailStatus.AVAILABLE.getCode());
                    reservable_room_count += 1;
                }else {
                    if(roomDetailInfo.getReservation_status() != 1){
                        // 사용금지가 아니고 호실상세정보삭제 예정일이 없다면 예약가능
                        if(roomDetailInfo.getAvailable_yn() && roomDetailInfo.getDelete_date() == null){
                            roomDetailInfo.setStatus(CommonEnum.RoomDetailStatus.AVAILABLE.getCode());
                            reservable_room_count += 1;
                        }
                        else {
                            roomDetailInfo.setStatus(CommonEnum.RoomDetailStatus.UNAVAILABLE.getCode());
                        }
                    }
                }

                roomDetailInfoListResult.add(roomDetailInfo);
            }

            // 예약가능방갯수
            roomInfo.setReservable_room_count(reservable_room_count);

            // 호실이 존재하는데 예약가능방 갯수가 0개면 예약 혹은 호실사용금지가 제일 빨리 끝나는 호실의 예약 or 사용금지 날짜 제공
            if(reservable_room_count == 0 && (!CollectionUtils.isEmpty(roomDetailInfoList))){
                //

            }

            // 객실 이미지 조회
            List<String> imageList = selectImage(CommonEnum.ImageType.ROOM.getCode(), roomInfo.getRoom_num());

            // 객실 태그 조회
            List<Integer> tags = new ArrayList<>();
            tags = hotelMapper.selectRoomTags(roomInfo.getRoom_num());

            if(!CollectionUtils.isEmpty(imageList)){
                roomInfo.setImage(imageList);
            }

            roomInfo.setRoom_detail_info(roomDetailInfoListResult);
            roomInfo.setTags(tags);

        return roomInfo;
    }

    /**
     * JWT Token에서 PK조회
     * @param jwtToken
     * @return
     * @throws Exception
     */
    private int getPk(String jwtToken) throws Exception{
        JwtTokenDto.PayLoadDto payloadData = jwtTokenProvider.getPayload(jwtToken);
        return payloadData.getId();
    }

    private void insertRoomDelete(int pk, String insert_user) throws Exception{
        HotelInfoVo.DeleteTable roomDeleteTable = new HotelInfoVo.DeleteTable();
        roomDeleteTable.setPk(pk);
        roomDeleteTable.setInsert_user(insert_user);
        hotelMapper.insertRoomDelete(roomDeleteTable);
    }

    private void insertRoomDetailDelete(int pk, String insert_user) throws Exception{
        HotelInfoVo.DeleteTable roomDetailDeleteTable = new HotelInfoVo.DeleteTable();
        roomDetailDeleteTable.setPk(pk);
        roomDetailDeleteTable.setInsert_user(insert_user);
        hotelMapper.insertRoomDetailDelete(roomDetailDeleteTable);
    }

    /**
     * 에러 처리용 메소드들
     * 나중에 Exception 핸들러 만들어서 처리해야함
     * @param commonResponseVo
     * @return
     */

    private boolean isResultError(CommonResponseVo commonResponseVo){
        return commonResponseVo.getResult().equals("ERROR");
    }

    private CommonResponseVo noSearchResult (CommonResponseVo result){
        result.setResult("ERROR");
        result.setMessage("조회된 결과 없음");
        return result;
    }

    private CommonResponseVo notMyHotel (CommonResponseVo result){
        result.setResult("ERROR");
        result.setMessage("해당 사업자는 이 정보를 수정/삭제할 권한이 없습니다");
        return result;
    }

    private CommonResponseVo ErrorResult (CommonResponseVo result){
        result.setResult("ERROR");
        result.setMessage("BACK-0001");
        return result;
    }

    /**
     * 호텔 상세정보 조회 - 호텔정보, 객실정보, 호실정보
     * @param hotelNum
     * @return
     */
    private HotelInfoVo.HotelDetailInfoResponse getHotelInfo(int hotelNum) throws Exception{
        HotelInfoVo.HotelDetailInfoResponse result = new HotelInfoVo.HotelDetailInfoResponse();
        // 호텔정보 조회
        HotelInfoVo.HotelDetailInfo hotelDetailInfo = hotelMapper.selectHotelInfo(hotelNum);

        // 조회된 호텔정보 없으면 빈값 리턴
        if(hotelDetailInfo == null){
            result.setMessage("호텔 상세정보 조회 완료");
            result.setData(null);
            return result;
        }

        // 위도 경도값 / 호텔 전화번호 복호화
        List<Double> location = new ArrayList<>();
        location.add(hotelDetailInfo.getLongitude());
        location.add(hotelDetailInfo.getLatitude());
        hotelDetailInfo.setLocation(location);
        hotelDetailInfo.setPhone_num(aes256Util.decrypt(hotelDetailInfo.getPhone_num()));

        // 호텔 이미지 조회
        List<String> hotelImageList = selectImage(CommonEnum.ImageType.HOTEL.getCode() ,hotelNum);
        if (!CollectionUtils.isEmpty(hotelImageList)){
            hotelDetailInfo.setImage(hotelImageList);
        }

        // 성수기 정보 조회
        List<HotelInfoVo.PeakSeason> peakSeasonList = hotelMapper.selectPeakSeasonList(hotelNum);
        if(!CollectionUtils.isEmpty(peakSeasonList)){
            hotelDetailInfo.setPeak_season_list(peakSeasonList);
        }

        // 호텔 태그 정보 조회
        List<Integer> hotelTagList = hotelMapper.selectHotelTags(hotelNum);
        if(!CollectionUtils.isEmpty(hotelTagList)){
            hotelDetailInfo.setTags(hotelTagList);
        }

        // 객실정보
        List<HotelInfoVo.RoomInfo> roomInfoList = hotelMapper.selectRoomInfoList(hotelNum);
        List<HotelInfoVo.RoomInfo> RoomInfoListData = new ArrayList<>();
        for(HotelInfoVo.RoomInfo roomInfo : roomInfoList){
            addRoomInfoData(roomInfo); // 객실, 호실정보등 필요한 데이터 추가
            RoomInfoListData.add(roomInfo);
        }

        hotelDetailInfo.setRoom_list(RoomInfoListData);
        result.setData(hotelDetailInfo);
        result.setMessage("호텔 상세정보 조회 완료");

        return result;
    }

}
