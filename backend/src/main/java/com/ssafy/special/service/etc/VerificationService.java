package com.ssafy.special.service.etc;

import com.ssafy.special.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

import static org.hibernate.id.IdentifierGeneratorHelper.createNumber;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final RedisUtil redisUtil;

    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendVerifyCode(String email) {

        Random random = new Random();
        int num = random.nextInt(10000);
        String value = String.format("%05d",num);

        System.out.println(value);

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("이메일 인증");
            String body = "";
            body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
            body += "<h1>" + value + "</h1>";
            body += "<h3>" + "감사합니다." + "</h3>";
            message.setText(body,"UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        // 이메일 전송
        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redis에 인증코드 등록
        redisUtil.setDataExpire(email,value,180000);


    }

    public int check(String key, String userInput) {
        String code = redisUtil.getData(key);

        if(code==null) { // 인증번호 만료
            redisUtil.deleteData(key);
            return 0;
        }

        System.out.println(code + " " + userInput);
        if(userInput.equals(code)) { // 인증 성공
            return 1;
        } else { // 인증 실패
            return 2;
        }
    }

}
