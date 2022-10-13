package com.hotel.common.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hotel.common.CommonResponseVo;
import com.hotel.common.svc.CommonService;
import com.hotel.common.vo.CommonVo;
import com.hotel.jwt.CheckTokenInfo;
import com.hotel.jwt.JwtAuthenticationFilter;
import com.hotel.jwt.JwtTokenProvider;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/common")
//@Api(tags = {"공통으로 사용하는 API를 제공하는 Controller"})
public class CommonController {

    @Autowired
    CommonService commonService;
    
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
	CheckTokenInfo info;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
	CheckTokenInfo info;

    @ApiOperation(value="휴대폰 인증 요청 - 인증 번호 생성 및 전송")
    @ResponseBody
    @PostMapping("/phone/auth/request")
    public CommonResponseVo RequestPhoneAuth(@RequestBody CommonVo.PhoneAuthRequest PhoneAuthRequest){
        return commonService.RequestPhoneAuth(PhoneAuthRequest);
    }

    @ApiOperation(value="휴대폰 인증 확인 - 인증 번호 체크. 인증번호 맞으면 true. 틀리면 false 리턴")
    @ResponseBody
    @PostMapping("/phone/auth/verify")
    public CommonVo.VerifyPhoneAuthResponse VerifyPhoneAuth(@RequestBody CommonVo.VerifyPhoneAuthRequest verifyPhoneAuthRequest){
        return commonService.VerifyPhoneAuth(verifyPhoneAuthRequest);
    }

    @ApiOperation(value="이메일 중복 확인 - 중복이면 true. 아니면 false 리턴")
    @ResponseBody
    @PostMapping("/email/duplicate-check")
    public CommonVo.EmailDuplicateCheckResponse EmailDuplicateCheck(@RequestBody CommonVo.EmailDuplicateCheckRequest emailDuplicateCheckRequest){
        return commonService.EmailDuplicateCheck(emailDuplicateCheckRequest);
    }

    @ApiOperation(value="회원 코드 - 사업자, 일반회원 구분")
    @ResponseBody
    @GetMapping("/code/user-role")
    public CommonVo.CommonCodeResponse UserRoleCode(){
        return commonService.UserRoleCode();
    }

    @ApiOperation(value="호텔 태그 코드 (호텔시설)")
    @ResponseBody
    @GetMapping("/code/hotel")
    public CommonVo.CommonCodeResponse HotelTagCode(){
        return commonService.HotelTagCode();
    }

    @ApiOperation(value="객실 태그 코드 (객실 서비스)")
    @ResponseBody
    @GetMapping("/code/room")
    public CommonVo.CommonCodeResponse RoomTagCode(){
        return commonService.RoomTagCode();
    }

    @ApiOperation(value="탈퇴사유 코드 - 회원")
    @ResponseBody
    @GetMapping("/code/delete-reason/member")
    public CommonVo.CommonCodeResponse MemberDeleteReasonCode(){
        return commonService.MemberDeleteReasonCode();
    }

    @ApiOperation(value="탈퇴사유 코드 - 사업자")
    @ResponseBody
    @GetMapping("/code/delete-reason/owner")
    public CommonVo.CommonCodeResponse OwnerDeleteReasonCode(){
        return commonService.OwnerDeleteReasonCode();
    }

    @ApiOperation(value="지역코드 - 시,도 구분용")
    @ResponseBody
    @GetMapping("/code/region")
    public CommonVo.CommonCodeResponse RegionCode(){
        return commonService.RegionCode();
    }

//    @ApiOperation(value="여행지 코드 - 메인페이지 여행지 목록")
//    @ResponseBody
//    @GetMapping("/code/tourist-spot")
//    public CommonVo.CommonCodeResponse TouristSpotCode(){
//        return commonService.TouristSpotCode();
//    }

    @ApiOperation(value="설정파일 암호화 - 내부 API")
    @ResponseBody
    @GetMapping("/encrypt/config")
    public String EncryptConfig(String text){
        return commonService.EncryptConfig(text);
    }

    @ApiOperation(value="공휴일 정보 저장 - 내부 스케쥴러 API")
    @ResponseBody
    @GetMapping("/scheduler/crawling/holiday")
    public String HolidayCrawling(){
        return commonService.HolidayCrawling();
    }

    @ApiOperation(value="테스트용 JWT 토큰 생성 - 임시")
    @ResponseBody
    @GetMapping("/create/token")
    public String CreateToken(@RequestParam int user_num, @RequestParam int user_role){
        return commonService.CreateToken(user_num, user_role);
    }

    @ApiOperation(value="여행지 호텔 갯수 저장 - 내부 스케쥴러 API")
    @ResponseBody
    @GetMapping("/scheduler/tourist-spot-hotel-count")
    public String SaveTouristSpotHotelCount(){
        return commonService.SaveTouristSpotHotelCount();
    }

    @ApiOperation(value="여행지 사진 저장/교체 - 내부 API")
    @ResponseBody
    @PostMapping(value = "/tourist/insert/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String InsertTouristSpotImage(@ModelAttribute CommonVo.InsertTouristSpotImageRequest insertTouristSpotImageRequest){
        return commonService.InsertTouristSpotImage(insertTouristSpotImageRequest);
    }

    @ApiOperation(value="여행지 정보 저장 - 이미지없어도 등록가능. 내부 API")
    @ResponseBody
    @PostMapping(value = "/tourist/insert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String InsertTouristSpot(@ModelAttribute CommonVo.InsertTouristSpotRequest insertTouristSpotRequest){
        return commonService.InsertTouristSpot(insertTouristSpotRequest);
    }

    @ApiOperation(value="메일전송 테스트 - 테스트 API")
    @ResponseBody
    @GetMapping("/mail")
    public String MailTest(@RequestParam String text, @RequestParam String to){
        return commonService.MailTest(text, to);
    }
    
    @ApiOperation(value="토큰 재발급 api")
	@ResponseBody
	@PostMapping("/token")
	public Map<String, Object> TokenReCreate(@RequestBody Map<String, Object> map, HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		Map<String, Object> result = new HashMap<>();
		
		String token = req.getHeader("Authorization");
		String reToken = req.getHeader("refreshToken");
		if(token == null) {
			result.put("result", "ERR");
			result.put("reason", "JWT-0002");
		}else if(reToken == null) {
			result.put("result", "ERR");
			result.put("reason", "JWT-0002");
		}
		String email = info.tokenInfo(token);
		if(email == null) {
			result.put("result", "ERR");
			result.put("reason", "JWT-0003");
		}
		
		result = commonService.TokenReCreate(email);
		
		Date date = new Date();
		SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		res.setHeader("Authorization", (String) result.get("Authorization"));
		res.setHeader("RefreshToken", (String) result.get("RefreshToken"));
		res.setHeader("TokenCreateDate", sDate.format(date));
		
		result.clear();
		
		return result;
	}

    @ApiOperation(value="토큰 재발급 api")
	@ResponseBody
	@PostMapping("/token")
	public Map<String, Object> TokenReCreate(@RequestBody Map<String, Object> map, HttpServletRequest req, HttpServletResponse res) throws Exception {

		Map<String, Object> result = new HashMap<>();

		String token = req.getHeader("Authorization");
		String reToken = req.getHeader("refreshToken");
		if(token == null) {
			result.put("result", "ERR");
			result.put("reason", "JWT-0002");
		}else if(reToken == null) {
			result.put("result", "ERR");
			result.put("reason", "JWT-0002");
		}
		String email = info.tokenInfo(token);
		if(email == null) {
			result.put("result", "ERR");
			result.put("reason", "JWT-0003");
		}

		result = commonService.TokenReCreate(email);

		Date date = new Date();
		SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		res.setHeader("Authorization", (String) result.get("Authorization"));
		res.setHeader("RefreshToken", (String) result.get("RefreshToken"));
		res.setHeader("TokenCreateDate", sDate.format(date));

		result.clear();

		return result;
	}

    @ApiOperation(value="객실, 호실 예약삭제 - 내부 스케쥴러 API")
    @ResponseBody
    @GetMapping(value = "/scheduler/delete/room")
    public String deleteRoom(){
        return commonService.deleteRoom();
    }

}
