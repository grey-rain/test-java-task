import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailClient {

    private Logger log = LoggerFactory.getLogger(MailClient.class);
    private String sender;
    private String senderPassword;
    private Properties smtpProperties = new Properties();
    private Properties imapProperties = new Properties();

    public MailClient() {
        smtpProperties.setProperty("mail.smtp.host", "smtp.yandex.ru");
        smtpProperties.setProperty("mail.smtp.port", "465");
        smtpProperties.setProperty("mail.smtp.auth", "true");
        smtpProperties.setProperty("mail.smtp.ssl.enable", "true");
        smtpProperties.setProperty("mail.smtp.socketFactory.port", "465");
        smtpProperties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        imapProperties.setProperty("mail.imap.host", "imap.yandex.ru");
        imapProperties.setProperty("mail.imap.port", "993");
        imapProperties.setProperty("mail.imap.auth", "yes");
        imapProperties.setProperty("mail.imap.ssl.enable", "true");
        imapProperties.setProperty("mail.imap.socketFactory.port", "993");
        imapProperties.setProperty("mail.imap.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    }

    public MailClient asUser(String sender, String senderPassword) {
        this.sender = sender;
        this.senderPassword = senderPassword;
        return this;
    }

    private MimeMessage composeMail(Session session, String recipient, String subj, String msgBody) {
        MimeMessage message = new MimeMessage(session);
        log.debug("Composing an email FROM {} with passw = {} Tо {}, message Subject = {} and Body = {}", sender,
                senderPassword, recipient, subj, msgBody);
        try {
            message.setFrom(new InternetAddress(sender));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subj);
            message.setText(msgBody);
            log.debug("Email is composed.");
        } catch (MessagingException e) {
            log.error("Failed to compose email.{}", e.getLocalizedMessage());
        }
        return message;
    }

    public void sendEmail(String recipient, String subj, String msgBody) {
        Session smtpSession = Session.getInstance(smtpProperties);
        MimeMessage messageToSend = composeMail(smtpSession, recipient, subj, msgBody);
        try {
            log.debug("Trying to send email via SMTP.");
            Transport.send(messageToSend, sender, senderPassword);
            log.debug("Email is sent successfully.");
        } catch (MessagingException e) {
            log.error("Failed to send email.{}", e.getLocalizedMessage());
        }

        //put message to SENT directory. Workaround as yandex doesn't save email sent o_O
        Session imapSession = Session.getInstance(imapProperties);
        MimeMessage messageInSent = composeMail(imapSession, recipient, subj, msgBody);

        try {
            log.debug("Trying to put email into SENT folder (IMAP).");
            Store store = imapSession.getStore("imap");
            store.connect(sender, senderPassword);
            Folder inbox = store.getFolder("Отправленные");
            inbox.open(Folder.READ_WRITE);
            Message[] messages = new Message[1];
            messages[0] = messageInSent;
            inbox.appendMessages(messages);

            inbox.close(false);
            store.close();
            log.debug("Email is put successfully.");
        } catch (MessagingException e) {
            log.error("Failed to put email into SENT folder. {}", e.getLocalizedMessage());
        }
    }
}
