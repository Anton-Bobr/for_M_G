package com.manyvids.parser;

import com.manyvids.parser.selenium.page.LoginPage;
import com.manyvids.parser.selenium.page.UserPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Tag("ComparisonFollowers")
public class ComparisonFollowersTest extends AbstractTestCases {

    @BeforeEach
    public void init() {
        loginPage = new LoginPage(getDriver());
        userPage = new UserPage(getDriver());
    }

    @Test
    void contextLoads() {

        final List<String> followers = new ArrayList<>();
        final List<String> following = new ArrayList<>();
        int subscribedUsers = 0;
        try {
            loginPage.loginOnSite(user, pass);

            userPage.goToFollowers();

            followers.addAll(userPage.getAllFollowers());
            following.addAll(userPage.getAllFollowing());
            subscribedUsers = subscriberService.compareFollowersAnsSaveNew(followers, following);
        } finally {
            parsingLogService.saveComparisonFollowersLog(followers.size(),
                                                         following.size(),
                                                         subscribedUsers);
        }
    }
}
