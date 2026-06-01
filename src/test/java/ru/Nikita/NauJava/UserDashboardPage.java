package ru.Nikita.NauJava;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Page Object для защищённой страницы пользователя
 */
public class UserDashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By logoutLink = By.xpath("//a[contains(@href, '/logout')] | //button[contains(@onclick, '/logout')]");
    private By userProfileSection = By.xpath("//h1 | //div[@class='user-profile'] | //nav");

    public UserDashboardPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public boolean isUserLoggedIn() {
        try {
            String currentUrl = driver.getCurrentUrl();
            return currentUrl.contains("/custom/users") || currentUrl.contains("/profile") || 
                   (!currentUrl.contains("/auth/login") && !currentUrl.contains("/home"));
        } catch (Exception e) {
            return false;
        }
    }


    public void clickLogout() {
        try {
            driver.findElement(logoutLink).click();
        } catch (Exception e) {
            driver.navigate().to(driver.getCurrentUrl().split("/custom")[0] + "/logout");
        }
    }

}
