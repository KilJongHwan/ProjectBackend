package com.book.gpt.common;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {
    private final JavaMailSender emailSender;
    private static final Map<String, String> emailVerificationCodes = new HashMap<>();

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }
    // 이메일 인증 코드를 저장하는 메소드
    public void saveVerificationCode(String email, String verificationCode) {
        emailVerificationCodes.put(email, verificationCode);
    }

    // 이메일 발송 메소드
    public boolean sendVerificationEmail(String to, String ePw) {
        try {
            MimeMessage message = createMessage(to, ePw);
            emailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private MimeMessage createMessage(String to, String ePw) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("회원가입 이메일 인증");

        String msgg = "<div style='margin:100px;'>";
        msgg += "<h1> Sign-up Code</h1>";
        msgg += "CODE : <strong>" + ePw + "</strong><div><br/> ";
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");
        message.setFrom(new InternetAddress("dafr47@naver.com", "kil", "UTF-8"));

        return message;
    }

    public static String createVerificationCode() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            int index = rnd.nextInt(3);
            switch (index) {
                case 0:
                    key.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    key.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    key.append((rnd.nextInt(10)));
                    break;
            }
        }
        return key.toString();

    }
    // 인증 코드 확인 메소드
    public boolean verifyEmail(String email, String verificationCode) {
        // 이메일을 키로하여 저장된 인증 코드를 가져옴
        String storedCode = emailVerificationCodes.get(email);

        // 저장된 인증 코드와 입력된 인증 코드를 비교
        if (storedCode != null && storedCode.equals(verificationCode)) {
            // 일치하면 맵에서 삭제
            emailVerificationCodes.remove(email);
            return true;
        }

        return false;
    }
}

