import org.testng.Assert;
import org.testng.annotations.Test;

public class EmailTest {

    @Test
    public void testEmail() {
        MailClient mailClient = new MailClient();
        mailClient.asUser("daria-test2@tut.by", "11111111")
                .sendEmail("daria-test3@yandex.ru", "test subj", "test body");
        //check email sent Selenium - box 1
        //check email received Selenium - box 2

        Assert.assertNull(null);
    }
}
