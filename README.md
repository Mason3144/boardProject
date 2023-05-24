# 게시판 프로젝트

## 공통 기능
- [x] 예외처리
- [x] 공통 Dto
- [ ] 테스트작성
- [ ] Sending email to manager when error got
- [x] Spring Security 적용
- [x] JWT를 이용한 인증 및 인가 처리
- [x] Google Social Login 기능 

## 회원 기능
- [x] Entity 작성
  (관리자 및 회원 상태 추가)
- [x] DTO 작성
- [x] 기본 CRUD
  - [ ] password 업데이트시 PasswordEncoder사용
  - [ ] Social Login user는 패스워드 업데이트 관련 로직 추가


 

### 부가 기능
- [ ] 이메일 verification, social login
- [ ] Photo upload for profile

## 게시판 기능
- [x] Entity 작성
- [x] DTO 작성
- [ ] 기본 CRUD
- [ ] 내 게시글
- [ ] 게시글 공개, 비공개
- [ ] 게시글 조회수, 최신글
- [ ] 댓글 기능, 댓글 갯수
- [ ] 좋아요 기능, 좋아요 갯수


### 부가 기능 및 보완 사항
- [ ] 이메일 notification
- [ ] 첨부파일 업로드
- [ ] BoardDto.Response의 작성자를 작성자 이름만 응답할지,
  Member 엔티티 전체를 해줄지 비교 (CommentDto도 마찬가지)