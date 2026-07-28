# 머니로그 API 명세서

도전 과제 API(budgets, search, export)는 이번 범위에서 제외합니다.

## 공통 응답 형식

모든 성공 응답은 아래처럼 감쌉니다.

```json
{
  "success": true,
  "message": "사람이 읽는 안내 메시지",
  "data": { }
}
```

목록 조회는 `data` 옆에 `meta.pagination`을 추가합니다.

```json
{
  "success": true,
  "message": "거래내역 목록을 조회했습니다.",
  "data": { "transactions": [ ] },
  "meta": {
    "pagination": {
      "page": 0,
      "size": 20,
      "totalItems": 42,
      "totalPages": 3,
      "hasNext": true,
      "hasPrev": false
    }
  }
}
```

에러 응답도 같은 형식이되 `success:false`이고 `code`가 붙습니다.

```json
{
  "success": false,
  "code": "TRANSACTION_NOT_FOUND",
  "message": "거래내역을 찾을 수 없습니다.",
  "data": null
}
```

### 표준 에러 코드

| HTTP 상태 | code | 상황 |
|---|---|---|
| 400 | VALIDATION_ERROR | 입력 검증 실패 |
| 401 | INVALID_CREDENTIALS | 로그인 시 이메일/비밀번호 불일치 |
| 409 | DUPLICATE_EMAIL | 이미 가입된 이메일 |
| 404 | CATEGORY_NOT_FOUND | 존재하지 않는 카테고리 |
| 404 | TRANSACTION_NOT_FOUND | 존재하지 않는 거래 |

인증되지 않은 요청(토큰 없음/만료)과 남의 데이터 접근은 Spring Security가 먼저 막기 때문에
위 공통 형식이 아니라 본문 없는 403이 내려갑니다. 이것까지 공통 형식으로 통일하려면
커스텀 `AuthenticationEntryPoint`가 필요한데, 이번 범위에서는 하지 않았습니다.

남의 데이터에 접근하는 경우는 조회 쿼리 자체가 `user_id` 기준으로 필터링되므로
`*_NOT_FOUND`(404)로 응답합니다.

### 인증 헤더

로그인 이후 모든 요청은 아래 헤더를 포함합니다.

```
Authorization: Bearer {accessToken}
```

## 인증 (Auth)

| 메서드 | 경로 | 설명 | 요청 바디 | 인증 |
|---|---|---|---|---|
| POST | /api/auth/signup | 회원가입 | {email, password, nickname} | X |
| POST | /api/auth/login | 로그인(JWT 발급) | {email, password} | X |

## 카테고리 (Category)

| 메서드 | 경로 | 설명 | 요청 바디 | 인증 |
|---|---|---|---|---|
| GET | /api/categories | 내 카테고리 목록 | - | O |
| POST | /api/categories | 카테고리 추가 | {name, type} | O |
| PUT | /api/categories/{id} | 카테고리 수정 | {name, type} | O |
| DELETE | /api/categories/{id} | 카테고리 삭제 | - | O |

## 거래내역 (Transaction)

| 메서드 | 경로 | 설명 | 인증 |
|---|---|---|---|
| GET | /api/transactions?yearMonth=2026-07&page=0&size=20 | 월별 목록 + 페이징 | O |
| POST | /api/transactions | 거래 등록 | O |
| GET | /api/transactions/{id} | 거래 상세 | O |
| PUT | /api/transactions/{id} | 거래 수정 | O |
| DELETE | /api/transactions/{id} | 거래 삭제 | O |

## 통계 (Statistics)

| 메서드 | 경로 | 설명 | 인증 |
|---|---|---|---|
| GET | /api/statistics/monthly?yearMonth=2026-07 | 월별 총수입/총지출/잔액 + 카테고리별 집계 | O |

## 대표 요청/응답 예시

### 로그인 - POST /api/auth/login

요청:
```json
{ "email": "hong@moneylog.com", "password": "pass1234!" }
```

응답 (200):
```json
{
  "success": true,
  "message": "로그인에 성공했습니다.",
  "data": { "accessToken": "eyJhbGciOi..." }
}
```

### 거래 등록 - POST /api/transactions

요청:
```json
{
  "type": "EXPENSE",
  "amount": 12000,
  "categoryId": 3,
  "description": "점심 - 김치찌개",
  "transactionDate": "2026-07-08"
}
```

응답 (201):
```json
{
  "success": true,
  "message": "거래내역이 등록되었습니다.",
  "data": {
    "id": 42,
    "type": "EXPENSE",
    "amount": 12000,
    "categoryId": 3,
    "categoryName": "식비",
    "description": "점심 - 김치찌개",
    "transactionDate": "2026-07-08",
    "createdAt": "2026-07-08T12:31:05"
  }
}
```

### 월별 통계 - GET /api/statistics/monthly?yearMonth=2026-07

응답 (200):
```json
{
  "success": true,
  "message": "월별 통계를 조회했습니다.",
  "data": {
    "income": 2500000,
    "expense": 830000,
    "balance": 1670000,
    "byCategory": [
      { "categoryName": "식비", "total": 420000 },
      { "categoryName": "교통", "total": 180000 }
    ]
  }
}
```

## 화면-API 매핑 (필수 화면 2종)

| 화면 | 사용자 행동 | 호출 API |
|---|---|---|
| 로그인 | 회원가입 | POST /api/auth/signup |
| 로그인 | 로그인 -> 토큰 저장 | POST /api/auth/login |
| 목록+등록 | 이번 달 목록 로드 | GET /api/transactions?yearMonth=... |
| 목록+등록 | 카테고리 드롭다운 로드 | GET /api/categories |
| 목록+등록 | 거래 저장 | POST /api/transactions |
| 목록+등록 | 거래 삭제 | DELETE /api/transactions/{id} |

## 개발 우선순위

1. 인증 (signup, login)
2. 카테고리 CRUD
3. 거래 CRUD
4. 월별 목록 조회 + 페이징 (타입/카테고리 필터는 이번 범위에서 제외)
5. 통계
6. 프론트 연동
