# 일정관리

## API 명세서

### 속성

| 필드명     | 타입     | 필수 | 설명                   |
|------------|----------|------|------------------------|
| ID         | Long     | X    | 일정 ID (자동 생성)    |
| title      | String   | O    | 일정 제목              |
| contents   | String   | O    | 일정 내용              |
| writer     | String   | O    | 등록자 이름            |
| password   | String   | O    | 수정/삭제용 비밀번호   |
| createdAt  | datetime | X    | 등록일 (자동 생성)     |
| updatedAt  | datetime | X    | 수정일 (자동 생성)     |

### 기능

| 기능           | 메서드 | 엔드포인트        | 설명                                        |
|----------------|--------|-------------------|---------------------------------------------|
| 일정 등록      | POST   | /schedules        | 새로운 일정을 등록합니다                    |
| 일정 조회      | GET    | /schedules        | 모든 일정을 조회합니다 (수정일 기준 내림차순) |
| 선택 일정 조회 | GET    | /schedules/{id}   | 특정 일정을 조회합니다                      |
| 일정 수정      | PATCH  | /schedules/{id}   | 특정 일정을 수정합니다 (일정 제목, 내용만)  |
| 일정 삭제      | DELETE | /schedules/{id}   | 특정 일정을 삭제합니다                      |



### 일정등록 예시
### POST  /schedules

#### -----Request-----
```JSON
{
  "title": "세션",
  "contents": "세션 내용",
  "writer": "튜터님",
  "password": "1234"
}
```
#### -----Response-----
```JSON
{
  "id": 1,
  "title": "세션",
  "contents": "세션 내용",
  "writer": "튜터님",
  "createdAt": "2025-07-31T00:00:00",
  "updatedAt": "2025-07-31T00:00:00"
}
```

### 일정 조회 예시
### GET  /schedules

#### -----Response-----
```JSON
[
  {
    "id": 1,
    "title": "세션",
    "contents": "세션 내용",
    "writer": "튜터님",
    "createdAt": "2025-07-31T00:00:00",
    "updatedAt": "2025-07-31T00:00:00"
  },
  {
    "id": 2,
    "title": "세션2",
    "contents": "세션 내용2",
    "writer": "튜터님2",
    "createdAt": "2025-07-31T01:00:00",
    "updatedAt": "2025-07-31T01:00:00"
  }
]
```

### 선택 일정 조회 예시
### GET  /schedules/{id}

#### -----Request-----
- path parameter: id (예시: 1) 

#### -----Response-----
```JSON
{
  "id": 1,
  "title": "세션",
  "contents": "세션 내용",
  "writer": "튜터님",
  "createdAt": "2025-07-31T00:00:00",
  "updatedAt": "2025-07-31T00:00:00"
}
```

### 일정 수정 예시
### PATCH  /schedules/{id}

#### -----Request-----
- path parameter: id (예시: 1)
```JSON
{
  "title": "수정된 세션",
  "contents": "수정된 세션 내용"
}
```

#### -----Response-----
```JSON
{
  "id": 1,
  "title": "수정된 세션",
  "contents": "수정된 세션 내용",
  "writer": "튜터님",
  "createdAt": "2025-07-31T00:00:00",
  "updatedAt": "2025-07-31T01:00:00"
}
```

### 일정 삭제 예시
### DELETE  /schedules/{id}

#### -----Request-----
- path parameter: id (예시: 1)



## ERD
<img width="215" height="259" alt="{B132A72B-50FE-4285-8720-27AA4ADADAA8}" src="https://github.com/user-attachments/assets/b1b8547f-95b3-44f8-8a7b-7fc2ec05029c" />








