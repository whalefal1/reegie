package com.example.reggie.utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/*
邮件发送工具类
 */

public class EmailSending {
    private String recipient;
    private  String sender="450582885@qq.com";
    private  String password="hlzwtgvfocgucbcf";
    public void sendEmail(String emailAddress,String header,String context) throws MessagingException {
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host", "smtp.qq.com");
            properties.setProperty("mail.smtp.port", "465");
            properties.setProperty("mail.smtp.auth", "true");
            properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            properties.setProperty("mail.smtp.socketFactory.fallback", "false");
            properties.setProperty("mail.smtp.socketFactory.port", "465");
            Session session = Session.getInstance(properties);
            session.setDebug(true);
            Transport transport = session.getTransport();
            transport.connect("smtp.qq.com", "450582885", "hlzwtgvfocgucbcf");
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(emailAddress));
            message.setSubject(header);
            message.setSentDate(new Date());
        /*
        此处可编辑邮件内容
         */
            message.setContent(context,"text/html;charset=UTF-8");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
    }
}
