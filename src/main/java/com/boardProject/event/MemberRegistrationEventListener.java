package com.boardProject.event;

import com.boardProject.member.entity.Member;
import com.boardProject.member.service.MemberService;
import com.boardProject.smtp.email.EmailSender;
import com.boardProject.utils.UriCreator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.MailSendException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.net.URI;

// @EnableAsync와 @Async 에너테이션을 사용하여 해당 이벤트를 비동기적으로 처리
@EnableAsync
@Component
@Slf4j
public class MemberRegistrationEventListener {
    @Value("${mail.subject.member.registration}")
    private String subject;

    @Value("${mail.template.name.member.join}")
    private String templateName;

    private final String URI= "/v1/email/verification?code=";
    @Value("${url}")
    private String URL;

    private final EmailSender emailSender;
    private final MemberService memberService;

    public MemberRegistrationEventListener(EmailSender emailSender, MemberService memberService) {
        this.emailSender = emailSender;
        this.memberService = memberService;
    }

    // @EventListener를 통해 service 레이어에서 발생시켜 생성된 MemberRegistrationApplicationEvent 객체를 전달 받음
    @Async
    @EventListener
    public void listen(MemberRegistrationApplicationEvent event) throws Exception {
        try {

            String[] to = new String[]{event.getMember().getEmail()};

            String text1 = event.getMember().getEmail() + "님, 회원 가입이 성공적으로 완료되었습니다.";
            String text2 = "아래의 링크로 이메일 인증을 마무리 해주세요.";
            String text3= URL+URI+event.getMember().getEmailVerification().getCode();

            Messages message = new Messages(text1,text2,text3);

            // 이메일 발송에 관련된 데이터들을 이메일 발송을 위한 객체에 전달
            emailSender.sendEmail(to, subject, message, templateName);
            // 여기까지가 이벤트에 대한 처리였으며 이 이후로는 이메일 발송 처리 로직이 된다.

        } catch (MailSendException e) {
            e.printStackTrace();
            log.error("MailSendException: rollback for Member Registration:");

            // 다른 스레드의 작업은 트랜잭션이 적용이안되므로, 만약 이메일 전송에 실패하면 수동적으로 해당 회원을 DB에서 지워준다.
            Member member = event.getMember();
            memberService.deleteMember(member.getMemberId());
        }
    }
    @AllArgsConstructor
    @Getter
    public class Messages{
        String text1;
        String text2;
        String text3;
    }
}