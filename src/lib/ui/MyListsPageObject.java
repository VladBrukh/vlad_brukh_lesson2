package lib.ui;

import io.appium.java_client.AppiumDriver;
import lib.Platform;

abstract public class MyListsPageObject extends MainPageObject {

    protected static String
            FOLDER_BY_NAME_TPL,
            ARTICLE_BY_TITLE_TPL,
            ARTICLE_IN_LIST;

    private static String getFolderXpathByName(String name_of_folder) {

        return FOLDER_BY_NAME_TPL.replace("{FOLDER_NAME}", name_of_folder);
    }

    private static String getSavedArticleXpathByTitle(String article_title) {

        return ARTICLE_BY_TITLE_TPL.replace("{TITLE}", article_title);
    }

    public MyListsPageObject(AppiumDriver driver) {

        super(driver);
    }

    public void openFolderByName(String name_of_folder) {

        String folder_name_xpath = getFolderXpathByName(name_of_folder);
        this.waitForElementAndClick(
                folder_name_xpath,
                "Cannot find folder by name " + name_of_folder,
                10
        );
    }

    public void waitForArticleToAppearByTitle(String article_title) {

        String article_xpath = getSavedArticleXpathByTitle(article_title);
        this.waitForElementPresent(article_xpath, "Cannot find saved article by title " + article_title, 15);
    }

    public void waitForArticleToAppearByTitleAndClick(String article_title) {

        String article_xpath = getSavedArticleXpathByTitle(article_title);
        this.waitForElementAndClick(article_xpath, "Cannot find saved article by title " + article_title, 15);
    }

    public void waitForArticleToDisappearByTitle(String article_title) {

        String article_xpath = getSavedArticleXpathByTitle(article_title);
        this.waitForElementNotPresent(article_xpath, "Saved article still present with article title " + article_title, 15);
    }

    public void swipeArticleToDelete(String article_title) {

        this.waitForArticleToAppearByTitle(article_title);
        String article_xpath = getSavedArticleXpathByTitle(article_title);
        this.swipeElementToLeft(
                article_xpath,
                "Cannot find saved article"
        );

        if(Platform.getInstance().isIOS()) {
            this.clickElementToTheRightUpperCorner(article_xpath, "Cannot find saved article", 5);
        }

        this.waitForArticleToDisappearByTitle(article_title);
    }

    public int checkNumberOfArticlesInMyLists() {

        this.waitForElementPresent(ARTICLE_IN_LIST, "Cannot find article container", 10);

        int number_of_articles = this.getAmountOfElements(ARTICLE_IN_LIST);
        return number_of_articles;
    }
}
