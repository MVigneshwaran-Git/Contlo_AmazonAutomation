import utility.BaseTest;
import org.testng.annotations.Test;
import utility.ConfigReader;
import pages.*;

public class ProductSearchTest extends BaseTest {

    @Test
    public void AmazonProductSearch() {
        driver.get(ConfigReader.getProperty("base.url"));
        AmazonHomePage homepage = new AmazonHomePage(driver);
        homepage.searchAProduct("lg soundbar");

        SearchResultsPage searchPage = new SearchResultsPage(driver);
        searchPage.returnTitleAndPrice();

    }
}
