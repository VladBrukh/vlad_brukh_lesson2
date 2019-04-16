package tests;

import lib.CoreTestCase;
import lib.ui.*;
import org.junit.Test;
import org.openqa.selenium.By;

public class LessonFourTests extends CoreTestCase {

    @Test
    public void testSearchTwoArticleAndCancelSearch() {

        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Dracula");
        SearchPageObject.waitForSearchResult("Dracula in popular culture");
        SearchPageObject.waitForSearchResult("Dracula Untold");

        SearchPageObject.waitForCancelButtonToAppear();
        SearchPageObject.clickCancelButton();

        SearchPageObject.assertThereIsNoResultOfSearch();

    }

    @Test
    public void testSavingTwoArticlesToMyLists() {

        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Dracula");
        SearchPageObject.clickByArticleWithSubstring("Dracula in popular culture");

        ArticlePageObject ArticlePageObject = new ArticlePageObject(driver);
        ArticlePageObject.waitForTitleElement();
        String first_article_title = ArticlePageObject.getArticleTitle();
        String name_of_folder = "Dracula folder";
        ArticlePageObject.addArticleToMyList(name_of_folder);
        ArticlePageObject.closeArticle();

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Dracula");
        SearchPageObject.clickByArticleWithSubstring("Dracula Untold");
        ArticlePageObject.waitForTitleElement();
        String second_article_title = ArticlePageObject.getArticleTitle();
        ArticlePageObject.addSecondArticleToMyList(name_of_folder);
        ArticlePageObject.closeArticle();

        NavigationUI NavigationUI = new NavigationUI(driver);
        NavigationUI.clickMyLists();

        MyListsPageObject MyListsPageObject = new MyListsPageObject(driver);
        MyListsPageObject.openFolderByName(name_of_folder);
        MyListsPageObject.swipeArticleToDelete(first_article_title);
        MyListsPageObject.waitForArticleToAppearByTitleAndClick(second_article_title);

        ArticlePageObject.waitForTitleElement();
        String second_article_title_opened_from_folder = ArticlePageObject.getArticleTitle();

        assertEquals(
                "Titles do not match",
                second_article_title,
                second_article_title_opened_from_folder
        );

    }

    @Test
    public void testAssertTitle() {

        SearchPageObject SearchPageObject = new SearchPageObject(driver);

        SearchPageObject.initSearchInput();
        SearchPageObject.typeSearchLine("Dracula");
        SearchPageObject.clickByArticleWithSubstring("Dracula in popular culture");

        String description = "Wikimedia list article";
        String title_element = "org.wikipedia:id/view_page_title_text";

        MainPageObject MainPageObject = new MainPageObject(driver);
        MainPageObject.waitForElementPresent(
                By.xpath("//*[contains(@text, '" + description + "')]"),
                "The article with " + description + " is not opened",
                15
        );

        MainPageObject.assertElementPresent(
                By.id(title_element),
                "The title element is not found in the article"
        );

    }
}
