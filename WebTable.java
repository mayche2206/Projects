/*
1) goto https://forms.zohopublic.com/murodil/report/Applicants/reportperma/DibkrcDh27GWoPQ9krhiTdlSN4_34rKc8ngubKgIMy8
2) Create a HashMap
3) change row number to 100, read all data on first page and put uniquID as a KEY and Applicant info as a Value to a map. 
applicants.put(29,"Amer, Sal-all@dsfdsf.com-554-434-4324-130000")
4) Click on next page , repeat step 3
5) Repeat step 4 for all pages 
6) print count of items in a map. and assert it is matching with a number at the buttom
======================================
 */

package com.cybertek;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebTable {

	WebDriver driver;

	@BeforeClass
	public void setUpClass() throws InterruptedException {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.get("https://forms.zohopublic.com/murodil/report/Applicants/reportperma/DibkrcDh27GWoPQ9krhiTdlSN4_34rKc8ngubKgIMy8");
		
		Select selectPerPage = new Select(driver.findElement(By.xpath("//select[@id='recPerPage']")));
		selectPerPage.selectByVisibleText("100");

		Thread.sleep(500);
	}

	@Test
	public void testCollectRecords() throws InterruptedException {

		// Gets the total number of records
		int totalRecords = Integer.parseInt(driver.findElement(By.xpath("//span[@id='total']")).getText());
		
		Map<Integer, String> map = new HashMap<Integer, String>();

		// Loop through each page
		for (int i = 0; i <= totalRecords / 100; i++) {

			// Gets the list of all <tr> elements on a page
			List<WebElement> listRows = driver.findElements(By.xpath("//table[@id='reportTab']/tbody/tr"));

			// Loop through each row
			for (WebElement row : listRows) {

				// Constructs row's xPath
				String xpathToRow = "//tr[@id='" + row.getAttribute("id").toString() + "']";

				// Gets the Application ID number
				int key = Integer.parseInt(driver.findElement(By.xpath(xpathToRow + "/td[1]")).getText());

				// Builds value by adding all remaining td's
				String value = "";
				for (int tdNo = 2; tdNo <= 5; tdNo++)
					value += driver.findElement(By.xpath(xpathToRow + "/td[" + tdNo + "]")).getText() + "--";

				// Adds key and value to the map
				map.put(key, value);

			}

			// Goes to the next page
			goToNextPage();

		}

		// Asserts if expected and actual total numbers are matching
		Assert.assertEquals(map.size(), totalRecords);

	}

	public void goToNextPage() throws InterruptedException {

		try {
			if (driver.findElement(By.xpath("//a[@class='nxtArrow']")).isDisplayed())
				driver.findElement(By.xpath("//a[@class='nxtArrow']")).click();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Thread.sleep(500);
	}

	@AfterClass
	public void teardownClass() {
		 driver.close();
	}
}

