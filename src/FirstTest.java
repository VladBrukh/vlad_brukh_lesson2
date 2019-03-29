import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;

public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception
    {
        DesiredCapabilities capabilities = new DesiredCapabilities();

        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("deviceName", "AndroidTestDevice");
        capabilities.setCapability("platformVersion", "8.0");
        capabilities.setCapability("automationName", "Appium");
        capabilities.setCapability("appPackage", "org.wikipedia");
        capabilities.setCapability("appActivity", ".main.MainActivity");
        capabilities.setCapability("app", "C:/Users/1/Documents/GitHub/vlad_brukh_lesson2/apks/org.wikipedia.apk");

        driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
    }

    @After
    public void tearDown()
    {
        driver.quit();
    }

    @Test
    public void searchResults()
    {
        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                "Dracula",
                "Cannot find search input",
                15
        );

        waitForElementHasWord(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_container']//*[@text='Dracula']"),
                "Dracula",
                "The article with 'Dracula' title does not exist",
                15
        );

        waitForElementHasWord(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_container']//*[@text='Dracula Untold']"),
                "Dracula",
                "The article with 'Dracula Untold' title does not exist",
                15
        );

        waitForElementHasWord(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_container']//*[@text='Dracula in popular culture']"),
                "Dracula",
                "The article with 'Dracula in popular culture' title does not exist",
                15
        );

        waitForElementHasWord(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_container']//*[@text='Dracula (1931 English-language film)']"),
                "Dracula",
                "The article with 'Dracula (1931 English-language film)' title does not exist",
                15
        );

        waitForElementHasWord(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_container']//*[@text='Dracula (1958 film)']"),
                "Dracula",
                "The article with 'Dracula (1958 film)' title does not exist",
                15
        );

        waitForElementHasWord(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_container']//*[@text='Dracula (1924 play)']"),
                "Dracula",
                "The article with 'Dracula (1924 play)' title does not exist",
                15
        );

        waitForElementHasWord(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_container']//*[@text='Dracula (Marvel Comics)']"),
                "Dracula",
                "The article with 'Dracula (Marvel Comics)' title does not exist",
                15
        );


    }

    private WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds)
    {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    private void waitForElementHasWord(By by, String word, String error_message, long timeoutInSeconds)
    {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        String article = element.getAttribute("text");
        Assert.assertTrue("The article " + article + " does not contain word " + word,article.contains(word));
    }

}
