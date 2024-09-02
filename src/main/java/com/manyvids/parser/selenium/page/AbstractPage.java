package com.manyvids.parser.selenium.page;

import com.manyvids.parser.selenium.WebElementWithDelay;
import io.qameta.allure.Step;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static com.manyvids.parser.service.WebDriverService.APP_URL;
import static com.manyvids.parser.util.WaitConditionUtil.waitUntil;

public abstract class AbstractPage {

    public static final Dimension PAGE_DIMENSION = new Dimension(1366, 768);

    private final static String PAGE_URN = "";

    protected WebDriver driver;

    public AbstractPage(final WebDriver driver) {
        this.driver = driver;
    }

    public void open(final Dimension d) {
        open(getPageUrn(),
             d);
    }

    public void open() {
        open(getPageUrn(),
             PAGE_DIMENSION);
    }

    @Step("Открываю страницу {urlAppend} в разрешении {dimension}")
    private void open(final String urlAppend,
                      final Dimension dimension) {
        System.out.println("Opening " + urlAppend);
        driver.manage().window().setSize(dimension);
        driver.get(APP_URL + urlAppend);
        waitForPageLoad();
    }


    public void waitForPageLoad() {
        System.out.println("Waiting for page to finish loading");
        waitUntil(() -> (((JavascriptExecutor) driver).executeScript("return document.readyState")).equals("complete"),
                  20);
    }

    public void waitElementLoad(final WebElement e) {
        waitElement(e, true);
    }

    public void waitElementClose(final WebElement e) {
        waitElement(e, false);
    }

    public void waitElement(final WebElement e, final boolean isElementMustPresent) {
        waitUntil(() -> {
            try {
                e.isDisplayed();
            } catch (final Exception exc) {
                return !isElementMustPresent;
            }
            return isElementMustPresent;
        }, 60);
    }

    public List<WebElementWithDelay> getFollowersList() {
        return driver.findElements(By.xpath("//div[contains(@class,'soc-follow-card')]")).stream()
            .map(WebElementWithDelay::new).toList();
    }

    public WebElementWithDelay getFollowerByNumber(final int i) {
        return new WebElementWithDelay(
            driver.findElement(By.xpath("//div[contains(@class,'soc-follow-card')][" + i + "]")));
    }

    public int getFollowersNumber() {
        return driver.findElements(By.xpath("//div[contains(@class,'soc-follow-card')]")).size();
    }

    protected void setFilter(final String val) {
        new WebElementWithDelay(
            driver.findElement(By.xpath(String.format("//form[@class='user-type-filter']//input[@value='%s']",
                                                      val))))
            .click();
    }

    protected String getPageUrn() {
        return PAGE_URN;
    }

    public String mapFollowerCardToNames(final WebElementWithDelay we) {
        return we.getElement()
            .findElement(By.xpath(".//kwc-user-info"))
            .getAttribute("handle");
    }
}
