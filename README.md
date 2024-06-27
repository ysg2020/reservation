# :convenience_store: 매장 예약 서비스 API
매장 테이블을 미리 예약 할 수 있는 서비스 API 입니다

## :wrench: 기술 스택
- Java, Spring Boot, Spring Security, Spring Data JPA
- Mysql, Redis
- Jwt
---
## 주요 기능
회원 종류는 고객, 점주로 나뉩니다.
매장을 등록 수정 삭제하기 위해서는 점주만 가능합니다.
고객은 매장을 검색하고 예약 & 상세 정보를 확인합니다.
매장의 예약 & 상세 정보를 보고, 예약을 진행한다.(예약을 진행하기 위해서는 회원이어야만 가능합니다.)
고객은 서비스를 통해서 예약한 이후에, 예약 10분전에 도착하여 점주님이 방문 확인을 진행합니다.
고객은 예약 및 사용 이후에 리뷰를 작성할 수 있습니다.
리뷰의 경우 수정,삭제 권한은 리뷰 작성자만 삭제할 수 있습니다.

## :page_facing_up: 제약 사항
### 1. 예약 가능 여부  
예약시 특정 매장의 예약하려는 시간대(예약 하려는 시간의 앞뒤로 1시간씩)에 예약상태가 성공 처리되어 있는 예약 건수를 조회하여
남아있는 테이블이 있는지 계산 후 예약 가능 여부를 판단

### 2. 도착 확인 체크  
도착 확인은 예약시간으로부터 10분전까지만 가능하고 이후에 도착시 예약 거절(Reject) 및 도착 여부(Y/N) F 처리하고 성공 처리된 예약이어야만 도착 확인 가능

### 3. 리뷰 정책
예약 상태가 성공 처리된 예약이어야 하고 예약 1건당 1개의 리뷰만 작성 가능, 매장 도착시간에 1시간을 더한시간부터 5일이 지나기 전까지 리뷰 작성 가능

---

### 예약 API (/reservation)
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
  - 특정 매장의 상세 정보 조회
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

### 회원 API (/member)
- POST /member
  - 회원 등록
- PUT /member
  - 회원 수정
- DELETE /member
  - 회원 삭제

## :pencil2: ERD 설계
![erd](https://github.com/ysg2020/reservation/assets/70841944/47cd5cd7-0b9a-4aa4-a6bb-b5d59d7860b5)
