import dto.EmailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.MailboxPage;
import test.data.EmailTestDataProvider;

import static org.testng.Assert.assertTrue;

public class EmailTest extends BaseTest {
    private LoginPage loginPage;
    private MailboxPage mailboxPage;
    private Logger log = LoggerFactory.getLogger(EmailTest.class);

    String sender;
    String senderPassword;
    String recipient;
    String recipientPassword;
    MailClient mailClient = new MailClient();

    @BeforeMethod
    public void initPages() {
        loginPage = new LoginPage(getDriver());
        mailboxPage = new MailboxPage(getDriver());
    }

    @Test(dataProvider = "csvDataProvider", dataProviderClass = EmailTestDataProvider.class)
    public void testEmail(String sender, String senderPassword, String subj, String msgBody, String recipient,
            String recipientPassword) {
        this.sender = sender;
        this.senderPassword = senderPassword;
        this.recipient = recipient;
        this.recipientPassword = recipientPassword;

        //Step1. Send email
        mailClient.asUser(sender, senderPassword).sendEmail(recipient, subj, msgBody);
        EmailDto emailDto = new EmailDto();
        emailDto.setRelatedAddress(recipient);
        emailDto.setSubj(subj);
        emailDto.setMsgBody(msgBody);

        //Step2. Check email sent Selenium - box 1
        log.debug("Getting into sender SENT box.");
        loginPage.login(sender, senderPassword);
        mailboxPage.openSentBox();
        log.debug("Success. Searching for mail with To = {}, Subj = {}, Body = {}", emailDto.getRelatedAddress(),
                emailDto.getSubj(), emailDto.getMsgBody());
        assertTrue(mailboxPage.getMails().contains(emailDto), "Email is not found in Sent folder.");

        //Step3. Check email received Selenium - box 2
        loginPage.logout();
        log.debug("Getting into recipient INBOX box.");
        loginPage.login(recipient, recipientPassword);
        emailDto.setRelatedAddress(sender);
        log.debug("Success. Searching for mail with From = {}, Subj = {}, Body = {}", emailDto.getRelatedAddress(),
                emailDto.getSubj(), emailDto.getMsgBody());
        assertTrue(mailboxPage.getMails().contains(emailDto), "Email is not found in Inbox folder.");
    }

    @AfterMethod
    public void deleteMail() {
        mailClient.asUser(sender, senderPassword).emptyMailFolder("Отправленные");
        mailClient.asUser(recipient, recipientPassword).emptyMailFolder("INBOX");
    }
}
