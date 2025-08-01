# 일정관리
Spring 기초 과제<br><br><br>
## API 명세서

### 속성
#### 일정
<br><br>
| 필드명     | 타입     | 필수 | 설명                   |
|------------|----------|------|------------------------|
| ID         | Long     | X    | 일정 ID (자동 생성)    |
| title      | String   | O    | 일정 제목              |
| contents   | String   | O    | 일정 내용              |
| writer     | String   | O    | 등록자 이름            |
| password   | String   | O    | 수정/삭제용 비밀번호   |
| createdAt  | datetime | X    | 등록일 (자동 생성)     |
| modifiedAt | datetime | X    | 수정일 (자동 생성)     |

#### 댓글
<br><br>
| 필드명     | 타입     | 필수 | 설명                    |
|------------|----------|------|-------------------------|
| ID         | Long     | X    | 일정 ID (자동 생성)      |
| contents   | String   | O    | 댓글 내용                |
| writer     | String   | O    | 등록자 이름              |
| password   | String   | O    | 수정/삭제용 비밀번호      |
| createdAt  | datetime | X    | 등록일 (자동 생성)        |
| modifiedAt | datetime | X    | 수정일 (자동 생성)        |
| scheduledId| Long     | X    | 해당 일정의 Id(자동 생성) |
<br><br>
### 기능
#### 일정
<br><br>
| 기능           | 메서드 | 엔드포인트        | 설명                                        |
|----------------|--------|-------------------|---------------------------------------------|
| 일정 등록      | POST   | /schedules        | 새로운 일정을 등록합니다                    |
| 일정 조회      | GET    | /schedules        | 모든 일정을 조회합니다 (수정일 기준 내림차순) |
| 선택 일정 조회 | GET    | /schedules/{id}   | 특정 일정을 조회합니다                      |
| 일정 수정      | PATCH  | /schedules/{id}   | 특정 일정을 수정합니다 (일정 제목, 내용만)  |
| 일정 삭제      | DELETE | /schedules/{id}   | 특정 일정을 삭제합니다                      |
<br><br>
####댓글
<br><br>
| 기능           | 메서드 | 엔드포인트               | 설명                                      |
|----------------|--------|------------------------|-------------------------------------------|
| 댓글 등록      | POST   | /schedules/{id}/comment | 일정에 새로운 댓글을 등록합니다             |
<br><br><br>
### 일정 등록 예시
<br>
### POST  /schedules
<br><br>
#### -----Request-----
```JSON
{
  "title": "세션",
  "contents": "세션 내용",
  "writer": "튜터님",
  "password": "1234"
}
```
<br>
#### -----Response-----
```JSON
{
  "id": 1,
  "title": "세션1",
  "contents": "세션 내용1",
  "writer": "튜터님1",
  "createdAt": "2025-07-31T00:00:00",
  "modifiedAt": "2025-07-31T00:00:00"
}
```
<br><br>
### 일정 조회 예시
<br>
### GET  /schedules
<br><br>
#### -----Response-----
```JSON
[
  {
    "id": 1,
    "title": "세션1",
    "contents": "세션 내용1",
    "writer": "튜터님1",
    "createdAt": "2025-07-31T00:00:00",
    "modifiedAt": "2025-07-31T00:00:00"
  },
  {
    "id": 2,
    "title": "세션2",
    "contents": "세션 내용2",
    "writer": "튜터님2",
    "createdAt": "2025-07-31T01:00:00",
    "modifiedAt": "2025-07-31T01:00:00"
  }
]
```
<br><br>
### 선택 일정 조회 예시
<br>
### GET  /schedules/{id}
<br><br>
#### -----Request-----
- path parameter: id (예시: 1) 
<br>
#### -----Response-----
```JSON
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
<br><br>
### 일정 수정 예시
<br>
### PATCH  /schedules/{id}
<br><br>
#### -----Request-----
- path parameter: id (예시: 1)
```JSON
{
  "title": "수정된 세션",
  "contents": "수정된 세션 내용"
  "password": "1234"
}
```
<br>
#### -----Response-----
```JSON
{
  "id": 1,
  "title": "수정된 세션",
  "contents": "수정된 세션 내용",
  "writer": "튜터님",
  "createdAt": "2025-07-31T00:00:00",
  "modifiedAt": "2025-07-31T01:00:00"
}
```
<br><br>
### 일정 삭제 예시
### DELETE  /schedules/{id}
<br><br>
#### -----Request-----
- path parameter: id (예시: 1)
```JSON
{
  "password": "1234"
}
```
<br><br>
### 댓글 등록 기능 예시
<br>
### POST /schedules/{id}/comment
<br><br>
#### -----Request-----
- path parameter: id (예시: 1)
```JSON
{
  "comment": "댓글 내용",
  "writer": "작성자"
  "password": "1234"
}
```
<br>
#### -----Response-----
```JSON
{
  "comment": "댓글 내용",
  "writer": "작성자"
  "createdAt": "2025-07-31T00:00:00",
  "modifiedAt": "2025-07-31T01:00:00"
  "schedulerId": 1
}
```
## ERD
<img width="517" height="285" alt="{B515AFA8-CB3A-4B81-A6F1-80E33FD6F325}" src="https://github.com/user-attachments/assets/24aa108b-1b00-4630-b4ea-a6047661c085" />











