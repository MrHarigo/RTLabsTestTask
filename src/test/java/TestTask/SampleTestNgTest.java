package TestTask;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.PageFactory;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import TestTask.pages.HomePage;

public class SampleTestNgTest extends TestNgTestBase {

    private HomePage homepage;

    @BeforeMethod
    public void initPageObjects() {
        homepage = PageFactory.initElements(driver, HomePage.class);
    }

    @Test(sequential = true)
    public void testEnterButtonSearch() {
        driver.get(baseUrl);
        driver.findElement(By.className("HeadBanner-Button-Enter")).click();
        Assert.assertEquals("Авторизация", driver.getTitle());
    }

    @Test(sequential = true, dependsOnMethods = {"testEnterButtonSearch"})
    public void testWrongPasswordMessage() {
        driver.findElement(By.name("login")).sendKeys("SomeTestingAccount");
        driver.findElement(By.name("passwd")).sendKeys("12345678");
        driver.findElement(By.xpath("//button[@type='submit' and @class='passport-Button']")).click();
        WebDriverWait wait = new WebDriverWait(driver, 20);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("passport-Domik-Form-Error")));
        String textMessage = driver.findElement(By.className("passport-Domik-Form-Error")).getText();
        if (textMessage.equals("Введите символы с картинки")) throw new RuntimeException("death by Captcha");
        Assert.assertEquals(textMessage,"Неверный пароль");
    }

    @Test(sequential = true, dependsOnMethods = {"testEnterButtonSearch","testWrongPasswordMessage"})
    public void testSuccessfulLogin() {
        driver.findElement(By.name("passwd")).sendKeys("STA22052018");
        driver.findElement(By.xpath("//button[@type='submit' and @class='passport-Button']")).click();
        new WebDriverWait(driver, 30).until(
                webDriver -> webDriver.findElement(By.className("mail-Layout-Content")));
        Assert.assertTrue(driver.getTitle().contains("Входящие — Яндекс.Почта"));
    }
}
