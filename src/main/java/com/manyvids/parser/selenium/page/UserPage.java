package com.manyvids.parser.selenium.page;

import com.manyvids.parser.selenium.WebElementWithDelay;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;


public class UserPage extends AbstractPage {

    private final static String PAGE_URN = "Profile/%s/%s/Store/Videos/";

    public UserPage(final WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver, this);
    }

    public void goToFollowers() {
        getFollowingBtn().click();
        waitForPageLoad();
    }


    private WebElementWithDelay getFollowingBtn() {
        return new WebElementWithDelay(driver.findElement(By.xpath("//a[text()=' Following']")));
    }

    public List<String> getAllFollowing() {
        return getAllMembersFromTab(getFollowingTab());
    }

    public List<String> getAllFollowers() {
        return getAllMembersFromTab(getFollowersTab());
    }

    public List<String> getAllMembersFromTab(final WebElementWithDelay tab) {
        final WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        wait.until(ExpectedConditions.elementToBeClickable(tab.getElement()));
        tab.click();
//        setFilter("MEMBER");
        waitForPageLoad();
        return getFollowersList().stream().map(this::mapFollowerCardToNames).toList();
    }

    private WebElementWithDelay getFollowersTab() {
        return new WebElementWithDelay(driver.findElement(By.xpath("//div[text()='followers']")));
    }

    private WebElementWithDelay getFollowingTab() {
        return new WebElementWithDelay(driver.findElement(By.xpath("//div[text()='following']")));
    }

    @Override
    protected String getPageUrn() {
        return PAGE_URN;
    }
}
