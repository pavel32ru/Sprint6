package pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OrderPage {
    private final WebDriver driver;

    // Локаторы для первой части формы
    private final By nameField = By.xpath(".//input[@placeholder='* Имя']");
    private final By surnameField = By.xpath(".//input[@placeholder='* Фамилия']");
    private final By addressField = By.xpath(".//input[@placeholder='* Адрес: куда привезти заказ']");
    private final By metroField = By.xpath(".//input[@placeholder='* Станция метро']");
    private final By phoneField = By.xpath(".//input[@placeholder='* Телефон: на него позвонит курьер']");
    private final By nextButton = By.xpath(".//button[text()='Далее']");

    // Локаторы для второй части формы
    private final By dateField = By.xpath(".//input[@placeholder='* Когда привезти самокат']");
    private final By rentalPeriodField = By.className("Dropdown-placeholder");
    private final By rentalPeriodOptions = By.className("Dropdown-option");
    private final By colorBlack = By.id("black");
    private final By colorGrey = By.id("grey");
    private final By commentField = By.xpath(".//input[@placeholder='Комментарий для курьера']");
    private final By orderButton = By.xpath(".//button[text()='Заказать']");
    private final By yesButton = By.xpath(".//button[text()='Да']");
    private final By orderModal = By.className("Order_Modal__YZ-d3");
    private final By orderSuccess = By.xpath(".//div[contains(@class, 'Order_ModalHeader')]");

    // Локаторы для сообщений об ошибках
    private final By errorMessages = By.xpath(".//div[contains(@class, 'Input_ErrorMessage')]");

    public OrderPage(WebDriver driver) {
        this.driver = driver;
    }

    // Методы для заполнения первой части формы
    public void fillName(String name) {
        driver.findElement(nameField).sendKeys(name);
    }

    public void fillSurname(String surname) {
        driver.findElement(surnameField).sendKeys(surname);
    }

    public void fillAddress(String address) {
        driver.findElement(addressField).sendKeys(address);
    }

    public void fillMetroStation(String station) {
        driver.findElement(metroField).click();
        driver.findElement(metroField).sendKeys(station);
        driver.findElement(metroField).sendKeys(Keys.DOWN, Keys.ENTER);
    }

    public void fillPhone(String phone) {
        driver.findElement(phoneField).sendKeys(phone);
    }

    public void clickNextButton() {
        driver.findElement(nextButton).click();
    }

    // Метод для заполнения всей первой части
    public void fillFirstPart(String name, String surname, String address, String station, String phone) {
        fillName(name);
        fillSurname(surname);
        fillAddress(address);
        fillMetroStation(station);
        fillPhone(phone);
        clickNextButton();
    }

    // Методы для заполнения второй части формы
    public void fillDate(String date) {
        driver.findElement(dateField).sendKeys(date);
        driver.findElement(dateField).sendKeys(Keys.ENTER);
    }

    public void selectRentalPeriod(String period) {
        driver.findElement(rentalPeriodField).click();
        List<WebElement> options = driver.findElements(rentalPeriodOptions);
        for (WebElement option : options) {
            if (option.getText().equals(period)) {
                option.click();
                break;
            }
        }
    }

    public void selectColor(String color) {
        if (color.equals("black")) {
            driver.findElement(colorBlack).click();
        } else if (color.equals("grey")) {
            driver.findElement(colorGrey).click();
        }
    }

    public void fillComment(String comment) {
        driver.findElement(commentField).sendKeys(comment);
    }

    public void clickOrderButton() {
        driver.findElement(orderButton).click();
    }

    public void clickYesButton() {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(yesButton));
        driver.findElement(yesButton).click();
    }

    // Метод для заполнения всей второй части
    public void fillSecondPart(String date, String period, String color, String comment) {
        fillDate(date);
        selectRentalPeriod(period);
        selectColor(color);
        fillComment(comment);
        clickOrderButton();
    }

    // Методы для проверок
    public boolean isOrderSuccessModalDisplayed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(orderSuccess));
            return driver.findElement(orderSuccess).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public List<WebElement> getErrorMessages() {
        return driver.findElements(errorMessages);
    }

    public boolean isFieldErrorDisplayed(String fieldName) {
        // Проверка ошибок для конкретных полей
        String xpath = String.format(
                ".//input[@placeholder='* %s']/following-sibling::div[contains(@class, 'Input_ErrorMessage')]",
                fieldName
        );
        return !driver.findElements(By.xpath(xpath)).isEmpty();
    }
}