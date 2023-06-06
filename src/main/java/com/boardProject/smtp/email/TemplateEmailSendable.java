package com.boardProject.smtp.email;

import com.boardProject.event.MemberRegistrationEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

// EmailSendable을 구현하는 3개의 클래스중 EmailConfiguration에서 메인 빈으로 등록된 클래스
@Slf4j
public class TemplateEmailSendable implements EmailSendable {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final Context context;

    public TemplateEmailSendable(JavaMailSender javaMailSender,
                                 TemplateEngine templateEngine,
                                 Context context) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.context = context;
    }

    @Override
    public void send(String[] to, String subject, MemberRegistrationEventListener.Messages message, String templateName) {
        try {
            // Context를 이용하여 생성한 커스텀 템플릿인 html의 변수에 들어갈 밸류를 넣어준다.
            context.setVariable("message1", message.getText1());
            context.setVariable("message2", message.getText2());
            context.setVariable("message3", message.getText3());

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            // TemplateEngine을 이용하여 이메일의 내용물을 완성시킨다
            // 커스텀한 html 템플릿과 지정된 변수들에 사용할 밸류들을 넣어 커스텀 템플릿을 완성 시킨다.
            String html = templateEngine.process(templateName, context);

            // 스프링의 JavaMailSender를 이용하여 메일을 보낼땐 MimeMessage 객체 형태로 메일을 보내게 된다.
            // 이때 MimeMessageHelper를 이용하여 발송될 메일의 제목, 내용, 수신자등의 데이터를 MimeMessage 객체에 넣어 준다.
            // 간단한 메일 정도는 MimeMessage 객체만으로 전송해도 상관없지만 MimeMessageHelper를 사용하면 더쉽게 많은 기능들을 사용할 수 있다.
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(html, true);

            // 마지막으로 JavaMailSender객체를 이용하여 완성된 이메일 양식을 보낸다.
            javaMailSender.send(mimeMessage);
            log.info("Sent Template email!");
        } catch (Exception e) {
            log.error("email send error: ", e);
        }

    }
}