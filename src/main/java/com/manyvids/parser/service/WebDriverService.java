package com.manyvids.parser.service;


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

    private static WebDriver driver;
    private static WebDriverWait wait;
    static final int WAIT_TIME_DEFAULT = 10;

    public static final String APP_URL = "https://www.manyvids.com/";

    private static String activeProfile = "";

    @Value("${spring.profiles.active}")
    public void setActiveProfile(final String profiles) {
        activeProfile = profiles;
    }

    public static WebDriver getDriver() {
        if (driver == null) {
            setupWebDriver();
        }
        return driver;
    }

    public static void reloadSession() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    private static void setupWebDriver() {
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
        System.setProperty("java.awt.headless", "true");

        final ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.EAGER);

//        if (activeProfile.equals("prod")) {
            options.addArguments("--headless");
//        }
        System.out.println("activeProfile = " + activeProfile);

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
