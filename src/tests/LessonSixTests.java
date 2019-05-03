package tests;

import lib.CoreTestCase;
import lib.Platform;
import lib.ui.*;
import lib.ui.factories.ArticlePageObjectFactory;
import lib.ui.factories.MyListsPageObjectFactory;
import lib.ui.factories.NavigationUIFactory;
import lib.ui.factories.SearchPageObjectFactory;
import org.junit.Test;

public class LessonSixTests extends CoreTestCase {

    @Test
    public void testSearchTwoArticleAndCancelSearch() {

        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);;

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Dracula");
        SearchPageObject.waitForSearchResult("Dracula in popular culture");
        SearchPageObject.waitForSearchResult("Dracula Untold");

        SearchPageObject.waitForCancelButtonToAppear();
        SearchPageObject.clickCancelButton();

        SearchPageObject.assertThereIsNoResultOfSearch();

    }

    private static final String name_of_folder = "Dracula folder";
    @Test
    public void testSavingTwoArticlesToMyLists() {

        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);;

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Dracula");
        SearchPageObject.clickByArticleWithSubstring("Dracula in popular culture");

        ArticlePageObject ArticlePageObject = ArticlePageObjectFactory.get(driver);
        ArticlePageObject.waitForTitleElement();
        String first_article_title = ArticlePageObject.getArticleTitle();

        if(Platform.getInstance().isAndroid()) {
            ArticlePageObject.addArticleToMyList(name_of_folder);
        } else {
            ArticlePageObject.addArticlesToMySaved();
        }

        ArticlePageObject.closeArticle();

        SearchPageObject.initSearchInput();

        if(Platform.getInstance().isAndroid()) {
            SearchPageObject.typeSearchLine("Dracula");
        }

        SearchPageObject.clickByArticleWithSubstring("Dracula Untold");

        MyListsPageObject MyListsPageObject = MyListsPageObjectFactory.get(driver);

        String second_article_title = null;
        if(Platform.getInstance().isAndroid()) {
            ArticlePageObject.waitForTitleElement();
            second_article_title = ArticlePageObject.getArticleTitle();
            ArticlePageObject.addSecondArticleToMyList(name_of_folder);
        } else {
            ArticlePageObject.addArticlesToMySaved();
        }

        ArticlePageObject.closeArticle();

        NavigationUI NavigationUI = NavigationUIFactory.get(driver);
        NavigationUI.clickMyLists();

        if (Platform.getInstance().isAndroid()) {
            MyListsPageObject.openFolderByName(name_of_folder);
        } else {
            assertEquals(
                    "The second article is not added!",
                    2,
                    MyListsPageObject.checkNumberOfArticlesInMyLists()
            );
        }

        MyListsPageObject.swipeArticleToDelete(first_article_title);

        if(Platform.getInstance().isAndroid()) {
            MyListsPageObject.waitForArticleToAppearByTitleAndClick(second_article_title);

            ArticlePageObject.waitForTitleElement();
            String second_article_title_opened_from_folder = ArticlePageObject.getArticleTitle();

            assertEquals(
                    "Titles do not match",
                    second_article_title,
                    second_article_title_opened_from_folder
            );
        } else {

            assertEquals(
                    "There are still two articles in the list!",
                    MyListsPageObject.checkNumberOfArticlesInMyLists(),
                    1
            );
        }
    }

    @Test
    public void testAssertTitle() {

        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);;

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Dracula");
        SearchPageObject.clickByArticleWithSubstring("Dracula in popular culture");

        String description = "Wikimedia list article";
        String title_element = "org.wikipedia:id/view_page_title_text";

        MainPageObject MainPageObject = new MainPageObject(driver);
        MainPageObject.waitForElementPresent(
                "//*[contains(@text, '" + description + "')]",
                "The article with " + description + " is not opened",
                15
        );

        MainPageObject.assertElementPresent(
                title_element,
                "The title element is not found in the article"
        );
    }

    @Test
    public void testSearchArticleByTitleAndDescription () {

        SearchPageObject SearchPageObject = SearchPageObjectFactory.get(driver);;

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Dracula");
        SearchPageObject.waitForElementByTitleAndDescription("Dracula Untold", "2014 American dark fantasy action horror film directed by Gary Shore");
        SearchPageObject.waitForElementByTitleAndDescription("Dracula in popular culture", "Wikimedia list article");
        SearchPageObject.waitForElementByTitleAndDescription("Dracula (1924 play)", "1924 stage play");

    }
}
