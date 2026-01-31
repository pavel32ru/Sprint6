package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class MainPage {
    private final WebDriver driver;

    // Локаторы элементов главной страницы
    private final By orderButtonTop = By.xpath(".//button[@class='Button_Button__ra12g' and text()='Заказать']");
    private final By orderButtonBottom = By.xpath(".//button[@class='Button_Button__ra12g Button_Middle__1CSJM' and text()='Заказать']");
    private final By accordionQuestions = By.className("accordion__item");
    private final By accordionButtons = By.className("accordion__button");
    private final By accordionAnswers = By.className("accordion__panel");
    private final By scooterLogo = By.xpath(".//a[@class='Header_LogoScooter__3lsAR']");
    private final By yandexLogo = By.xpath(".//a[@class='Header_LogoYandex__3TSOI']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    // Методы для работы с аккордеоном
    public void clickAccordionItem(int index) {
        List<WebElement> items = driver.findElements(accordionButtons);
        if (index < items.size()) {
            ((JavascriptExecutor) driver).executeScript(
                    "arguments[0].scrollIntoView();", items.get(index)
            );
            items.get(index).click();
        }
    }

    public String getAccordionAnswerText(int index) {
        List<WebElement> answers = driver.findElements(accordionAnswers);
        if (index < answers.size()) {
            return answers.get(index).getText();
        }
        return "";
    }

    public boolean isAccordionAnswerDisplayed(int index) {
        List<WebElement> answers = driver.findElements(accordionAnswers);
        if (index < answers.size()) {
            return answers.get(index).isDisplayed();
        }
        return false;
    }

    // Методы для кнопок заказа
    public void clickOrderButtonTop() {
        driver.findElement(orderButtonTop).click();
    }

    public void clickOrderButtonBottom() {
        WebElement button = driver.findElement(orderButtonBottom);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView();", button
        );
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(button));
        button.click();
    }

    // Методы для логотипов
    public void clickScooterLogo() {
        driver.findElement(scooterLogo).click();
    }

    public void clickYandexLogo() {
        driver.findElement(yandexLogo).click();
    }

    // Метод для получения текущего URL
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}