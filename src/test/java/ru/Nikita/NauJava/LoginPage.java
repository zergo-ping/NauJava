package ru.Nikita.NauJava;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object для страницы логина
 */
public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By usernameField = By.id("username");
    private By passwordField = By.id("password");
    private By submitButton = By.xpath("//button[@type='submit']");
    private By loginHeading = By.xpath("//h1[contains(text(), 'Вход')]");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void navigateTo(String baseUrl) {
        driver.navigate().to(baseUrl + "/auth/login");
    }

    public void enterUsername(String username) {
        WebElement element = driver.findElement(usernameField);
        element.clear();
        element.sendKeys(username);
    }

    public void enterPassword(String password) {
        WebElement element = driver.findElement(passwordField);
        element.clear();
        element.sendKeys(password);
    }

    public void clickLoginButton() {
        driver.findElement(submitButton).click();
    }

    public boolean isLoginPageDisplayed() {
        try {
            return driver.findElement(loginHeading).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    public void login(String username, String password) {
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
}
