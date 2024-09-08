package com.manyvids.parser;

import com.manyvids.parser.service.WebDriverService;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static com.manyvids.parser.service.WebDriverService.reloadSession;

public class TestControl implements TestWatcher {

    @Override
    public void testSuccessful(final ExtensionContext context) {
        reloadSession();
    }

    @Override
    public void testDisabled(final ExtensionContext context, final Optional<String> reason) {
        System.err.println(
            "Test " + context.getTestMethod().orElseThrow().getName() + " disabled because of " + reason);
        reloadSession();
    }

    @Override
    public void testAborted(final ExtensionContext context, final Throwable cause) {
        finishFailedTest(context, cause);
    }

    @Override
    public void testFailed(final ExtensionContext context, final Throwable cause) {
        finishFailedTest(context, cause);
    }

    private void finishFailedTest(final ExtensionContext context,
                                  final Throwable cause) {
        final String testMethod = context.getTestMethod().orElseThrow().getName();
        makeScrnShot(WebDriverService.getDriver(),
                     "build/screenshots/" + testMethod + ".png");
        cause.printStackTrace();
        reloadSession();
    }

    private void makeScrnShot(final WebDriver driver, final String filePath) {
        try {
            final TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            final File srcFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
            final File destFile = new File(filePath);
            FileUtils.copyFile(srcFile, destFile);
            Allure.addAttachment("Screenshot", FileUtils.openInputStream(srcFile));
        } catch (final IOException ignored) {
        }
    }
}
