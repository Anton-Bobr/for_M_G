package com.manyvids.parser.selenium;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.openqa.selenium.WebElement;

import static com.manyvids.parser.util.RandomUtil.getRandomDelay;

@Getter
@AllArgsConstructor
public class WebElementWithDelay {

    private final WebElement element;

    public void clear() {
        getRandomDelay();
        element.clear();
        getRandomDelay();
    }

    public void click() {
        getRandomDelay();
        element.click();
        getRandomDelay();

    }

    public void sendKeys(final CharSequence... keysToSend) {
        getRandomDelay();
        element.sendKeys(keysToSend);
        getRandomDelay();
    }
}
