package com.manyvids.parser.service;


import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

@Service
public class WebDriverService {

    private WebDriver driver;
    private WebDriverWait wait;
    static final int WAIT_TIME_DEFAULT = 10;

    public static final String APP_URL = "https://www.manyvids.com/";

    public WebDriver getDriver() {
        if (driver == null) {
            setupWebDriver();
        }
        return driver;
    }

    public void reloadSession() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private void setupWebDriver() {
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        System.setProperty("java.awt.headless", "false");

        final ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

//        if (appProfile.equals("gitlab")) {
//            options.addArguments("--headless");
//        }

        final LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.PERFORMANCE, Level.ALL);
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logPrefs);

        options.addArguments("enable-automation");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-browser-side-navigation");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-site-isolation-trials");
        options.addArguments("--disable-web-security");
        options.addArguments("--hide-scrollbars");

        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(WAIT_TIME_DEFAULT));
        driver.manage().timeouts().implicitlyWait(WAIT_TIME_DEFAULT, TimeUnit.SECONDS);
    }
}
