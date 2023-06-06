

package com.boardProject.member.entity;

import com.boardProject.audit.Auditable;
import com.boardProject.board.entity.Comment;
import com.boardProject.board.entity.Likes;
import com.boardProject.board.entity.Posts;
import com.boardProject.emailVerification.entity.EmailVerification;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member extends Auditable {
    public Member(Integer memberId) {
        this.memberId = Long.valueOf(memberId);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(length = 100) //password는 암호화되어 저장되기 때문에 열의 길이는 100으로 지정
    private String password;
    @Column
    private boolean socialLogin;
    @Column
    private String picture=null;

    @Enumerated(value = EnumType.STRING)
    @Column(length = 20, nullable = false)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;

    // @ElementCollection 사용시 DB에 중복검열이 안된다... 해당 어플리케이션과 같이 계속해서 중복된 값들을 사용할 경우에는 사용을 권장하지 않음
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();
    @OneToOne(mappedBy = "member", cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
    private EmailVerification emailVerification;
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Posts> posts = new ArrayList<>();
    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Likes> likes = new LinkedList<>();
    @OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE)
    private List<Comment> comments = new LinkedList<>();

    public void setEmailVerification(EmailVerification emailVerification){
        this.emailVerification = emailVerification;
        if(emailVerification.getMember()!=this) emailVerification.setMember(this);
    }
    public void setComments(Comment comment){
        this.comments.add(comment);
        if(comment.getMember()!=this) comment.setMember(this);
    }

    public void setLikes(Likes likes){
        this.likes.add(likes);
        if(likes.getMember()!=this) likes.setMember(this);
    }

    public void setPosts(Posts posts){
        this.posts.add(posts);
        if(posts.getMember() != this) posts.setMember(this);
    }

    public enum MemberStatus{
        MEMBER_ACTIVE("활동중"),
        MEMBER_SLEEP("활동 중지"),
        MEMBER_QUIT("활동 정지");
        @Getter
        private String status;

        MemberStatus(String status) {
            this.status = status;
        }
    }

    public Map<String,Object> getAttributes(){
        Map<String,Object> map = new HashMap<>();
        map.put("memberId", this.getMemberId());
        map.put("email", this.getEmail());
        map.put("name", this.getName());
        map.put("password", this.getPassword());
        map.put("socialLogin", this.isSocialLogin());
        map.put("memberStatus", this.getMemberStatus());
        map.put("roles", this.getRoles());
        return map;
    }
}
