package com.appsource.partnerDir;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVPrinter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ErrorTest {
		public static void main(String[] args) throws IOException {
			WebDriver driver = new ChromeDriver();
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(40));
			driver.get("https://appsource.microsoft.com/en-us/marketplace/partner-dir");
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			driver.manage().window().maximize();
			Utils.selectCountry("Egypt", driver);
			
			String nextBtnXpath = "//button//span[text()='Next']";
			List<String> partnerNames = new ArrayList<String>();
			List<WebElement> partnersNameElement = wait.until(ExpectedConditions.refreshed(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath("//div[@role='listitem']//p[@class='name']"))));
			
			for(WebElement partner : partnersNameElement) {
				partnerNames.add(partner.getText());
			}

		
			while(!partnerNames.contains("I Tech Pro")) {
				driver.findElement(By.xpath(nextBtnXpath)).click();
							
			}
			CSVPrinter csvPrinter = DataExporter.createCSVprinter(new File("ExtractedData.csv"));
			csvPrinter.printComment("Egypt");
			for(int i=1;i<=18;i++) {
				
				List<Object> partnerDetails = Utils.getPartnerDetails(i, driver, wait);
				csvPrinter.printRecords(partnerDetails);
				
//				if(!driver.findElement(By.xpath(nextBtnXpath)).isDisplayed()) {
//					System.out.println("Iterated through all the partners for this country");
//					csvPrinter.close();
//					break;
//				}
			}
			csvPrinter.close();
			
		}
}
