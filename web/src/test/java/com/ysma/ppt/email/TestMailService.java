package com.ysma.ppt.email;

import com.ysma.ppt.PptApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * desc: TODO
 *
 * @author ysma
 * date : 2020/1/15 18:03
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PptApplication.class)// 指定启动类
public class TestMailService {

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Test
    public void test(){
        //简单邮件
        SimpleMailMessage simpleMailMessage =new SimpleMailMessage();
        simpleMailMessage.setFrom("mayongsheng331@163.com");
        simpleMailMessage.setTo("mayongsheng331@163.com");
        simpleMailMessage.setSubject("DuangDuangDuang");
        simpleMailMessage.setText("一杯茶，一根烟，一个Bug改一天");
        mailSender.send(simpleMailMessage);

        //复杂邮件
        /*MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        messageHelper.setFrom("jiuyue@163.com");
        messageHelper.setTo("September@qq.com");
        messageHelper.setSubject("BugBugBug");
        messageHelper.setText("一杯茶，一根烟，一个Bug改一天！");
        messageHelper.addInline("bug.gif", new File("xx/xx/bug.gif"));
        messageHelper.addAttachment("bug.docx", new File("xx/xx/bug.docx"));
        mailSender.send(mimeMessage);*/
    }
}
