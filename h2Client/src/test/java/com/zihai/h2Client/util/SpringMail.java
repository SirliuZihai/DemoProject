package com.zihai.h2Client.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.util.Properties;

public class SpringMail {
    static final Logger logger = LoggerFactory.getLogger(SpringMail.class);
    private static JavaMailSenderImpl sender =new JavaMailSenderImpl();;

    static {
        sender.setHost("smtp.exmail.qq.com");
        sender.setPort(465);
        sender.setProtocol("smtp");
        sender.setUsername("ZTHA_IT@ztha.group");
        sender.setPassword("FdrfchDcSZEPs544");
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.ssl.enable", "true");
        properties.setProperty("mail.smtp.socketFactory.port", "465");
        properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        sender.setJavaMailProperties(properties);
    }

    public static String sendCode(String mailName,String code){
        try {
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setTo(mailName);
            helper.setFrom(sender.getUsername());
            helper.setText(code);
            helper.setSubject("提现申请");
            sender.send(message);
            logger.info("has send mail to {} success",mailName);
            return mailName;
        } catch (MessagingException e) {
            return null;
        }

    }



    public static void main(String[] args) throws MessagingException, IOException, InterruptedException {
        //SpringMail mail = new SpringMail();
        SpringMail.sendCode("liuyizhi@ztha.group", "TEXT3");
    }
}