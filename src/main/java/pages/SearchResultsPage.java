package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;

public class SearchResultsPage {
    private WebDriver driver;
    private By productContainer = By.xpath("//div[@data-cy='asin-faceout-container']");
    private By productTitle = By.xpath(".//div[@data-cy='title-recipe']/h2");
    private By productPrice = By.xpath(".//span[@class='a-price-whole']");

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
    }
    public void returnTitleAndPrice() {
        List<WebElement> listOfProducts = driver.findElements(productContainer);
        List<Map.Entry<String, Integer>> productList = new ArrayList<>();

        for (WebElement product : listOfProducts) {
            String title = product.findElement(productTitle).getText();
            int price;
            try{
                WebElement priceElement = product.findElement(productPrice);
                price = Integer.parseInt(priceElement.getText().replace(",",""));
            }
            catch(Exception e){
                price = 0;
            }
            productList.add(new AbstractMap.SimpleEntry<>(title,price));

        }
        productList.sort(Map.Entry.comparingByValue());
        for (Map.Entry<String,Integer> product: productList)
        {
            System.out.println("Product title " + product.getKey() + " and price is " + product.getValue());
        }
        Assert.assertNotEquals (0,productList.size(),"Search Page should have the search results");
    }
}
