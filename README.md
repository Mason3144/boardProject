# 게시판 프로젝트

## Todo
멤버 테스트에서 validation 문서에 표시하기

## 공통 기능
- [x] 예외처리
- [x] 공통 Dto
- [ ] 테스트코드 작성
  - Mock test
- [ ] API 문서 생성
  - RestDocs 이용
- [x] Spring Security 적용
  - [x] JWT를 이용한 인증 및 인가 처리
  - [x] Google Social Login 기능 

## 공통 개선사항
- [x] n+1 문제
  - JPQL의 fetch join 활용

## 회원 기능
- [x] Entity 작성
  (관리자 및 회원 상태 추가)
- [x] DTO 작성
- [x] 기본 CRUD
  - [x] password 업데이트시 PasswordEncoder 사용
  - [x] Social Login User는 패스워드 업데이트 관련 로직 추가

### 부가 기능
- [x] 회원가입 이메일 verification
  - [x] JavaMailSender를 활용하여 회원가입시 사용자에게 인증 링크 이메일로 발송  
  - [x] Thymeleaf를 이용한 이메일 템플릿 활용
  - [x] 이메일 인증 후 스프링 시큐리티의 AuthenticationSuccessHandler를 이용하여 바로 로그인 실행
  - [x] 이메일 인증 사용자에게만 권한 부여
- [x] social login

## 게시판 기능
- [x] Entity 작성
- [x] DTO 작성
- [x] 기본 CRUD
- [x] 내 게시글 표시
- [x] 게시글 공개, 비공개
- [x] 게시글 조회수
- [x] 검색 기능


### 부가 기능
- [x] 댓글 기능, 댓글 갯수
- [x] 좋아요 기능, 좋아요 갯수
- [x] 첨부파일 업로드

