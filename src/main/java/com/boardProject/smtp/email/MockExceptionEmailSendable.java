package com.boardProject.smtp.email;

import com.boardProject.event.MemberRegistrationEventListener;
import org.springframework.mail.MailSendException;


public class MockExceptionEmailSendable implements EmailSendable {
    @Override
    public void send(String[] to, String subject, MemberRegistrationEventListener.Messages message, String templateName) throws InterruptedException {
        Thread.sleep(5000L);
        throw new MailSendException("error while sending Mock email");
    }
}