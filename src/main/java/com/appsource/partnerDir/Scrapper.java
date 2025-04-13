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

public class Scrapper {

	public static void main(String[] args) {
		WebDriver driver = new ChromeDriver();
		driver.get("https://appsource.microsoft.com/en-us/marketplace/partner-dir");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.manage().window().maximize();
		
		Utils.selectCountry("Egypt", driver);

		int counter = driver.findElements(By.xpath("//div[@role='listitem']")).size();
		String nextBtnXpath = "//button//span[text()='Next']";

		int partnerCounter = 0;
		try {
			CSVPrinter csvPrinter = DataExporter.createCSVprinter(new File("ExtractedData.csv"));
			csvPrinter.printComment("Egypt");
//			try {
//				Thread.sleep(Duration.ofSeconds(10));
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			while(true) {
				
			for(int i=1;i<=counter;i++) {
				List<Object> partnerDetails = Utils.getPartnerDetails(i,driver);
						csvPrinter.printRecords(partnerDetails);
						
						
				System.out.println("-------------------------------------");
			}
			csvPrinter.close();
			try {
				if(!driver.findElement(By.xpath(nextBtnXpath)).isDisplayed()) {
					System.out.println("Iterated through all the partners for this country");
					break;
				}
			}
			finally {
				System.out.println("finally block");
			}
				driver.findElement(By.xpath(nextBtnXpath)).click();	
			}
		}
			catch(NoSuchElementException | IOException e) {
				driver.switchTo().frame("partnersIframe");
				driver.findElement(By.xpath(nextBtnXpath)).click();

			}

		}

	}


