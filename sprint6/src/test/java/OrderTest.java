package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import pageobjects.MainPage;
import pageobjects.OrderPage;
import utilities.WebDriverFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    private WebDriver driver;
    private MainPage mainPage;
    private OrderPage orderPage;

    @BeforeEach
    public void setUp() {
        driver = WebDriverFactory.getWebDriver("chrome");
        driver.get("https://qa-scooter.praktikum-services.ru/");
        mainPage = new MainPage(driver);
        orderPage = new OrderPage(driver);
    }

    // Параметризация для тестов заказа с разными данными
    static Stream<Arguments> orderDataProvider() {
        return Stream.of(
                // Первый набор данных
                Arguments.of(
                        "top",  // точка входа (top или bottom)
                        "Иван", // имя
                        "Иванов", // фамилия
                        "ул. Пушкина, д. 10", // адрес
                        "Сокольники", // станция метро
                        "+79991234567", // телефон
                        "25.12.2023", // дата
                        "трое суток", // срок аренды
                        "black", // цвет
                        "Позвоните за час" // комментарий
                ),
                // Второй набор данных
                Arguments.of(
                        "bottom",
                        "Мария",
                        "Петрова",
                        "пр. Ленина, д. 25",
                        "Черкизовская",
                        "+79998765432",
                        "30.12.2023",
                        "сутки",
                        "grey",
                        "Оставьте у двери"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("orderDataProvider")
    public void testOrderFlow(
            String entryPoint,
            String name,
            String surname,
            String address,
            String station,
            String phone,
            String date,
            String period,
            String color,
            String comment
    ) {
        // Выбор точки входа
        if ("top".equals(entryPoint)) {
            mainPage.clickOrderButtonTop();
        } else {
            mainPage.clickOrderButtonBottom();
        }

        // Заполнение первой части формы
        orderPage.fillFirstPart(name, surname, address, station, phone);

        // Заполнение второй части формы
        orderPage.fillSecondPart(date, period, color, comment);

        // Подтверждение заказа
        orderPage.clickYesButton();

        // Проверка успешного оформления
        assertTrue(orderPage.isOrderSuccessModalDisplayed(),
                "Модальное окно об успешном оформлении заказа не появилось");
    }

    // Тест на проверку ошибок валидации
    @ParameterizedTest
    @MethodSource("invalidDataProvider")
    public void testValidationErrors(
            String name,
            String surname,
            String address,
            String station,
            String phone,
            int expectedErrors
    ) {
        mainPage.clickOrderButtonTop();
        orderPage.fillFirstPart(name, surname, address, station, phone);

        // Проверка, что количество ошибок соответствует ожидаемому
        int actualErrors = orderPage.getErrorMessages().size();
        assertEquals(expectedErrors, actualErrors,
                "Количество ошибок валидации не совпадает");
    }

    static Stream<Arguments> invalidDataProvider() {
        return Stream.of(
                Arguments.of("", "Иванов", "ул. Пушкина, д. 10", "Сокольники", "+79991234567", 1),
                Arguments.of("Иван", "", "ул. Пушкина, д. 10", "Сокольники", "+79991234567", 1),
                Arguments.of("Иван", "Иванов", "", "Сокольники", "+79991234567", 1),
                Arguments.of("Иван", "Иванов", "ул. Пушкина, д. 10", "", "+79991234567", 1),
                Arguments.of("Иван", "Иванов", "ул. Пушкина, д. 10", "Сокольники", "", 1),
                Arguments.of("", "", "", "", "", 5)
        );
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}