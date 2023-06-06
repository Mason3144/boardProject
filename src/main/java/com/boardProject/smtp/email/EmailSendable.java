package com.boardProject.smtp.email;

import com.boardProject.event.MemberRegistrationEventListener;
import org.springframework.stereotype.Component;

// 혹시라도 나중에 코드 수정이 용이 하도록 인터페이스를 생성해 주었으며
// 이메일 전송을 하는 3가지 클래스들에 구현해 주었다.
@Component
public interface EmailSendable {
    void send(String[] to, String subject, MemberRegistrationEventListener.Messages message, String templateName) throws InterruptedException;
}
