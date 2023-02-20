package org.keepcode;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.util.*;

public class Main {

    private static final String FILE_NAME = "price-list.txt";
    public static void main(String[] args) {
        var result = new HashMap<String, List<OfferedService>>();

        var chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("headless");
        var driver = new ChromeDriver(chromeOptions);

        driver.get("https://onlinesim.ru/price-list");
        closeCookiesPopup(driver);

        var elements = driver.findElements(By.className("country-name"));
        for (WebElement element : elements) {
            var countryName = element.getText();
            System.out.println("Getting price list for " + countryName + "...");
            element.click();
            var document = Jsoup.parse(driver.getPageSource());
            var services = document.getElementsByClass("service-block");

            var offeredServiceList = new ArrayList<OfferedService>();
            for (Element service : services) {
                var serviceName = service.selectXpath(".//*[@class='price-name']").get(0).text();
                var servicePrice = service.selectXpath(".//*[@class='price-text']").get(0).text().replaceAll("[^-\\d.]", "");
                offeredServiceList.add(new OfferedService(serviceName, Double.valueOf(servicePrice)));
            }
            result.put(countryName, offeredServiceList);
        }
        driver.quit();
        writeToFile(result);
    }

    public static void closeCookiesPopup(WebDriver driver) {
        try {
            var wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            var cookiePopup = wait.until(ExpectedConditions.elementToBeClickable(By.id("termsbox_close")));
            cookiePopup.click();
            System.out.println("Popup closed");
        } catch (Exception e) {
            System.out.println("There is no cookie popup");
        }
    }

    public static void writeToFile(Map<String, List<OfferedService>> map) {
        try (var fileWriter = new FileWriter(FILE_NAME);
             var bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write(new JSONObject(map).toString(4));
            System.out.println("Created file price-list.txt");
        } catch (IOException e) {
            System.out.println("Could not write to file");
        }
    }
}