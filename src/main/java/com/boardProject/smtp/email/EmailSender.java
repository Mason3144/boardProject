package com.boardProject.smtp.email;

import com.boardProject.event.MemberRegistrationEventListener;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailSender {
    private final JavaMailSender mailSender;
    private final EmailSendable emailSendable;

    public EmailSender(JavaMailSender mailSender, EmailSendable emailSendable) {
        this.mailSender = mailSender;
        this.emailSendable = emailSendable;
    }

    // MemberRegistrationEventListener에서 이메일 이벤트 발생시 직접적으로 호출되는 메서드
    // EmailSendable을 구현한 클래스중 EmailConfiguration에 메인 빈으로 등록된 TempleteEmailSandable이 호출된다.
    public void sendEmail(String[] to, String subject, MemberRegistrationEventListener.Messages message, String templateName) throws MailSendException,
            InterruptedException {
        emailSendable.send(to, subject, message, templateName);
    }
}
