package com.boardProject.smtp.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Properties;

// 이메일 처리에 대한 설정 및 클래스들을 빈으로 등록
@Configuration
public class EmailConfiguration {
    // 스프링의 JavaMail을 이용하여 구글 SMTP서버에 접근, 그에 필요한 데이터들을 초기화
    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.port}")
    private int port;

    @Value("${mail.smtp.username}")
    private String username;

    @Value("${mail.smtp.password}")
    private String password;

    @Value("${mail.smtp.auth}")
    private String auth;

    @Value("${mail.smtp.starttls.enable}")
    private String tlsEnable;

    // 이메일 전송을 위한 클래스는 총 3가지로 만들어 주었으며 그중 메인 클래스는 @Primary가 붙은 TemplateEmailSendable 클래스이다.
    // 혹시라도 나중에 코드 수정이 용이 하도록 EmailSendable 인터페이스를 만들고 구현해줌

    // 두번째 클래스인 SimpleEmailSendable와 비교해보면 둘다 이메일 전송을 위한 JavaMailSender가 필요하지만
    // TemplateEmailSendable 클래스는 Thymleaf를 활용한 커스텀 템플릿에 대한 정보도 생성자에 보내주고 있다.
    @Primary
    @Bean
    public EmailSendable templateEmailSendable(TemplateEngine templateEngine) {
        return new TemplateEmailSendable(javaMailSender(), templateEngine, new Context());
    }
    @Bean
    public EmailSendable simpleEmailSendable() {
        return new SimpleEmailSendable(javaMailSender());
    }
    @Bean
    public EmailSendable mockExceptionEmailSendable() {
        return new MockExceptionEmailSendable();
    }


    // 구글 SMTP와 연결하기 위해 JavaMailSender를 사용
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", tlsEnable);

        return mailSender;
    }
}
