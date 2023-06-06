package com.boardProject.smtp.email;

import com.boardProject.event.MemberRegistrationEventListener;

// Mock 테스트에서 이메일 발송을 대신할 객체
// EmailConfiguration에서 해당 객체가 사용되도록 바꿔줄 수 있다.
public class MockEmailSendable implements EmailSendable {

    @Override
    public void send(String[] to, String subject, MemberRegistrationEventListener.Messages message, String templateName) throws InterruptedException {
        System.out.println("Sent mock email!");
    }
}