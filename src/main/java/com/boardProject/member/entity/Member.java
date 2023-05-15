

package com.boardProject.member.entity;

import com.boardProject.audit.Auditable;
import com.boardProject.board.entity.Likes;
import com.boardProject.board.entity.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.w3c.dom.stylesheets.LinkStyle;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Member extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long memberId;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false,length = 50)
    private String password;
    @Enumerated
    @Column(nullable = false, length = 20)
    private MemberStatus memberStatus = MemberStatus.MEMBER_ACTIVE;
    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<Likes> likes = new LinkedList<>();
    public void setLikes(Likes likes){
        this.likes.add(likes);
        if(likes.getMember()!=this) likes.setMember(this);
    }

    public void setPosts(Post post){
        posts.add(post);
        if(post.getMember() != this) post.setMember(this);
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
}
