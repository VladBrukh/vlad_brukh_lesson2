import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URL;
import java.util.List;

public class FirstTest {

    private AppiumDriver driver;

    @Before
    public void setUp() throws Exception {
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
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void changeScreenOrientation() {

        checkScreenOrientation();

        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                5
        );

        String search_word = "Dracula";
        String article_description = "2014 American dark pantasy action horror film directed by Gary Shore";

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_word,
                "Cannot find search input",
                15
        );

        driver.rotate(ScreenOrientation.LANDSCAPE);

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_container']//*[contains(@text, '" + article_description + "')]"),
                "Cannot find article with " + article_description + " text searching by " + search_word,
                25
        );


        waitForElementPresent(
                By.xpath("//*[contains(@text, '" + article_description + "')]"),
                "The article with " + article_description + " is not opened",
                15
        );

    }

    @Test
    public void changeScreenOrientation_two() {

        checkScreenOrientation();

        waitForElementAndClick(
                By.xpath("//*[contains(@text, 'Search Wikipedia')]"),
                "Cannot find 'Search Wikipedia' input",
                25
        );

        String search_word = "Dracula";
        String article_description = "2014 American dark fantasy action horror film directed by Gary Shore";

        waitForElementAndSendKeys(
                By.id("org.wikipedia:id/search_src_text"),
                search_word,
                "Cannot find search input",
                25
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_container']//*[contains(@text, '1897 Gothic horror novel by Irish author Bram Stoker')]"),
                "Cannot find article with '1897 Gothic horror novel by Irish author Bram Stoker' text searching by 'Dracula'",
                25
        );

        waitForElementAndClick(
                By.xpath("//android.widget.ImageView[@content-desc='More options']"),
                "Cannot find a button to open article options",
                25
        );

        waitForElementAndClick(
                By.xpath("//android.widget.LinearLayout//*[@text='Add to reading list']"),
                "Cannot find an option to add the article to reading list",
                25
        );

        waitForElementAndClick(
                By.id("org.wikipedia:id/onboarding_button"),
                "Cannot find 'Got it' tip overlay",
                25
        );

        waitForElementAndClear(
                By.id("org.wikipedia:id/text_input"),
                "Cannot find input to sen the article name",
                25
        );

        waitForElementAndClick(
                By.xpath("//*[@resource-id='org.wikipedia:id/search_results_container']//*[contains(@text, '" + article_description + "')]"),
                "Cannot find article with " + article_description + " text searching by " + search_word,
                25
        );


        waitForElementPresent(
                By.xpath("//*[contains(@text, '" + article_description + "')]"),
                "The article with " + article_description + " is not opened",
                15
        );

    }

    private WebElement waitForElementPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private WebElement waitForElementAndClick(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.click();
        return element;
    }

    private WebElement waitForElementAndSendKeys(By by, String value, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.sendKeys(value);
        return element;
    }

    private boolean waitForElementNotPresent(By by, String error_message, long timeoutInSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
        wait.withMessage(error_message + "\n");
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
    }

    private WebElement waitForElementAndClear(By by, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        element.clear();
        return element;
    }

    private void waitForElementHasWord(By by, String word, String error_message, long timeoutInSeconds) {
        WebElement element = waitForElementPresent(by, error_message, timeoutInSeconds);
        String article = element.getAttribute("text");
        Assert.assertTrue("The article " + article + " does not contain word " + word, article.contains(word));
    }

    protected void swipeUp(int timeOfSwipe) {
        TouchAction action = new TouchAction(driver);
        Dimension size = driver.manage().window().getSize();
        int x = size.width / 2;
        int start_y = (int) (size.height * 0.8);
        int end_y = (int) (size.height * 0.2);

        action
                .press(x, start_y)
                .waitAction(timeOfSwipe)
                .moveTo(x, end_y)
                .release()
                .perform();
    }

    protected void swipeUpQuick() {
        swipeUp(200);
    }

    protected void swipeElementToLeft(By by, String error_message) {
        WebElement element = waitForElementPresent(
                by,
                error_message,
                15
        );

        int left_x = element.getLocation().getX();
        int right_x = left_x + element.getSize().getWidth();
        int upper_y = element.getLocation().getY();
        int lower_y = upper_y + element.getSize().getHeight();
        int middle_y = (upper_y + lower_y) / 2;

        TouchAction action = new TouchAction(driver);
        action
                .press(right_x, middle_y)
                .waitAction(400)
                .moveTo(left_x, middle_y)
                .release()
                .perform();

    }

    private int getAmountOfElements(By by) {

        List elements = driver.findElements(by);
        return elements.size();

    }

    private void assertElementPresent(By searchable_element, String error_message) {
        int amount_of_elements = getAmountOfElements(searchable_element);
        if (amount_of_elements == 0) {
            String default_message = "An element '" + searchable_element.toString() + "' is not present\n";
            throw new AssertionError(default_message + " " + error_message);

        }

    }

    public void checkScreenOrientation() {
        ScreenOrientation orientation = driver.getOrientation();
        if(orientation == ScreenOrientation.LANDSCAPE) {
            driver.rotate(ScreenOrientation.PORTRAIT);
        }
    }

}


