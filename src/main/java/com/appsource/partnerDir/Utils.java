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
			Thread.sleep(Duration.ofSeconds(15));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(char c:countryName.toCharArray()) {
			countrySearchElement.sendKeys(Character.toString(c));
		}
		try {
			Thread.sleep(Duration.ofSeconds(15));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		action.sendKeys(Keys.DOWN).sendKeys(Keys.ENTER).build().perform();
		try {
			Thread.sleep(Duration.ofSeconds(5));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		driver.findElement(By.xpath("//button[@name='apply_button']")).click();
	}

	public static List<Object> getPartnerDetails(int partnerNumber, WebDriver driver, WebDriverWait wait) {

		String partnerNameXpath = "//div[@class='header']//h1";
		String productsXpath = "(//div[@class='card-products-box']//span)";
		String locDropdownXpath = "//div[@class='partner-location-box']//span[contains(@id,'dropdown')]";
		String locationsXpath = "//div[@class='filter-search-box-list']//span[@role='presentation']";
		String partnerDescXpath = "//div[@class='partner-description']";
		String solPartnerDesgnXpath = "//div[text()='Solutions Partner designation']/../following-sibling::div//li";
		String expertiseCategoryXpath = "(//div[text()='Partner Expertise']/../following-sibling::div)[2]//h6";
		String additionalInfoXpath = "//a/label[text()='Additional information']";
		String partnerContactInfoXpath = "//div[@aria-labelledby='partner-details-links-header']//a";

		try {
			wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@role='listitem']["+partnerNumber+"]"))));
			driver.findElement(By.xpath("//div[@role='listitem']["+partnerNumber+"]")).click();
		}
		catch(NoSuchElementException e1) {
			e1.printStackTrace();
			driver.switchTo().frame("partnersIframe");
			driver.findElement(By.xpath("//div[@role='listitem']["+partnerNumber+"]")).click();
		}
		catch(StaleElementReferenceException e2) {
			e2.printStackTrace();
			driver.switchTo().parentFrame();
			driver.switchTo().frame("partnersIframe");
			driver.findElement(By.xpath("//div[@role='listitem']["+partnerNumber+"]")).click();
		}

		//printing partners name
		wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(partnerNameXpath))));
		String partnerName = driver.findElement(By.xpath(partnerNameXpath)).getText();
		System.out.println("Partner name : "+partnerName);

		//partner profile link
		String partnerProfilePageUrl = driver.getCurrentUrl();
		System.out.println("Partner profile link : "+partnerProfilePageUrl);

		//finding all products
		List<WebElement> allProducts = driver.findElements(By.xpath(productsXpath));
		//System.out.println("\nAll products from this partners are : ");
		//for(WebElement products : allProducts) System.out.println(products.getAttribute("textContent"));

		//finding all locations
		driver.findElement(By.xpath(locDropdownXpath)).click();
		try {
			Thread.sleep(Duration.ofSeconds(5));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		List<WebElement> allLocations= driver.findElements(By.xpath(locationsXpath));
		List<String> locations = new ArrayList<String>();
		//System.out.println("\nAll locations from this partners are : ");
		for(WebElement location : allLocations) {
			//if(location.getText().contains("United States")) 
			locations.add(location.getAttribute("textContent"));
			//System.out.println(location.getAttribute("textContent"));
		}

		//finding partner description
		String partnerDesc = driver.findElement(By.xpath(partnerDescXpath)).getText();
		//System.out.println("partner description :\n"+partnerDesc);

		//finding partner designations
		List<WebElement> partnerDesignations = driver.findElements(By.xpath(solPartnerDesgnXpath));
		List<String> designations = new ArrayList<String>();
		//System.out.println("\nAll designations from this partners are : ");
		for(WebElement designation : partnerDesignations) {
			designations.add(designation.getText());
			//System.out.println(designation.getText());
		}

		//get partner expertise
		List<WebElement> expertiseCategory = driver.findElements(By.xpath(expertiseCategoryXpath));
		List<String> experties = new ArrayList<String>();
		//System.out.println("\nAll expertise from this partners are : ");
		for(WebElement category : expertiseCategory) {
			//System.out.println("Expertise Category : "+category.getText());
			String expertiesXpath = "(//div[text()='Partner Expertise']/../following-sibling::div)[2]//h6[text()='"+category.getText()+"']//following-sibling::ul//li";
			List<WebElement> expertises = driver.findElements(By.xpath(expertiesXpath));
			for(WebElement expertise : expertises) {
				experties.add(expertise.getText());
				//System.out.println(expertise.getText());
			}
		}

		//partner contact information
		//System.out.println("Partner contact information : ");
		driver.findElement(By.xpath(additionalInfoXpath)).click();
		List<WebElement> contactInfos = driver.findElements(By.xpath(partnerContactInfoXpath));
		List<String> contactInfoLinks = new ArrayList<String>();
		for(WebElement contactInfo : contactInfos) {
			contactInfoLinks.add(contactInfo.getText()+" : "+contactInfo.getAttribute("href"));
			//System.out.println(contactInfo.getText()+" : "+contactInfo.getAttribute("href"));
		}

		List<Object> partnerDetails = new ArrayList<Object>();
		
		List<String> row1 = new ArrayList<String>(); 
		int rowCount = Math.max(Math.max(locations.size(), designations.size()),Math.max(experties.size(),contactInfoLinks.size()));
		for(int i=0;i<rowCount;i++) {
			String location = i<locations.size() ? locations.get(i) : null;
			String designation = i<designations.size() ? designations.get(i) : null;
			String expertie = i<experties.size() ? experties.get(i) : "";
			String contactInfoLink = i<contactInfoLinks.size() ? contactInfoLinks.get(i) : null;
			if(i == 0) {
				row1.add(partnerName);
				row1.add(partnerProfilePageUrl);
				row1.add(location);
				row1.add(partnerDesc);
				row1.add(designation);
				row1.add(expertie);
				row1.add(contactInfoLink);
				partnerDetails.add(row1);
			}
			else {
				partnerDetails.add(new String[] {null,null,location,null,designation,expertie,contactInfoLink});
			}
		}
		
		driver.navigate().back();
		driver.navigate().back();

		return partnerDetails;

	}
}
