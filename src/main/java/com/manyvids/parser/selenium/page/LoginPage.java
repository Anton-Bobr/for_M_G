package com.manyvids.parser.selenium.page;

import com.manyvids.parser.selenium.WebElementWithDelay;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import static com.manyvids.parser.util.WaitConditionUtil.waitUntil;


public class LoginPage extends AbstractPage {

    private final static String PAGE_URN = "Login/";

    public LoginPage(final WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver, this);
    }

    public void loginOnSite(final String login, final String pass) {
        open();
        getLoginInput().sendKeys(login);
        getPassInput().sendKeys(pass);
        getSingInBtn().click();
        waitUntil(() -> !driver.findElements(
                By.xpath(String.format("//p[contains(@class, 'ProfileAboutMeUI') and text() = '%s']",
                                       login)))
            .isEmpty());
        waitForPageLoad();
    }

    @Override
    protected String getPageUrn() {
        return PAGE_URN;
    }

    public WebElementWithDelay getLoginInput() {
        return new WebElementWithDelay(driver.findElement(By.xpath("//input[@name='userName']")));
    }

    public WebElementWithDelay getPassInput() {
        return new WebElementWithDelay(driver.findElement(By.xpath("//input[@name='password']")));
    }

    public WebElementWithDelay getSingInBtn() {
        return new WebElementWithDelay(driver.findElement(By.xpath("//button[@type='submit']")));
    }
}
