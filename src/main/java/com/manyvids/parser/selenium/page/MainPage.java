package com.manyvids.parser.selenium.page;

import com.manyvids.parser.enums.ContentTypeEnum;
import com.manyvids.parser.selenium.WebElementWithDelay;
import lombok.SneakyThrows;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static java.lang.Thread.sleep;


public class MainPage extends AbstractPage {

    private final static String PAGE_URN = "";

    public MainPage(final WebDriver driver) {
        super(driver);
        PageFactory.initElements(this.driver, this);
    }

    @Override
    protected String getPageUrn() {
        return PAGE_URN;
    }

    @SneakyThrows
    public void goToCreators() {
        sleep(3000);
        getCreatorsBtn().click();
    }

    public void selectContentType(final ContentTypeEnum creatorType) {
        final WebElementWithDelay creatorTypeBtn = getCreatorTypeBtn(creatorType);
        if (!creatorTypeBtn.getElement().getAttribute("class").contains("SubNavUI_active__nCSCT")) {
            creatorTypeBtn.click();
        }
    }

    public WebElementWithDelay getCreatorsBtn() {
        return new WebElementWithDelay(driver.findElement(
            By.xpath("//div[contains(@class, 'smallSideBar_small_sidebar')]//a[@data-event-label='Creators']")));
    }

    public WebElementWithDelay getCreatorTypeBtn(final ContentTypeEnum creatorType) {
        return new WebElementWithDelay(driver.findElement(By.xpath(String.format("//a[text()='%s']",
                                                                                 creatorType.getName()))));
    }

    public List<WebElementWithDelay> getCreatorElementsList() {
        return driver.findElements(By.xpath("//a[@class='ProfileCardUI_link__UVBT2']"))
            .stream()
            .map(WebElementWithDelay::new)
            .toList();
    }
}
