package com.appsource.partnerDir;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Scrapper {
	
	public static void main(String[] args) {
		WebDriver driver = new ChromeDriver();
		driver.get("https://appsource.microsoft.com/en-us/marketplace/partner-dir");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.manage().window().maximize();
		Utils.selectCountry("United States", driver);
		
		int counter = driver.findElements(By.xpath("//div[@role='listitem']")).size();
		String nextBtnXpath = "//button//span[text()='Next']";
		
		int partnerCounter = 0;
			while(true) {
				for(int i=1;i<=counter;i++) {
					Utils.getPartnerDetails(i,driver);
					System.out.println("-------------------------------------");
				}
				try {
					if(!driver.findElement(By.xpath(nextBtnXpath)).isDisplayed()) {
						System.out.println("Iterated through all the partners for this country");
						break;
					}
					driver.findElement(By.xpath(nextBtnXpath)).click();	
				}
				catch(NoSuchElementException e) {
					driver.switchTo().frame("partnersIframe");
					driver.findElement(By.xpath(nextBtnXpath)).click();
						
				}
				
		}
		
	}

}
