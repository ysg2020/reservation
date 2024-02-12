# 매장 예약 서비스 API
매장 테이블 예약을 미리 할수 있는 서비스


## 기술 스택
- Spring boot , java
- Jpa


## API
예약 API (Reservation API)
- 예약 추가
- 예약 수정
- 예약 도착 확인
- 예약 삭제
- 특정 사용자의 예약 정보 확인
- 특정 매장의 예약 정보 확인

매장 API (Store API)
- 매장 등록
- 매장 수정
- 매장 삭제
- 특정 매장 이름으로 조회
- 매장 전체 조회

리뷰 API (Review API)
- 리뷰 등록
- 리뷰 수정
- 리뷰 삭제
- 특정 사용자의 리뷰 정보 확인
- 특정 매장의 리뷰 정보 확인

---

### 예약 API (/reservation)
제약 사항
1. 예약 가능 여부
예약시 특정 매장의 예약하려는 시간대(예약하려는시간의 앞뒤로 1시간씩)에 예약상태가 성공 처리되어 있는 예약 건수를 조회하여
남아있는 테이블이 있는지 계산 후 예약 가능 여부를 판단

2. 도착 확인 체크
도착 확인은 예약시간으로부터 10분전까지만 가능하고 이후에 도착시 예약 거절(Reject) 및 도착 여부(Y/N) F 처리
*성공 처리된 예약이어야만 도착 확인 가능

- POST /reservation/addEdit
  - 예약 추가
- PUT /reservation/addEdit
  - 예약 수정
- POST /reservation/arrChk
  - 예약 도착 확인
- DELETE /reservation
  - 예약 삭제
- GET /reservation/member
  - 특정 사용자의 예약 정보 확인
- GET /reservation/store
  - 특정 매장의 예약 정보 확인

### 매장 API (/store)
- POST /store
  - 매장 등록
- PUT /store
  - 매장 수정
- DELETE /store
  - 매장 삭제
- GET /store
  - 특정 매장 이름으로 조회
- GET /store/all
  - 매장 전체 조회
  
### 리뷰 API (/review)
- POST /review/addEdit
  - 리뷰 등록
- PUT /review/addEdit
  - 리뷰 수정
- DELETE /review
  - 리뷰 삭제
- GET /review/member
  - 특정 사용자의 리뷰 정보 조회
- GET /review/store
  - 특정 매장의 리뷰 정보 조회

  
