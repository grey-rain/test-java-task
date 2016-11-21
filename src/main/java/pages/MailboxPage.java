package pages;

import dto.EmailDto;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.ArrayList;
import java.util.List;

public class MailboxPage extends AbstractPage {

    @FindBy(xpath = "//a[contains(@title,'Отправленные')]")
    private WebElement sentLink;

    @FindBy(xpath = "//div[contains(@class,'mail-MessagesList')]/div")
    private List<WebElement> mailsList;

    public MailboxPage(WebDriver driver) {
        super(driver);
    }

    public void openSentBox() {
        sentLink.click();
    }

    public List<EmailDto> getMails() {
        List<EmailDto> emailDtoList = new ArrayList<EmailDto>();
        for (WebElement element : mailsList) {
            EmailDto emailDto = new EmailDto();
            WebElement sender = element.findElement(By.xpath(".//span[@class='mail-MessageSnippet-FromText']"));
            WebElement subj = element
                    .findElement(By.xpath(".//span[contains(@class,'mail-MessageSnippet-Item_subject')]"));
            WebElement msgBody = element
                    .findElement(By.xpath("//span[contains(@class,'js-message-snippet-firstline')]"));

            emailDto.setRelatedAddress(sender.getText());
            emailDto.setSubj(subj.getText());
            emailDto.setMsgBody(msgBody.getText());
            emailDtoList.add(emailDto);
        }
        return emailDtoList;
    }
}
