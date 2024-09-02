package com.manyvids.parser;

import com.manyvids.parser.selenium.page.CreatorPage;
import com.manyvids.parser.selenium.page.FollowerPage;
import com.manyvids.parser.selenium.page.LoginPage;
import com.manyvids.parser.selenium.page.MainPage;
import com.manyvids.parser.selenium.page.UserPage;
import com.manyvids.parser.service.SubscriberService;
import com.manyvids.parser.service.WebDriverService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class AbstractTestCases {

    @Value("${app.settings.username}")
    protected String user;
    @Value("${app.settings.password}")
    protected String pass;

    @Autowired
    private WebDriverService driverService;
    @Autowired
    protected SubscriberService subscriberService;

    protected LoginPage loginPage;
    protected MainPage mainPage;
    protected CreatorPage creatorPage;
    protected UserPage userPage;
    protected FollowerPage followerPage;

    public WebDriver getDriver() {
        return driverService.getDriver();
    }

    @AfterEach
    public void destroyDriver() {
        driverService.reloadSession();
    }
}
