package com.manyvids.parser.selenium.page;

import com.manyvids.parser.selenium.WebElementWithDelay;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;

import java.awt.*;
import java.awt.event.KeyEvent;

import static com.manyvids.parser.util.RandomUtil.getRandomDelay;
import static java.lang.Thread.sleep;


public class CreatorPage extends AbstractPage {

    private final static String PAGE_URN = "Profile/%s/%s/Store/Videos/";

    public CreatorPage(final WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver, this);
    }

    @SneakyThrows
    public void goToFollowers() {
        getFollowersBtn().click();
        setFilter("MEMBER");
        sleep(5000);
    }

    public WebElementWithDelay getFollowersBtn() {
        return new WebElementWithDelay(driver.findElement(By.xpath("//a[text()=' Followers']")));
    }

    public WebElementWithDelay getMembersBtn() {
        return new WebElementWithDelay(driver.findElement(By.xpath("//label[text()=' Members']")));
    }

    @Override
    protected String getPageUrn() {
        return PAGE_URN;
    }

    @SneakyThrows
    public void openFollowerInNewTab(final WebElementWithDelay followerElement) {
        final SearchContext shadowRoot =
            followerElement.getElement().findElement(By.xpath(".//kwc-user-info")).getShadowRoot();
        final WebElement usernameLabel = shadowRoot.findElement(By.cssSelector("span.card-name"));
        final Actions action = new Actions(driver);
        getRandomDelay();
        action.contextClick(usernameLabel).perform();
        final Robot robot = new Robot();
        robot.setAutoDelay(100);
        getRandomDelay();
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        getRandomDelay();
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        getRandomDelay();
    }
}
