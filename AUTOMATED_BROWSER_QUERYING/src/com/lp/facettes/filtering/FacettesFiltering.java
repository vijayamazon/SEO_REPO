package com.lp.facettes.filtering;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class FacettesFiltering {
	public static void main(String[] args){

        // Create a new instance of the Firefox driver
        // Notice that the remainder of the code relies on the interface, 
        // not the implementation.
        WebDriver driver = new FirefoxDriver();
	//	WebDriver driver = new ChromeDriver();

        // And now use this to visit Google
        driver.get("http://www.cdiscount.com/maison/linge-maison/linge-de-decoration/plaids-et-couvre-lits/l-117620403.html");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        
        // Radio Button: Check Monday using XPATH locator.
        
        WebElement menuArrow = driver.findElement(By.cssSelector("div.mvFTitle.noSel"));
        menuArrow.click();
        
        WebElement checkBoxFacetMarketPlaceFiltering = driver.findElement(By.xpath("//input[@value='f/368/c le marche']"));
        checkBoxFacetMarketPlaceFiltering.click();
     




        // Check the title of the page
        System.out.println("Page title is: " + driver.getTitle());
        
        // Google's search is rendered dynamically with JavaScript.
        // Wait for the page to load, timeout after 10 seconds
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("cheese!");
            }
        });

        // Should see: "cheese! - Google Search"
        System.out.println("Page title is: " + driver.getTitle());
        
        //Close the browser
        driver.quit();
    	
		
	}
}
