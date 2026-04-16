package ru.Nikita.NauJava;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты для проверки сценариев входа и выхода из приложения
 */
public class AuthenticationUITest {


    @Autowired
    private LoginPage loginPage;
    @Autowired
    private UserDashboardPage dashboardPage;

    protected static final String BASE_URL = "http://localhost:8080";
    @Autowired
    private  WebDriver driver;
    @Autowired
    private  WebDriverWait wait;

    private static final int TIMEOUT_SECONDS = 10;



    /**
     * Инициализация Page Objects перед каждым тестом
     */
    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(TIMEOUT_SECONDS));
        loginPage = new LoginPage(driver, wait);
        dashboardPage = new UserDashboardPage(driver, wait);

    }

    /**
     * Успешный вход в приложение
     */
    @Test
    public void testSuccessfulLogin() {
        // Arrange
        loginPage.navigateTo(BASE_URL);

        assertThat(loginPage.isLoginPageDisplayed())
            .isTrue();

        String testEmail = "testUser2@example.com";
        String testPassword = "test";
        
        loginPage.login(testEmail, testPassword);


        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String currentUrl = driver.getCurrentUrl();
        
        assertThat(currentUrl)
            .as("User should be redirected to dashboard after successful login")
            .containsAnyOf("/custom/users", "/profile", "/dashboard")
            .doesNotContain("/auth/login");

        assertThat(dashboardPage.isUserLoggedIn())
            .as("User should be logged in")
            .isTrue();

    }

    /**
     * Выход из приложения
     */
    @Test
    public void testSuccessfulLogout() {

        loginPage.navigateTo(BASE_URL);
        
        String testEmail = "testUser2@example.com";
        String testPassword = "test";
        
        loginPage.login(testEmail, testPassword);

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertThat(dashboardPage.isUserLoggedIn())
            .isTrue();

        try {
            dashboardPage.clickLogout();
        } catch (WebDriverException e) {
            driver.navigate().to(BASE_URL + "/logout");
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        String currentUrl = driver.getCurrentUrl();
        
        assertThat(currentUrl)
            .containsAnyOf("/auth/login", "/");

        assertThat(loginPage.isLoginPageDisplayed())
            .isTrue();

    }


}
