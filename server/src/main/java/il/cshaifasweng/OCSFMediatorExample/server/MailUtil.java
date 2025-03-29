package il.cshaifasweng.OCSFMediatorExample.server;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class MailUtil {
    public static void sendEmail(String to, String subject, String body) throws MessagingException {
        final String fromEmail = "doniakhamishu@gmail.com"; // your Gmail address
        final String password = "htxp hzrt gzhk fwwf"; // your 16-digit Gmail App Password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(fromEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);

        System.out.println("Email sent successfully to " + to);
    }


}
