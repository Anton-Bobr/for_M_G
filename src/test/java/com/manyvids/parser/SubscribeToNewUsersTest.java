package com.manyvids.parser;

import com.manyvids.parser.entity.ParsingLogEntity;
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

import javax.naming.LimitExceededException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.manyvids.parser.util.RandomUtil.SUBSCRIPTIONS_PER_DAY_MAX;
import static com.manyvids.parser.util.RandomUtil.SUBSCRIPTIONS_PER_DAY_MIN;
import static com.manyvids.parser.util.RandomUtil.getRandom;

//@Slf4j
@Tag("SubscribeToNewUsers")
class SubscribeToNewUsersTest extends AbstractTestCases {

//    @Value("${app.settings.max-number-of-tries}")
//    protected Integer maxNumberOfTries;

    protected Integer actualNumberOfTries;
    protected Integer maxNumberOfSubscriptionsToday = 0;
    protected int actualNumberOfSubscriptionsToday = 0;

    @BeforeEach
    public void init() {
        loginPage = new LoginPage(getDriver());
        mainPage = new MainPage(getDriver());
        creatorPage = new CreatorPage(getDriver());
        followerPage = new FollowerPage(getDriver());
    }

    @Test
    void subscribeToNewUsers() {
        final ParsingLogEntity logEntity = parsingLogService.getSubscribeToNewUsersLog();
        if (isCanSubscribeMoreUsersToday(logEntity)) {
            try {
                loginPage.loginOnSite(user, pass);
                mainPage.goToCreators();
                mainPage.selectContentType(ContentTypeEnum.WOMEN_CREATORS);
                final List<WebElementWithDelay> creatorElementsList = mainPage.getCreatorElementsList();
                for (final WebElementWithDelay webElementWithDelay : creatorElementsList) {
                    subscribeToAllCreatorsFollowers(webElementWithDelay);
                }
            } catch (final LimitExceededException ignored) {
                System.out.println(ignored.getMessage());
                logResult();
            } finally {
                parsingLogService.updateSubscribeToNewUsersLog(logEntity,
                                                               maxNumberOfSubscriptionsToday,
                                                               actualNumberOfSubscriptionsToday,
                                                               actualNumberOfTries);
            }
        } else {
            System.out.println("Limit for Subscribe or tries today reached.");
            logResult();
        }
    }

    private void logResult() {
        System.out.println("maxNumberOfSubscriptionsToday = " + maxNumberOfSubscriptionsToday);
        System.out.println("actualNumberOfSubscriptionsToday = " + actualNumberOfSubscriptionsToday);
        System.out.println("actualNumberOfTries = " + actualNumberOfTries);
    }

    private boolean isCanSubscribeMoreUsersToday(final ParsingLogEntity logEntity) {
        if (logEntity.getId() == null) {
            maxNumberOfSubscriptionsToday = getRandom(SUBSCRIPTIONS_PER_DAY_MIN,
                                                      SUBSCRIPTIONS_PER_DAY_MAX);
            actualNumberOfTries = 0;
            return true;
        }
        final HashMap<String, Object> map = parsingLogService.convertJsonToMap(logEntity.getData());
        actualNumberOfSubscriptionsToday = (Integer) map.get("actualNumberOfSubscriptionsToday");
        actualNumberOfTries = (Integer) map.get("actualNumberOfTries");
        maxNumberOfSubscriptionsToday = (Integer) map.get("maxNumberOfSubscriptionsToday");
//        return (Integer) map.get("actualNumberOfTries") < maxNumberOfTries &&
        return (Integer) map.get("maxNumberOfSubscriptionsToday") > actualNumberOfSubscriptionsToday;
    }

    private void subscribeToAllCreatorsFollowers(final WebElementWithDelay creatorElement)
        throws LimitExceededException {
        creatorElement.sendKeys(new CharSequence[]{Keys.CONTROL, Keys.RETURN});
        goToLastBrowserTab();
        creatorPage.goToFollowers();
        final List<WebElementWithDelay> followersList = creatorPage.getFollowersList();
        for (final WebElementWithDelay webElementWithDelay : followersList) {
            subscribeIfNeed(webElementWithDelay);
        }
        getDriver().close();
        goToLastBrowserTab();
    }

    private void subscribeIfNeed(final WebElementWithDelay followerElement) throws LimitExceededException {
        if (actualNumberOfSubscriptionsToday < maxNumberOfSubscriptionsToday) {
            final String followerName = creatorPage.mapFollowerCardToNames(followerElement);
            System.out.println("process followerName: " + followerName);
            final boolean isFollowerAlreadySubscribed = subscriberService.isFollowerExist(followerName);
            if (!isFollowerAlreadySubscribed) {
                creatorPage.openFollowerInNewTab(followerElement);
                goToLastBrowserTab();
                final String userId = followerPage.subscribeToUser(followerName);
                if (userId != null) {
                    subscriberService.createAndSaveNewSubscriber(followerName, userId);
                    actualNumberOfSubscriptionsToday++;
                    System.out.println("followerName: " + followerName + " followerId: " + userId + " SAVED");
                }
                getDriver().close();
                goToLastBrowserTab();
            }
        } else {
            throw new LimitExceededException("Limit for Subscribe today reached.");
        }
    }

    private void goToLastBrowserTab() {
        final ArrayList<String> tabs = new ArrayList(getDriver().getWindowHandles());
        getDriver().switchTo().window(tabs.getLast());
        creatorPage.waitForPageLoad();
    }

    @Override
    protected void reInitVariables() {
        final String minNumberUnsubscribingEnv = System.getenv("MIN_NUMBER_FOR_DAILY_SUBSCRIBING");
        final String maxNumberUnsubscribingEnv = System.getenv("MAX_NUMBER_FOR_DAILY_SUBSCRIBING");
        if (minNumberUnsubscribingEnv != null &&
            maxNumberUnsubscribingEnv != null &&
            Integer.parseInt(maxNumberUnsubscribingEnv) >= Integer.parseInt(minNumberUnsubscribingEnv)) {
            SUBSCRIPTIONS_PER_DAY_MIN = Integer.parseInt(minNumberUnsubscribingEnv);
            SUBSCRIPTIONS_PER_DAY_MAX = Integer.parseInt(maxNumberUnsubscribingEnv);
        }
        super.reInitVariables();
    }
}
