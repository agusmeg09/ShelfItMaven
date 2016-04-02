package com.visualscienceco.webdriver;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.testng.Assert.*;

public class LoginCheckAndOut {
    private WebDriver driver;

    @BeforeTest
    public void setUp() {
        driver = new FirefoxDriver();
        driver.get("http://shelfit.com/");
    }

    @BeforeMethod
    public void cleanLoginForm() {
        driver.findElement(By.name("UserName")).clear();
        driver.findElement(By.name("Password")).clear();
    }

    @Test
    public void testAuthenticationFailureWithEmptyFields() {
        driver.findElement(By.name("UserName")).sendKeys("");
        driver.findElement(By.name("Password")).sendKeys("");
        driver.findElement(By.id("logInBtn")).submit();

        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"authenticate\"]/div[2]/span[1]")));
        assertEquals("The User name field is required.", driver.findElement(By.xpath("//*[@id=\"authenticate\"]/div[2]/span[1]")).getText());
        new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"authenticate\"]/div[2]/span[2]")));
        assertEquals("The Password field is required.", driver.findElement(By.xpath("//*[@id=\"authenticate\"]/div[2]/span[2]")).getText());
    }

    @Test
    public void testAuthenticationFailureWhenProvidingBadCredentials() {
        driver.findElement(By.name("UserName")).sendKeys("Juanito");
        driver.findElement(By.name("Password")).sendKeys("elCartero");
        driver.findElement(By.id("logInBtn")).submit();

        new WebDriverWait(driver, 7)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"authenticate\"]/div[2]/div/ul/li")));
        assertEquals("The user name or password provided is incorrect.", driver.findElement(By.xpath("//*[@id=\"authenticate\"]/div[2]/div/ul/li")).getText());
    }

    @Test
    public void testAuthenticationSuccessWhenProvidingCorrectCredentials(){
        driver.findElement(By.name("UserName")).sendKeys("jeff@digitalinlet.com");
        driver.findElement(By.name("Password")).sendKeys("test");
        driver.findElement(By.id("logInBtn")).submit();

        assertEquals("My Shelves", driver.getTitle());
        assertEquals("SHELFIT: NOTRE DAME DE SION SCHOOL, KANSAS CITY", driver.findElement(By.xpath("//*[@id=\"container\"]/header/a")).getText());

        JavascriptExecutor js = (JavascriptExecutor)driver;
        WebElement element = (WebElement)js.executeScript("$(\"#logOffBtn\").click()");
        assertEquals("Shelf It Login Page", driver.getTitle());
    }

    @AfterTest
    public void tearDown() throws Exception {
        driver.quit();
    }
}
