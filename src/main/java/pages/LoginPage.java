package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends AbstractPage {
    @FindBy(className = "_nb-input-controller")
    private WebElement username;

    @FindBy(id = "nb-2")
    private WebElement password;

    @FindBy(className = "_nb-action-button")
    private WebElement loginButon;

    @FindBy(className = "js-user-picture")
    private WebElement userIcon;

    @FindBy(xpath = ".//a[@data-metric='Меню сервисов:Выход']")
    private WebElement logout;

    private static final String URL = "https://mail.yandex.by/";

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        getDriver().get(URL);
        this.username.clear();
        this.username.sendKeys(username);
        this.password.sendKeys(password);
        loginButon.click();
    }

    public void logout() {
        userIcon.click();
        logout.click();
        //we need to make sure that user is completely logged out
        (new WebDriverWait(getDriver(), 10))
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='home-logo__default']")));
    }
}
