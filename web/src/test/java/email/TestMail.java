package email;

import com.ysma.ppt.util.mail.EmailEntity;
import com.ysma.ppt.util.mail.MailUtil;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * desc: TODO
 *
 * @author ysma
 * date : 2020/1/15 17:32
 */
public class TestMail {

    public static void main(String[] args) {

        EmailEntity bo = new EmailEntity();
        bo.setHost("smtp.163.com");
        bo.setAccount("mayongsheng331@163.com");
        bo.setPassword("ysMa1987");
        bo.setPort("465");
        JavaMailSender mailSender = MailUtil.createMailSender(bo, 5000);
        MailUtil.testConnection(mailSender, bo.getAccount(), new String[]{"mayongsheng331@163.com"});
    }
}
