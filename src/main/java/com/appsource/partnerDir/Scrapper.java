package com.appsource.partnerDir;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;

import org.apache.commons.csv.CSVPrinter;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Scrapper {

	public static void main(String[] args) {
		WebDriver driver = new ChromeDriver();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
		driver.get("https://appsource.microsoft.com/en-us/marketplace/partner-dir");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.manage().window().maximize();
		
		Utils.selectCountry("Egypt", driver);

		int counter = driver.findElements(By.xpath("//div[@role='listitem']")).size();
		String nextBtnXpath = "//button//span[text()='Next']";

		try {
			CSVPrinter csvPrinter = DataExporter.createCSVprinter(new File("ExtractedData.csv"));
			csvPrinter.printComment("Egypt");
			
			while(true) {		
			for(int i=1;i<=counter;i++) {
				List<Object> partnerDetails = Utils.getPartnerDetails(i,driver,wait);
				csvPrinter.printRecords(partnerDetails);
				System.out.println("-------------------------------------");
			}
			try {
				driver.switchTo().frame("partnersIframe");
				wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(nextBtnXpath)));
				if(!driver.findElement(By.xpath(nextBtnXpath)).isDisplayed()) {
					System.out.println("Iterated through all the partners for this country");
					csvPrinter.close();
					break;
				}
				else {
					driver.findElement(By.xpath(nextBtnXpath)).click();	
				}
			}
			catch(NoSuchElementException nsee) {
				nsee.printStackTrace();
				driver.switchTo().frame("partnersIframe");
				driver.findElement(By.xpath(nextBtnXpath)).click();
			}
			
			}
		}
			catch(Exception e) {
				
				e.printStackTrace();			}

		}

	}


