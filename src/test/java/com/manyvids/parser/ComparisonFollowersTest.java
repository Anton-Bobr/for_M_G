package com.manyvids.parser;

import com.manyvids.parser.selenium.page.LoginPage;
import com.manyvids.parser.selenium.page.UserPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

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
        loginPage.loginOnSite(user, pass);

        userPage.goToFollowers();

        final List<String> followers = userPage.getAllFollowers();
        final List<String> following = userPage.getAllFollowing();

        subscriberService.compareFollowersAnsSaveNew(followers, following);
    }
}
