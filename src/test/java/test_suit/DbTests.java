package test_suit;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import pageobject.TourPurchasePage;
import tools.DataHelper;
import tools.DatabaseTool;

import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DbTests {

    TourPurchasePage tourPurchasePage;

    @BeforeAll
    static void allureSetup() {
        SelenideLogger.addListener("allure", new AllureSelenide().
                screenshots(true).savePageSource(false));
    }

    @BeforeEach
    void browserSetUp() {
        open("http://localhost:8080/");
        tourPurchasePage = new TourPurchasePage();
    }

    @AfterEach
    void tearDown() {
        closeWindow();
    }

    @AfterAll
    static void tearDownAllure() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("Не должен сохранять номер карты в БД при заказе со страницы оплаты")
    void shouldNotSaveCreditIdOnPaymentPageTest() throws InterruptedException {
        var paymentPage = tourPurchasePage.payForTour();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        paymentPage.fillAndSendPaymentInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        paymentPage.waitAnyNotification();
        assertEquals("null", DatabaseTool.getCreditId());
    }

    @Test
    @DisplayName("Не должен сохранять номер карты в БД при заказе со страницы оформления кредита")
    void shouldNotSaveCreditIdOnCreditPageTest() {
        var creditPage = tourPurchasePage.buyWithCredit();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        creditPage.fillAndSendPaymentInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        creditPage.waitAnyNotification();
        assertEquals("null", DatabaseTool.getCreditId());
    }

    @Test
    @DisplayName("Должен сохранять платеж с действующей карты в БД как одобренный при заказе со страницы оплаты")
    void shouldApprovePaymentsWithApprovedCardOnPaymentPageTest() {
        var paymentPage = tourPurchasePage.payForTour();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        paymentPage.fillAndSendPaymentInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        paymentPage.waitAnyNotification();
        assertEquals("APPROVED", DatabaseTool.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен сохранять платеж с недействительной карты в БД как отклоненный при заказе со страницы оплаты")
    void shouldDeclinePaymentsWithDeclinedCardOnPaymentPageTest() {
        var paymentPage = tourPurchasePage.payForTour();
        var declinedPayment = DataHelper.declinedPayment(DataHelper.randomPlusMonth());
        paymentPage.fillAndSendPaymentInfo(declinedPayment.getCardNumber(), declinedPayment.getMonth(),
                declinedPayment.getYear(), declinedPayment.getCardHolder(), declinedPayment.getCvv());
        paymentPage.waitAnyNotification();
        assertEquals("DECLINED", DatabaseTool.getPaymentStatus());
    }

    @Test
    @DisplayName("Должен сохранять платеж с действующей карты в БД как одобренный при заказе со страницы оформления кредита")
    void shouldApprovePaymentsWithApprovedCardOnCreditPageTest() {
        var creditPage = tourPurchasePage.buyWithCredit();
        var approvedPayment = DataHelper.approvedPayment(DataHelper.randomPlusMonth());
        creditPage.fillAndSendPaymentInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        creditPage.waitAnyNotification();
        assertEquals("APPROVED", DatabaseTool.getCreditStatus());
    }

    @Test
    @DisplayName("Должен сохранять платеж с недействительной карты в БД как отклоненный при заказе со страницы оформления кредита")
    void shouldDeclinePaymentsWithDeclinedCardOnCreditPageTest() {
        var creditPage = tourPurchasePage.buyWithCredit();
        var declinedPayment = DataHelper.declinedPayment(DataHelper.randomPlusMonth());
        creditPage.fillAndSendPaymentInfo(declinedPayment.getCardNumber(), declinedPayment.getMonth(),
                declinedPayment.getYear(), declinedPayment.getCardHolder(), declinedPayment.getCvv());
        creditPage.waitAnyNotification();
        assertEquals("DECLINED", DatabaseTool.getCreditStatus());
    }
}