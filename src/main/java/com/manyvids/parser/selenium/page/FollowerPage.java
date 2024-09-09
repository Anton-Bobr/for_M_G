package com.manyvids.parser.selenium.page;

import com.manyvids.parser.selenium.WebElementWithDelay;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

//@Slf4j
public class FollowerPage extends AbstractPage {

    public final static String PAGE_URN = "Activity/%s/%s/Store/club/";
    public final static String FOLLOWING_BUTTON_XPATH = "//button[text()='FOLLOWING']";

    public FollowerPage(final WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver, this);
    }

    public String subscribeToUser(final String followerName) {
        if (!driver.getCurrentUrl().contains(followerName)) {
            System.out.println("followerName: " + followerName + " page FAILED");
            return null;
        }
        try {
            getFollowBtn().click();
        } catch (final ElementClickInterceptedException e) {
            closePopupWindow();
            getFollowBtn().click();
        }
        final String[] arg = driver.getCurrentUrl().split("/");
        return arg[arg.length - 2];
    }

    private void closePopupWindow() {
        if (isMarketingPopupWindowsExist()) {
            getMarketingPopupWindowsCloseButton().click();
        }
    }

    public WebElementWithDelay getMarketingPopupWindowsCloseButton() {
        return new WebElementWithDelay(driver.findElement(By.xpath("//button[@data-testid='pop-up-close-button']")));
    }

    public boolean isMarketingPopupWindowsExist() {
        return !driver.findElements(By.xpath("//div[@data-testid='marketing-pop-up-container']")).isEmpty();
    }

    public WebElementWithDelay getFollowBtn() {
        return new WebElementWithDelay(driver.findElement(By.xpath("//button[text()='FOLLOW']")));
    }

    public WebElementWithDelay getUnFollowBtn() {
        return new WebElementWithDelay(driver.findElement(By.xpath(FOLLOWING_BUTTON_XPATH)));
    }

    @Override
    protected String getPageUrn() {
        return PAGE_URN;
    }

    public boolean checkIsUserSubscribed() {
        return !driver.findElements(By.xpath(FOLLOWING_BUTTON_XPATH)).isEmpty();
    }

    public void unsubscribed() {
        getUnFollowBtn().click();
    }
}
