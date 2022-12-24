# 구현 API

## 회원 가입

- api : /signup (POST)
- 구현 여부 : O
- 구현 사항 : ID, PW, NAME 필수 값 검증 -> 중복 ID 검증 -> 문제 없을 시 회원 가입

## 로그인

- api : /signin (POST)
- 구현 여부 : O
- 구현 사항 : ID, PW 를 입력해 로그인을 진행 -> 필수 값 검증 -> 등록된 회원 검증 -> 비밀번호 일치 검증 -> 문제 없다면 JWT 토큰 발급

## 내 정보 조회

- api : /profile (GET)
- 구현 여부 : O
- 구현 사항 : Token 검증 -> Token 에 저장된 유저 프로필 정보 조회

## 내 Points 조회

- api : /points (GET)
- 구현 여부 : O 
- 구현 사항 : Token 검증 -> 저장된 유저의 Points 와 기본 정보 전달

## 글 작성

- api : /article (POST)
- 구현 여부 : O
- 구현 사항 : 글의 제목과 타이틀을 입력 -> 작성자와 함께 게시글 저장

## 글 수정

- api : /article (PUT)
- 구현 여부 : O
- 구현 사항 : 수정된 글의 제목과 타이틀을 입력 -> 글의 작성자가 동일한지 검증 -> 문제 없다면 글 수정

## 글 조회

- api : /article/{articleId} (GET)
- 구현 여부 : O
- 구현 사항 : 게시글 조회 -> 게시글 번호와 댓글들 번호 조회

## 글 삭제

- api : /article/{articleId} (DELETE)
- 구현 여부 : O
- 구현 사항 : 게시글 삭제 권한 검증 -> 게시글의 댓글단 유저들 Point 삭제 -> 게시글 + 댓글 삭제

## 댓글 등록

- api : /comments (POST)
- 구현 여부 : O
- 구현 사항 : 게시글 번호 + 댓글 내용 필수 검증 -> 작성자 +1, 댓글 작성자 +2(단, 작성자와 댓글 작성자가 동일할 경우 +1점) -> 댓글 등록

## 댓글 삭제

- api : /comments/{commentsId} (DELETE)
- 구현 여부 : O
- 구현 사항 : 본인이 작성한 댓글인지 검증 -> 댓글 작성자 -2, 게시글 작성자 -1 -> 댓글 삭제

