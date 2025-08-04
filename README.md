# 일정관리
Spring 기초 과제

## API 명세서

### 속성
#### 일정

| 필드명     | 타입     | 필수 | 설명                   |
|------------|----------|------|------------------------|
| ID         | Long     | X    | 일정 ID (자동 생성)    |
| title      | String   | O    | 일정 제목              |
| content    | String   | O    | 일정 내용              |
| writer     | String   | O    | 등록자 이름            |
| password   | String   | O    | 수정/삭제용 비밀번호   |
| createdAt  | datetime | X    | 등록일 (자동 생성)     |
| modifiedAt | datetime | X    | 수정일 (자동 생성)     |

#### 댓글

| 필드명     | 타입     | 필수 | 설명                    |
|------------|----------|------|-------------------------|
| ID         | Long     | X    | 댓글 ID (자동 생성)      |
| comment    | String   | O    | 댓글 내용                |
| writer     | String   | O    | 등록자 이름              |
| password   | String   | O    | 수정/삭제용 비밀번호      |
| createdAt  | datetime | X    | 등록일 (자동 생성)        |
| modifiedAt | datetime | X    | 수정일 (자동 생성)        |
| schedulerId| Long     | X    | 해당 일정의 Id(자동 생성) |

### 기능
#### 일정

| 기능           | 메서드 | 엔드포인트        | 설명                                        |
|----------------|--------|-------------------|---------------------------------------------|
| 일정 등록      | POST   | /schedules        | 새로운 일정을 등록합니다                       |
| 일정 조회      | GET    | /schedules        | 모든 일정을 조회합니다 (수정일 기준 내림차순)   |
| 선택 일정 조회 | GET    | /schedules/{id}   | 특정 일정을 조회합니다                         |
| 일정 수정      | PATCH  | /schedules/{id}   | 특정 일정을 수정합니다 (일정 제목, 내용만)      |
| 일정 삭제      | DELETE | /schedules/{id}   | 특정 일정을 삭제합니다                         |

#### 댓글

| 기능           | 메서드 | 엔드포인트               | 설명                                      |
|----------------|--------|--------------------------|-------------------------------------------|
| 댓글 등록      | POST   | /schedules/{id}/comments  | 일정에 새로운 댓글을 등록합니다              |

## 일정 등록 예시

### POST  /schedules

#### -----Request-----
```json
{
  "title": "세션",
  "content": "세션 내용",
  "writer": "튜터님",
  "password": "1234"
}
```

#### -----Response-----
```json
{
  "id": 1,
  "title": "세션1",
  "content": "세션 내용1",
  "writer": "튜터님1",
  "createdAt": "2025-07-31T00:00:00",
  "modifiedAt": "2025-07-31T00:00:00"
}
```

## 일정 조회 예시

| 파라미터 | 타입   | 설명                                 |
|----------|--------|--------------------------------------|
| writer   | string | 작성자명 (선택). 미입력 시 전체 조회   |

### 일정 전체 조회 (작성자명 미지정)

### GET  /schedules

#### -----Response-----
```json
[
  {
    "id": 2,
    "title": "세션2",
    "content": "세션 내용2",
    "writer": "튜터님2",
    "createdAt": "2025-07-31T01:00:00",
    "modifiedAt": "2025-07-31T01:00:00"
  },
  {
    "id": 1,
    "title": "세션1",
    "content": "세션 내용1",
    "writer": "튜터님1",
    "createdAt": "2025-07-31T00:00:00",
    "modifiedAt": "2025-07-31T00:00:00"
  }
]
```

### 일정 전체 조회 (작성자명 지정)

### GET  /schedules?writer

#### -----Request-----

### GET  /schedules?writer=튜터님1

#### -----Response-----
```json
[
  {
    "id": 1,
    "title": "세션1",
    "content": "세션 내용1",
    "writer": "튜터님1",
    "createdAt": "2025-07-31T00:00:00",
    "modifiedAt": "2025-07-31T02:00:00"
  },
  {
    "id": 11,
    "title": "세션11",
    "content": "세션 내용11",
    "writer": "튜터님1",
    "createdAt": "2025-07-31T01:00:00",
    "modifiedAt": "2025-07-31T01:00:00"
  }
]
```

## 선택 일정 조회 예시

### GET  /schedules/{id}

#### -----Request-----
- path parameter: id (예시: 1) 

#### -----Response-----
```json
{
  "scheduler": {
    "id": 1,
    "title": "세션1",
    "content": "세션1",
    "writer": "튜터님1",
    "createdAt": "2025-07-31T00:00:00",
    "modifiedAt": "2025-07-31T00:00:00"
  },
  "comments": [
    {
      "comment": "댓글 내용",
      "writer": "작성자",
      "createdAt": "2025-07-31T00:00:30",
      "modifiedAt": "2025-07-31T00:00:30",
      "schedulerId": 1
    }
  ]
}
```

## 일정 수정 예시

### PATCH  /schedules/{id}

#### -----Request-----
- path parameter: id (예시: 1)
```json
{
  "title": "수정된 세션",
  "content": "수정된 세션 내용",
  "password": "1234"
}
```

#### -----Response-----
```json
{
  "id": 1,
  "title": "수정된 세션",
  "content": "수정된 세션 내용",
  "writer": "튜터님",
  "createdAt": "2025-07-31T00:00:00",
  "modifiedAt": "2025-07-31T01:00:00"
}
```

## 일정 삭제 예시

### DELETE  /schedules/{id}

#### -----Request-----
- path parameter: id (예시: 1)
```json
{
  "password": "1234"
}
```

## 댓글 등록 기능 예시

### POST /schedules/{id}/comments

#### -----Request-----
- path parameter: id (예시: 1)
```json
{
  "comment": "댓글 내용",
  "writer": "작성자",
  "password": "1234"
}
```

#### -----Response-----
```json
{
  "comment": "댓글 내용",
  "writer": "작성자",
  "createdAt": "2025-07-31T00:00:00",
  "modifiedAt": "2025-07-31T01:00:00",
  "schedulerId": 1
}
```


## ERD
<img width="517" height="285" alt="일정 및 댓글 ERD 다이어그램" src="https://github.com/user-attachments/assets/24aa108b-1b00-4630-b4ea-a6047661c085" />
