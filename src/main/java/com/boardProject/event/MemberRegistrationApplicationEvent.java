package com.boardProject.event;

import com.boardProject.member.entity.Member;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


// service 레이어에서 이벤트 발생시 해당 객체가 이벤트로 등록됨
@Getter
public class MemberRegistrationApplicationEvent extends ApplicationEvent {
    private Member member;
    public MemberRegistrationApplicationEvent(Object source, Member member) {
        super(source);
        this.member = member;
    }
}