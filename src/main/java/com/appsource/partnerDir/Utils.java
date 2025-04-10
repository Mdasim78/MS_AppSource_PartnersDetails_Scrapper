package com.appsource.partnerDir;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Utils {


	public static void selectCountry(String countryName, WebDriver driver) {
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
		driver.switchTo().frame("partnersIframe");
		WebElement selectLocElement = driver.findElement(By.xpath("//button[text()='Select location']"));
		selectLocElement.click();
		WebElement countrySearchElement = driver.findElement(By.id("searchBox"));


		Actions action = new Actions(driver);
		wait.until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.xpath("//div[@role='listitem']"))));
		driver.findElement(By.id("searchBoxContainer")).click();
		countrySearchElement.click();
		try {
			Thread.sleep(Duration.ofSeconds(10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		for(char c:countryName.toCharArray()) {
			countrySearchElement.sendKeys(Character.toString(c));
		}
		try {
			Thread.sleep(Duration.ofSeconds(10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		action.sendKeys(Keys.DOWN).sendKeys(Keys.ENTER).build().perform();
		try {
			Thread.sleep(Duration.ofSeconds(5));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.findElement(By.xpath("//button[@name='apply_button']")).click();
	}

	public static void getPartnerDetails(int partnerNumber, WebDriver driver) {

		String partnerNameXpath = "//div[@class='header']//h1";
		String productsXpath = "(//div[@class='card-products-box']//span)";
		String locDropdownXpath = "//div[@class='partner-location-box']//span[contains(@id,'dropdown')]";
		String locationsXpath = "//div[@class='filter-search-box-list']//span[@role='presentation']";
		String partnerDescXpath = "//div[@class='partner-description']";
		String solPartnerDesgnXpath = "//div[text()='Solutions Partner designation']/../following-sibling::div//li";
		String expertiseCategoryXpath = "(//div[text()='Partner Expertise']/../following-sibling::div)[2]//h6";


		try {
			driver.findElement(By.xpath("//div[@role='listitem']["+partnerNumber+"]")).click();
		}
		catch(NoSuchElementException e1) {
			driver.switchTo().frame("partnersIframe");
			driver.findElement(By.xpath("//div[@role='listitem']["+partnerNumber+"]")).click();
		}
		catch(StaleElementReferenceException e2) {
			driver.switchTo().frame("partnersIframe");
			driver.findElement(By.xpath("//div[@role='listitem']["+partnerNumber+"]")).click();
		}

		//printing partners name
		String partnerName = driver.findElement(By.xpath(partnerNameXpath)).getText();
		System.out.println("Partner name of partner number "+partnerNumber+" is : "+partnerName);

		//finding all products
		List<WebElement> allProducts = driver.findElements(By.xpath(productsXpath));
		System.out.println("\nAll products from this partners are : ");
		for(WebElement products : allProducts) System.out.println(products.getAttribute("textContent"));

		//finding all locations
		driver.findElement(By.xpath(locDropdownXpath)).click();
		try {
			Thread.sleep(Duration.ofSeconds(5));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<WebElement> allLocations= driver.findElements(By.xpath(locationsXpath));
		System.out.println("\nAll locations from this partners are : ");
		for(WebElement location : allLocations) {
			if(location.getText().contains("United States")) System.out.println(location.getAttribute("textContent"));
		}

		//finding partner description
		String partnerDesc = driver.findElement(By.xpath(partnerDescXpath)).getText();
		System.out.println(partnerDesc);

		//finding partner designations
		List<WebElement> partnerDesignations = driver.findElements(By.xpath(solPartnerDesgnXpath));
		System.out.println("\nAll designations from this partners are : ");
		for(WebElement designation : partnerDesignations) System.out.println(designation.getText());

		//get partner expertise
		List<WebElement> expertiseCategory = driver.findElements(By.xpath(expertiseCategoryXpath));
		System.out.println("\nAll expertise from this partners are : ");
		for(WebElement category : expertiseCategory) {
			System.out.println("Expertise Category : "+category.getText());
			String expertiesXpath = "(//div[text()='Partner Expertise']/../following-sibling::div)[2]//h6[text()='"+category.getText()+"']//following-sibling::ul//li";
			List<WebElement> expertises = driver.findElements(By.xpath(expertiesXpath));
			for(WebElement expertise : expertises) System.out.println(expertise.getText());
		}

		driver.navigate().back();

	}
}
