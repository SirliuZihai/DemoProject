package com.zihai.h2Client.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {
    public static void sendMail(String title, String to) throws MessagingException {
        String content="测试";
        String from = "";
        Properties prop=new Properties();
        prop.put("mail.host","smtp.mxhichina.com" );
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.socketFactory.port", "465");
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "465");
        Session session= Session.getInstance(prop);
        session.setDebug(true);
        Transport ts=session.getTransport();
        ts.connect(from, "");
        Message message=new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject(title);
        message.setContent(content, "text/html;charset=utf-8");
        ts.sendMessage(message, message.getAllRecipients());
    }

    public static void main(String[] args) throws MessagingException{
        sendMail("subtest","nicool@iim.ltd");
    }
}
