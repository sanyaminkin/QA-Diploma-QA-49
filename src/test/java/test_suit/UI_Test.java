package test_suit;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataHelper;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import pageobject.TourPurchasePage;

import static com.codeborne.selenide.Selenide.closeWindow;
import static com.codeborne.selenide.Selenide.open;

public class UI_Test {
    TourPurchasePage tourPurchasePage;

    @BeforeAll
    static void allureSetup() {
        SelenideLogger.addListener("allure", new AllureSelenide().
                screenshots(true).savePageSource(false));
    }

    @BeforeEach
    void browserSetUp(){
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
    void shouldSuccessBuyTourOnPaymentPageTest() {
        var paymentPage = tourPurchasePage.payForTour();
        var approvedPayment = DataHelper.getApprovedPayment(DataHelper.randomPlusMonth());
        paymentPage.successfulSendingForm(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    void shouldSuccessBuyTourOnCreditPageTest() {
        var creditPage = tourPurchasePage.buyWithCredit();
        var approvedPayment = DataHelper.getApprovedPayment(DataHelper.randomPlusMonth());
        creditPage.successfulSendingForm(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
    }

    @Test
    void shouldSwitchFromPaymentPageToCreditPageTest() {
        var paymentPage = tourPurchasePage.payForTour();
        var creditPage = paymentPage.buyWithCredit();
    }

    @Test
    void shouldSwitchFromCreditPageToPaymentPageTest() {
        var creditPage = tourPurchasePage.buyWithCredit();
        var paymentPage = creditPage.payForTour();
    }

    @Test
    void shouldRequireFillPaymentFormFieldsTest() {
        var paymentPage = tourPurchasePage.payForTour();
        paymentPage.sendClearForm();
    }

    @Test
    void shouldRequireFillCreditFormFieldsTest() {
        var creditPage = tourPurchasePage.buyWithCredit();
        creditPage.sendClearForm();
    }

    @Test
    void shouldRequireCardNumberOnPaymentPageTest() {
        var paymentPage = tourPurchasePage.payForTour();
        var approvedPayment = DataHelper.getApprovedPayment(DataHelper.randomPlusMonth());
        paymentPage.fillPaymentInfo("", approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        paymentPage.sendInvalidForm();
    }

    @Test
    void shouldRequireMonthOnPaymentPageTest() {
        var paymentPage = tourPurchasePage.payForTour();
        var approvedPayment = DataHelper.getApprovedPayment(DataHelper.randomPlusMonth());
        paymentPage.fillPaymentInfo(approvedPayment.getCardNumber(), "",
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        paymentPage.sendInvalidForm();
    }

    @Test
    void shouldRequireYearOnPaymentPageTest() {
        var paymentPage = tourPurchasePage.payForTour();
        var approvedPayment = DataHelper.getApprovedPayment(DataHelper.randomPlusMonth());
        paymentPage.fillPaymentInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                "", approvedPayment.getCardHolder(), approvedPayment.getCvv());
        paymentPage.sendInvalidForm();
    }

    @Test
    void shouldRequireCardHolderOnPaymentPageTest() {
        var paymentPage = tourPurchasePage.payForTour();
        var approvedPayment = DataHelper.getApprovedPayment(DataHelper.randomPlusMonth());
        paymentPage.fillPaymentInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), "", approvedPayment.getCvv());
        paymentPage.sendInvalidForm();
    }

    @Test
    void shouldRequireCvvOnPaymentPageTest() {
        var paymentPage = tourPurchasePage.payForTour();
        var approvedPayment = DataHelper.getApprovedPayment(DataHelper.randomPlusMonth());
        paymentPage.fillPaymentInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), "");
        paymentPage.sendInvalidForm();
    }

    @Test
    void shouldRequireCardNumberOnCreditPageTest() {
        var creditPage = tourPurchasePage.buyWithCredit();
        var approvedPayment = DataHelper.getApprovedPayment(DataHelper.randomPlusMonth());
        creditPage.fillPaymentInfo("", approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        creditPage.sendInvalidForm();
    }

    @Test
    void shouldRequireMonthOnCreditPageTest() {
        var creditPage = tourPurchasePage.buyWithCredit();
        var approvedPayment = DataHelper.getApprovedPayment(DataHelper.randomPlusMonth());
        creditPage.fillPaymentInfo(approvedPayment.getCardNumber(), "",
                approvedPayment.getYear(), approvedPayment.getCardHolder(), approvedPayment.getCvv());
        creditPage.sendInvalidForm();
    }

    @Test
    void shouldRequireYearOnCreditPageTest() {
        var creditPage = tourPurchasePage.buyWithCredit();
        var approvedPayment = DataHelper.getApprovedPayment(DataHelper.randomPlusMonth());
        creditPage.fillPaymentInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                "", approvedPayment.getCardHolder(), approvedPayment.getCvv());
        creditPage.sendInvalidForm();
    }

    @Test
    void shouldRequireCardHolderOnCreditPageTest() {
        var creditPage = tourPurchasePage.buyWithCredit();
        var approvedPayment = DataHelper.getApprovedPayment(DataHelper.randomPlusMonth());
        creditPage.fillPaymentInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), "", approvedPayment.getCvv());
        creditPage.sendInvalidForm();
    }

    @Test
    void shouldRequireCvvOnCreditPageTest() {
        var creditPage = tourPurchasePage.buyWithCredit();
        var approvedPayment = DataHelper.getApprovedPayment(DataHelper.randomPlusMonth());
        creditPage.fillPaymentInfo(approvedPayment.getCardNumber(), approvedPayment.getMonth(),
                approvedPayment.getYear(), approvedPayment.getCardHolder(), "");
        creditPage.sendInvalidForm();
    }
}