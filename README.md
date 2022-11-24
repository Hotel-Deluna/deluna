### 목차
 1. 프로젝트 소개
 2. 사용한 기술 스택
 3. 각 도메인별 요구사항
 4. Rest Api 문서
 5. 개발자
 
<hr />

### 프로젝트 소개
 <img width="100%" src="https://user-images.githubusercontent.com/48265181/197496797-901764c5-8a41-4467-b7e3-139e68051e6d.png"/>
 
 호텔델루나 프로젝트는 호텔 예약 플랫폼 개발 프로젝트입니다.
 
 호텔델루나는 사업자의 경우 호텔 정보를 업로드 및 요청된 예약을 관리하고, 고객은 호텔 정보 조회 및 예약 요청을 할 수 있는 플랫폼입니다.

 연동 API 문서 : <a href="http://43.200.222.222:8080/swagger-ui/">Swagger</a>
<hr />


### UI 화면
1. 고객 & 사업자 회원가입
 <img width="50%" src="https://user-images.githubusercontent.com/48265181/197475530-5738675d-ea98-48ed-8fea-ee0cfc002c0b.gif" />
 
2. 고객 로그인 & 사업자 로그인
 <img width="50%" src="https://user-images.githubusercontent.com/48265181/197472389-1e3bbbef-cbe0-4c12-98ec-62caa37ba808.gif" />
 
3. 로그인 고객 & 비로그인 고객 & 사업자 메인페이지

|로그인 고객|비회원 고객|사업자|
|:---:|:---:|:---:|
|<img src="https://user-images.githubusercontent.com/48265181/197466752-b1512c48-f3e7-4546-b305-f2ca0385562b.png" />|<img src="https://user-images.githubusercontent.com/48265181/197473633-ea3e7ebf-8382-4ce7-a911-bcea7f797e01.png" />|<img src="https://user-images.githubusercontent.com/48265181/197473882-66df162b-9b76-4e5d-a30f-60b0631bfee7.png" />

4. 예약가능한 호텔 리스트

|테이블로 보기|지도로 보기|마커(인포윈도우)|
|:---:|:---:|:---:|
|<img src="https://user-images.githubusercontent.com/48265181/197476805-0c9f1f71-19b8-4e4f-9eb2-fdd98a593200.png" />|<img src="https://user-images.githubusercontent.com/48265181/197476811-9dff4182-9ba6-413e-9f65-403d31f2593c.png" />|<img src="https://user-images.githubusercontent.com/48265181/197476820-0d84031b-7340-4a25-8830-a1214228f77d.png" />

5. 선택한 호텔 상세페이지
<img width="50%" src="https://user-images.githubusercontent.com/48265181/197479105-3ccddf58-4219-45a8-bf53-4854b017410e.gif" />

6. 호텔 예약페이지
<img width="50%" src="https://user-images.githubusercontent.com/48265181/197478033-732a865b-93ba-481b-a4f1-68fb297c5bd2.gif" />

7. 사업자(호텔) 객실 등록하기 - 이미지 등록&변경&일괄삭제&개별삭제&순서바꾸기
<img width="50%" src="https://user-images.githubusercontent.com/48265181/197477991-767d4d68-bc5c-4810-8b33-31bd54714a34.gif" />

<hr />

### 사용한 기술 스택

*Rest Api* </br>
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=Java&logoColor=white"> <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"> <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=for-the-badge&logo=Spring&logoColor=white">  

*Cloud* </br>
<img src="https://img.shields.io/badge/Amazon S3-6DB33F?style=for-the-badge&logo=Amazon S3&logoColor=white">
<img src="https://img.shields.io/badge/Amazon-231F20?style=for-the-badge&logo=Amazon AWS&logoColor=white">

*DB* </br>
<img src="https://img.shields.io/badge/MariaDB-6DB33F?style=for-the-badge&logo=MariaDB&logoColor=white">

### 도메인별 요구사항
1. 사용자
* 회원가입, 소셜로그인, 일반로그인 
* 아이디찾기, 비밀번호 찾기
* 비밀번호 재설정
* 정보조회
* 회원탈퇴

2. 예약
* 예약하기
* 예약내역 조회
* 예약취소
* 예약취소 사유조회

3. 사업자
* 사업자 회원가입, 정보수정, 로그인
* 아이디, 비밀번호 찾기, 재설정
* 사업자번호 진위여부 확인
* 정보조회
* 회원탈퇴

4. 호텔
* 호텔등록
* 호텔 정보 조회
* 호텔 삭제
* 호텔 정보 수정
* 객실 등록
* 객실 정보 조회
* 객실 정보 수정
* 객실 삭제
* 예약요청범위에 해당하는 객실 조회
* 사업자가 소유한 호텔 리스트 조회
* 사업자가 소유한 호텔의 예약정보 조회
* 객실 이용불가 / 이용허가 설정
* 조건검색 : 검색어나 요청한 조건에 일치하는 호텔 검색 
* 여행지 정보

5. 공통 및 유틸
* 휴대폰 인증
* 이메일 인증
* 이미지 등록 및 조회
* 알림 문자, 메일 전송
* 공휴일 정보 저장

### Rest Api 문서
<a href="http://43.200.222.222:8080/swagger-ui/">Swagger</a>

### 개발자
<table>
    <tr>
        <td align="center">
            <a href="https://github.com/"><img  width="100px" src="https://user-images.githubusercontent.com/48265181/197496797-901764c5-8a41-4467-b7e3-139e68051e6d.png" /></a>
        </td>
        <td align="center">
            <a href="https://github.com/"><img  width="100px" src="https://avatars.githubusercontent.com/u/53042885?v=4" /></a>
        </td>  
        </td>  
    </tr>
    <tr>
        <td align="center">한동희</td>
        <td align="center">김영수</td>
    </tr>
    <tr>
        <td align="center">Member, Reservation Domain</td>
        <td align="center">Hotel, Owner Domain, Common util</td>
    </tr>
</table>
