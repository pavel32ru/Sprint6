package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.openqa.selenium.WebDriver;
import pageobjects.MainPage;
import utilities.WebDriverFactory;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AccordionTest {
    private WebDriver driver;
    private MainPage mainPage;

    @BeforeEach
    public void setUp() {
        driver = WebDriverFactory.getWebDriver("chrome");
        driver.get("https://qa-scooter.praktikum-services.ru/");
        mainPage = new MainPage(driver);
    }

    // Параметризованный тест для проверки всех вопросов
    static Stream<Arguments> accordionDataProvider() {
        return Stream.of(
                Arguments.of(0, "Сутки — 400 рублей. Оплата курьеру — наличными или картой."),
                Arguments.of(1, "Пока что у нас так: один заказ — один самокат. Если хотите покататься с друзьями, можете просто сделать несколько заказов — один за другим."),
                Arguments.of(2, "Допустим, вы оформляете заказ на 8 мая. Мы привозим самокат 8 мая в течение дня. Отсчёт времени аренды начинается с момента, когда вы оплатите заказ курьеру. Если мы привезли самокат 8 мая в 20:30, суточная аренда закончится 9 мая в 20:30."),
                Arguments.of(3, "Только начиная с завтрашнего дня. Но скоро станем расторопнее."),
                Arguments.of(4, "Пока что нет! Но если что-то срочное — всегда можно позвонить в поддержку по красивому номеру 1010."),
                Arguments.of(5, "Самокат приезжает к вам с полной зарядкой. Этого хватает на восемь суток — даже если будете кататься без передышек и во сне. Зарядка не понадобится."),
                Arguments.of(6, "Да, пока самокат не привезли. Штрафа не будет, объяснительной записки тоже не попросим. Все же свои."),
                Arguments.of(7, "Да, обязательно. Всем самокатов! И Москве, и Московской области.")
        );
    }

    @ParameterizedTest
    @MethodSource("accordionDataProvider")
    public void testAccordionItem(int index, String expectedAnswer) {
        mainPage.clickAccordionItem(index);
        assertTrue(mainPage.isAccordionAnswerDisplayed(index),
                "Ответ на вопрос " + index + " не отображается");

        String actualAnswer = mainPage.getAccordionAnswerText(index);
        assertEquals(expectedAnswer, actualAnswer,
                "Текст ответа не совпадает для вопроса " + index);
    }

    @Test
    public void testScooterLogoRedirect() {
        mainPage.clickScooterLogo();
        String currentUrl = mainPage.getCurrentUrl();
        assertEquals("https://qa-scooter.praktikum-services.ru/", currentUrl,
                "Клик по логотипу Самоката не ведет на главную страницу");
    }

    @Test
    public void testYandexLogoRedirect() {
        String originalWindow = driver.getWindowHandle();
        mainPage.clickYandexLogo();

        // Ждем открытия новой вкладки
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Переключаемся на новую вкладку
        for (String windowHandle : driver.getWindowHandles()) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }

        String currentUrl = driver.getCurrentUrl();
        assertTrue(currentUrl.contains("dzen.ru") || currentUrl.contains("yandex.ru"),
                "Клик по логотипу Яндекса не ведет на страницу Яндекса");

        driver.close();
        driver.switchTo().window(originalWindow);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}