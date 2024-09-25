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
import java.util.concurrent.atomic.AtomicInteger;

@Tag("Unsubscribing")
public class UnsubscribingTest extends AbstractTestCases {

    @Value("${app.settings.number-of-days-for-unsubscribe}")
    protected int numberOfDaysForUnsubscribe;

    @BeforeEach
    public void initPages() {
        loginPage = new LoginPage(getDriver());
        userPage = new UserPage(getDriver());
        followerPage = new FollowerPage(getDriver());
    }

    @Test
    void unsubscribing() {
        final List<SubscriberEntity> usersForUnsubscribing =
            subscriberService.getUsersWhoHaveNotSubscribedXdDays(numberOfDaysForUnsubscribe);
        final AtomicInteger numberOfUserRealUnsubscribed = new AtomicInteger(0);
        try {
            if (!usersForUnsubscribing.isEmpty()) {
                loginPage.loginOnSite(user, pass);
                usersForUnsubscribing.forEach(user -> {
                    final boolean isUnsubscribed = unsubscribe(user);
                    if (isUnsubscribed) {
                        numberOfUserRealUnsubscribed.getAndIncrement();
                    }
                });
            }
        } finally {
            parsingLogService.saveUnsubscribingLog(numberOfDaysForUnsubscribe,
                                                   usersForUnsubscribing.size(),
                                                   numberOfUserRealUnsubscribed.get());
            if(usersForUnsubscribing.size() != numberOfUserRealUnsubscribed.get()){
                throw new RuntimeException("SOMETHING WRONG");
            }
        }
    }

    private boolean unsubscribe(final SubscriberEntity user) {
        followerPage.open(String.format(followerPage.PAGE_URN, user.getUserName(), user.getUserId()));
        final boolean isUserSubscribed = followerPage.checkIsUserSubscribed();
        if (isUserSubscribed) {
            followerPage.unsubscribed();
            subscriberService.updateUserAsUnsubscribed(user);
            System.out.println("We unsubscribe from the username <" + user.getUserName() + ">");
            return true;
        } else {
            System.err.println("We weren't subscribed to the username <" + user.getUserName() + ">");
            return false;
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
