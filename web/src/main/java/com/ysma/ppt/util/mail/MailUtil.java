package com.ysma.ppt.util.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author saijie.lu
 * @create 2018-09-11 19:11
 **/
public class MailUtil {

    private static final String EMAIL_CHECKER = "^([a-z0-9A-Z]+[-|_|\\.]?)*?[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    public static JavaMailSender createMailSender(EmailEntity bo, int timeout) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(bo.getHost());
        sender.setPort(Integer.valueOf(bo.getPort()));
        sender.setUsername(bo.getAccount());
        sender.setPassword(bo.getPassword());
        sender.setDefaultEncoding("UTF-8");
        sender.setProtocol("smtp");
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", timeout + "");
        p.setProperty("mail.smtp.auth", "true");
        //p.setProperty("mail.smtp.starttls.enable", "true");
        sender.setJavaMailProperties(p);
        return sender;
    }

    //发送测试的邮件
    public static void testConnection(JavaMailSender sender, String from, String[] to) {
        if (!checkEmail(from)) {
            throw new IllegalArgumentException("发件人格式不正确");
        }
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(from);
        mail.setTo(to);
        //抄送一份给自己，不然会被163当成垃圾邮件
        mail.setCc(from);
        mail.setSubject("这是一封来自" + from + "的测试邮件");
        mail.setSentDate(new Date());// 邮件发送时间
        mail.setText("这是一封来自" + from + "的测试邮件");
        sender.send(mail);
    }

    public static void sendTextMail(JavaMailSender mailSender, String mailFrom, String[] to, String subject, String text) {
        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setFrom(mailFrom);
        mail.setTo(to);
        mail.setCc(mailFrom);
        mail.setSubject(subject);
        mail.setSentDate(new Date());// 邮件发送时间
        mail.setText(text);
        mailSender.send(mail);
    }

    public static void sendHtmlMail(JavaMailSender mailSender, String mailFrom,
                                    String[] to, String subject, String html) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(mailFrom);
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        mailSender.send(mimeMessage);
    }

    public static void sendFileMail(JavaMailSenderImpl mailSender,
                                    String[] to, String[] cc,
                                    String subject, String content,
                                    String fileName, File attach) throws Exception {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        messageHelper.setFrom(mailSender.getUsername());
        messageHelper.setTo(to);
        if(cc != null && cc.length > 0){
            messageHelper.setCc(cc);
        }
        messageHelper.setSubject(subject);
        content = StringUtils.isEmpty(content) ? "" : content;
        messageHelper.setText(content, true);
        messageHelper.addAttachment(attach.getName(), attach);
        // 发送
        mailSender.send(mimeMessage);
    }

    private static synchronized boolean checkEmail(String email) {
        boolean flag;
        try {
            Pattern EMAIL_REGEX = Pattern.compile(EMAIL_CHECKER);
            Matcher matcher = EMAIL_REGEX.matcher(email);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
