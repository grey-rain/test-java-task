import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailClient {
    private Logger log = LoggerFactory.getLogger(MailClient.class);
    private String emailAddress;
    private String password;
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

    public MailClient asUser(String emailAdress, String password) {
        this.emailAddress = emailAdress;
        this.password = password;
        return this;
    }

    private MimeMessage composeMail(Session session, String recipient, String subj, String msgBody) {
        MimeMessage message = new MimeMessage(session);
        log.debug("Composing an email FROM {} with passw = {} Tо {}, message Subject = {} and Body = {}", emailAddress,
                password, recipient, subj, msgBody);
        try {
            message.setFrom(new InternetAddress(emailAddress));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subj);
            message.setText(msgBody);
            log.debug("Success.");
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
            Transport.send(messageToSend, emailAddress, password);
            log.debug("Success.");
        } catch (MessagingException e) {
            log.error("Failed to send email.{}", e.getLocalizedMessage());
        }
        //put message to SENT directory. Workaround as yandex doesn't save email sent o_O
        Session imapSession = Session.getInstance(imapProperties);
        MimeMessage messageInSent = composeMail(imapSession, recipient, subj, msgBody);
        try {
            log.debug("Trying to put email into SENT folder (IMAP).");
            Store store = imapSession.getStore("imap");
            store.connect(emailAddress, password);
            Folder inbox = store.getFolder("Отправленные");
            inbox.open(Folder.READ_WRITE);
            Message[] messages = new Message[1];
            messages[0] = messageInSent;
            inbox.appendMessages(messages);
            inbox.close(false);
            store.close();
            log.debug("Success.");
        } catch (MessagingException e) {
            log.error("Failed to put email into SENT folder. {}", e.getLocalizedMessage());
        }
    }

    public void emptyMailFolder(String folder) {
        log.debug("Trying to get emails from {} folder as {} with password = {}.", folder, emailAddress, password);
        Session imapSession = Session.getInstance(imapProperties);
        try {
            Store store = imapSession.getStore("imap");
            store.connect(emailAddress, password);
            Folder storeFolder = store.getFolder(folder);
            storeFolder.open(Folder.READ_WRITE);
            log.debug("Success.");

            log.debug("Trying to delete emails from {} folder.", folder);
            Message[] messages = storeFolder.getMessages();
            for (Message message : messages) {
                message.setFlag(Flags.Flag.DELETED, true);
            }
            storeFolder.close(true);
            store.close();
            log.debug("Success.");
        } catch (MessagingException e) {
            log.error("Failed to delete emails. {}", e.getLocalizedMessage());
        }
    }
}
