package com.manyvids.parser.selenium.page;

import com.manyvids.parser.selenium.WebElementWithDelay;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import java.awt.*;
import java.awt.event.InputEvent;

import static com.manyvids.parser.util.RandomUtil.getRandom;
import static com.manyvids.parser.util.RandomUtil.getRandomDelay;
import static com.manyvids.parser.util.WaitConditionUtil.waitUntil;


public class LoginPage extends AbstractPage {

    private final static String PAGE_URN = "Login/";
    private int repeats = 2;


    public LoginPage(final WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver, this);
    }

    public void loginOnSite(final String login, final String pass) {
        open();
        getLoginInput().sendKeys(login);
        getPassInput().sendKeys(pass);

        fakeMouseMove(login);
        waitForPageLoad();
    }

    @SneakyThrows
    private void fakeMouseMove(final String login) {
        final WebElement we = getSingInBtn().getElement();

        final Robot robot = new Robot();
        robot.setAutoDelay(100);
        getRandomDelay();

        final JavascriptExecutor js = (JavascriptExecutor) driver;
        final Long x = (Long) js.executeScript(
            "var rect = arguments[0].getBoundingClientRect();" +
            "return rect.left + window.screenX;", we);

        final Long y = (Long) js.executeScript(
            "var rect = arguments[0].getBoundingClientRect();" +
            "return rect.top + window.screenY;", we);

        robot.mouseMove(x.intValue() + 20 + getRandom(5, we.getSize().getWidth() - 5),
                        y.intValue() + 160 + getRandom(5, we.getSize().getHeight()) - 5);
        getRandomDelay();
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        getRandomDelay();
        try {
            waitUntil(() -> !driver.findElements(
                    By.xpath(String.format("//p[contains(@class, 'ProfileAboutMeUI') and text() = '%s']",
                                           login)))
                .isEmpty());
        } catch (final Exception e) {
            if (!driver.findElements(By.xpath("//div[@data-testid = 'close-icon']")).isEmpty() &&
                repeats > 0) {
                repeats--;
                getRandomDelay();
                driver.findElement(By.xpath("//div[@data-testid = 'close-icon']//svg")).click();
                getRandomDelay();
                fakeMouseMove(login);
            } else {
                robot.mousePress(InputEvent.BUTTON3_MASK);
                robot.mouseRelease(InputEvent.BUTTON3_MASK);
                throw e;
            }
        }

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
