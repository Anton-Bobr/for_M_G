package com.manyvids.parser;

import com.manyvids.parser.entity.SubscriberEntity;
import com.manyvids.parser.selenium.page.FollowerPage;
import com.manyvids.parser.selenium.page.LoginPage;
import com.manyvids.parser.selenium.page.UserPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Tag("Unsubscribing")
public class UnsubscribingTest extends AbstractTestCases {

    @Value("${app.settings.number-of-days-for-unsubscribe}")
    protected int numberOfDaysForUnsubscribe;

    @Override
    @BeforeEach
    public void init() {
        loginPage = new LoginPage(getDriver());
        userPage = new UserPage(getDriver());
        followerPage = new FollowerPage(getDriver());
    }

    @Test
    void unsubscribing() {
        final List<SubscriberEntity> usersForUnsubscribing =
            subscriberService.getUsersWhoHaveNotSubscribedXdDays(numberOfDaysForUnsubscribe);

        if (!usersForUnsubscribing.isEmpty()) {
            loginPage.loginOnSite(user, pass);
            usersForUnsubscribing.forEach(user -> unsubscribe(user));
        }
    }

    private void unsubscribe(final SubscriberEntity user) {
        followerPage.open(String.format(followerPage.PAGE_URN, user.getUserName(), user.getUserId()));
        final boolean isUserSubscribed = followerPage.checkIsUserSubscribed();
        if (isUserSubscribed) {
            followerPage.unsubscribed();
            subscriberService.updateUserAsUnsubscribed(user);
            System.out.println("We unsubscribe from the username <" + user.getUserName() + ">");
        } else {
            System.err.println("We weren't subscribed to the username <" + user.getUserName() + ">");
        }
    }

    @Override
    protected void reInitVariables() {
        final String numberOfDaysForUnsubscribeEnv = System.getenv("NUMBER_OF_DAYS_FOR_UNSUBSCRIBE");
        if (numberOfDaysForUnsubscribeEnv != null) {
            numberOfDaysForUnsubscribe = Integer.parseInt(numberOfDaysForUnsubscribeEnv);
        }
        super.reInitVariables();
    }
}
