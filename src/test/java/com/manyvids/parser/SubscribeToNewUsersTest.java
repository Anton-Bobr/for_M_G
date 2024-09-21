//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.manyvids.parser;

import com.manyvids.parser.enums.ContentTypeEnum;
import com.manyvids.parser.selenium.WebElementWithDelay;
import com.manyvids.parser.selenium.page.CreatorPage;
import com.manyvids.parser.selenium.page.FollowerPage;
import com.manyvids.parser.selenium.page.LoginPage;
import com.manyvids.parser.selenium.page.MainPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

//@Slf4j
@Tag("SubscribeToNewUsers")
class SubscribeToNewUsersTest extends AbstractTestCases {

    @BeforeEach
    public void init() {
//        loginPage = new LoginPage(getDriver());
//        mainPage = new MainPage(getDriver());
//        creatorPage = new CreatorPage(getDriver());
//        followerPage = new FollowerPage(getDriver());
    }

    @Test
    void subscribeToNewUsers() {
        // Указание пути к ChromeDriver
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");

        // Настройка опций для Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu"); // если требуется

//        options.addArguments("--headless");

        // Запуск браузера Chrome через Selenium
        WebDriver driver = new ChromeDriver(options);
        driver.get("https://www.google.com");

        System.out.println("Title: " + driver.getTitle());

        driver.quit();





//        loginPage.loginOnSite(user, pass);
//        mainPage.goToCreators();
//        mainPage.selectContentType(ContentTypeEnum.WOMEN_CREATORS);
//        final List<WebElementWithDelay> creatorElementsList = mainPage.getCreatorElementsList();
//        creatorElementsList.forEach(this::subscribeToAllCreatorsFollowers);
    }

    private void subscribeToAllCreatorsFollowers(final WebElementWithDelay creatorElement) {
        creatorElement.sendKeys(new CharSequence[]{Keys.CONTROL, Keys.RETURN});
        goToLastBrowserTab();
        creatorPage.goToFollowers();
        final List<WebElementWithDelay> followersList = creatorPage.getFollowersList();
        followersList.forEach(this::subscribeIfNeed);
        getDriver().close();
        goToLastBrowserTab();
    }

    private void subscribeIfNeed(final WebElementWithDelay followerElement) {
        final String followerName = creatorPage.mapFollowerCardToNames(followerElement);
        System.out.println("process followerName: " + followerName);
        final boolean isFollowerAlreadySubscribed = subscriberService.isFollowerExist(followerName);
        if (!isFollowerAlreadySubscribed) {
            creatorPage.openFollowerInNewTab(followerElement);
            goToLastBrowserTab();
            final String userId = followerPage.subscribeToUser(followerName);
            if (userId != null) {
                subscriberService.createAndSaveNewSubscriber(followerName, userId);
                System.out.println("followerName: " + followerName + " followerId: " + userId + " SAVED");
            }
            getDriver().close();
            goToLastBrowserTab();
        }
    }

    private void goToLastBrowserTab() {
        final ArrayList<String> tabs = new ArrayList(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.getLast());
        creatorPage.waitForPageLoad();
    }
}
