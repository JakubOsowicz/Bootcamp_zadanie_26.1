package pl.osowicz.task_manager.mail;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailSenderService {
    private JavaMailSender javaMailSender;

    public MailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String to, String subject, String content, boolean isHtmlContent) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(content, isHtmlContent);
        javaMailSender.send(mimeMessage);
    }

    public void resetPasswordMail(String email, String key) throws MessagingException {
        String subject = "[Wymagane działanie] Link do zmiany hasła";
        String link = "<a href=\"http://localhost:8080/resetPassword?key=" + key + "\">Ustaw nowe hasło</a>";
        String content = "<p>Cześć! Poniżej znajdziesz link, który pozwoli Ci ustawić nowe hasło w naszym serwisie</p>" + link;
        sendMail(email, subject, content, true);
    }

//    public void changePasswordAlertMail(String email) {
//        String subject = "[Alert] Twoje hasło zostało zmienione";
//        String content = "<p>Twoje hasło w naszym sewisie właśnie zostało zmienione</p> Jeśli to nie Ty, niezwłocznie " +
//                "zmień swoje hasło korzystając z opcji \"Zapomniałem hasła\".";
//    }
}
