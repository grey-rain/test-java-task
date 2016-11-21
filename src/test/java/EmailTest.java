import dto.EmailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.MailboxPage;

import static org.testng.Assert.assertTrue;

public class EmailTest extends BaseTest {
    private LoginPage loginPage;
    private MailboxPage mailboxPage;
    private Logger log = LoggerFactory.getLogger(EmailTest.class);

    String sender = "daria-test2@tut.by";
    String senderPassword = "11111111";
    String subj = "test subj";
    String msgBody = "test body";
    String recipient = "daria-test3@yandex.ru";
    String recipientPassword = "123123qa";
    MailClient mailClient = new MailClient();

    @BeforeTest
    public void initPages() {
        loginPage = new LoginPage(getDriver());
        mailboxPage = new MailboxPage(getDriver());
    }

    @Test
    public void testEmail() {
        //Step1. Send email
        mailClient.asUser(sender, senderPassword).sendEmail(recipient, subj, msgBody);
        EmailDto emailDto = new EmailDto();
        emailDto.setRelatedAddress(recipient);
        emailDto.setSubj(subj);
        emailDto.setMsgBody(msgBody);

        //Step2. Check email sent Selenium - box 1
        log.debug("Receiving emails from SENT box.");
        loginPage.login(sender, senderPassword);
        mailboxPage.openSentBox();
        log.debug("Success. Searching for mail with To = {}, Subj = {}, Body = {}", emailDto.getRelatedAddress(),
                emailDto.getSubj(), emailDto.getMsgBody());
        assertTrue(mailboxPage.getMails().contains(emailDto), "Email is not found in Sent folder.");

        //Step3. Check email received Selenium - box 2
        loginPage.logout();
        log.debug("Receiving emails from INBOX box.");
        loginPage.login(recipient, recipientPassword);
        emailDto.setRelatedAddress(sender);
        log.debug("Success. Searching for mail with From = {}, Subj = {}, Body = {}", emailDto.getRelatedAddress(),
                emailDto.getSubj(), emailDto.getMsgBody());
        assertTrue(mailboxPage.getMails().contains(emailDto), "Email is not found in Inbox folder.");
    }

    @AfterTest
    public void deleteMail() {
        mailClient.asUser(sender, senderPassword).emptyMailFolder("Отправленные");
        mailClient.asUser(recipient, recipientPassword).emptyMailFolder("INBOX");
    }
}
