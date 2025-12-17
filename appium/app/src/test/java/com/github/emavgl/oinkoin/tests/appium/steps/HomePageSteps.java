package com.github.emavgl.oinkoin.tests.appium.steps;

import com.github.emavgl.oinkoin.tests.appium.BaseTest;
import com.github.emavgl.oinkoin.tests.appium.context.ScenarioContext;
import com.github.emavgl.oinkoin.tests.appium.pages.HomePage;
import com.github.emavgl.oinkoin.tests.appium.utils.CategoryType;
import com.github.emavgl.oinkoin.tests.appium.utils.RecordData;
import com.github.emavgl.oinkoin.tests.appium.utils.RepeatOption;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Map;

import static com.github.emavgl.oinkoin.tests.appium.utils.Utils.formatRangeDateText;

public class HomePageSteps {
    private final HomePage homePage;
    private final ScenarioContext context;

    public HomePageSteps(ScenarioContext context) {
        this.context = context;
        this.homePage = new HomePage(BaseTest.getDriver());
    }

    @Given("The app is open on the Homepage")
    public void openAppInHomepage() {
        homePage.openHomeTab();
    }

    //region Date Range Text
    @When("I select the records for the month {string}")
    public void selectRecordsByMonth(String month) {
        homePage.showRecordsPerMonth(Month.valueOf(month.toUpperCase()));
    }

    @When("I select the records for the year {string}")
    public void selectRecordsByYear(String year) {
        homePage.showRecordsPerYear(Year.parse(year));
    }

    @When("I select the records for a custom date range from {int} months and {int} days ago to {int} days ago")
    public void selectRecordsByCustomRange(int startMonthsAgo, int startDaysAgo, int endDaysAgo) {
        LocalDate startDate = LocalDate.now().minusMonths(startMonthsAgo).minusDays(startDaysAgo);
        LocalDate endDate = LocalDate.now().minusDays(endDaysAgo);
        context.set("startDate", startDate);
        context.set("endDate", endDate);
        homePage.showRecordPerDateRange(startDate, endDate);
    }

    @Then("The text of the date range displayed is {string}")
    public void dateRangeTextDisplayed(String expectedText) {
        String actualText = homePage.dateRangeText();
        expectedText = expectedText.replace("<current_year>", String.valueOf(LocalDate.now().getYear()));
        Assert.assertEquals(actualText, expectedText);
    }

    @Then("The text of the date range displayed is correct for that range")
    public void dateRangeTextDisplayedForCustomRange() {
        LocalDate startDate = context.get("startDate");
        LocalDate endDate = context.get("endDate");
        String expectedText = formatRangeDateText(startDate, endDate);
        Assert.assertEquals(homePage.dateRangeText(), expectedText);
    }
    //endregion

    //region Add/Delete Records
    private void createAndSaveRecord(String name, double amount, CategoryType type, String category, RepeatOption repeat, String contextKey) {
        RecordData record = new RecordData(name, amount, type, category, LocalDate.now(), repeat, "Test record");
        context.set(contextKey, record);
        homePage.addRecord(record);
    }
    
    private void createAndSaveRecordWithDate(String name, double amount, CategoryType type, String category, LocalDate date, RepeatOption repeat, String contextKey) {
        RecordData record = new RecordData(name, amount, type, category, date, repeat, "Test record");
        context.set(contextKey, record);
        homePage.addRecord(record);
    }

    @Given("I add a new expense record with the following details:")
    public void addExpenseRecord(DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        createAndSaveRecord(data.get("Name"), Double.parseDouble(data.get("Amount")), CategoryType.EXPENSE, data.get("Category"), RepeatOption.NOT_REPEAT, "record");
    }

    @Given("I add a new income record with the following details:")
    public void addIncomeRecord(DataTable dataTable) {
        Map<String, String> data = dataTable.asMaps().get(0);
        RepeatOption repeat = data.containsKey("Repeat") ? RepeatOption.fromString(data.get("Repeat")) : RepeatOption.NOT_REPEAT;
        createAndSaveRecord(data.get("Name"), Double.parseDouble(data.get("Amount")), CategoryType.INCOME, data.get("Category"), repeat, "record");
    }

    @Then("The expense record {string} for {string} is displayed in the list")
    public void expenseRecordIsDisplayed(String name, String amount) {
        Assert.assertTrue(homePage.isRecordDisplayedInCurrentView(name, CategoryType.EXPENSE, Double.parseDouble(amount)));
    }

    @Then("The income record {string} for {string} is displayed in the list")
    public void incomeRecordIsDisplayed(String name, String amount) {
        Assert.assertTrue(homePage.isRecordDisplayedInCurrentView(name, CategoryType.INCOME, Double.parseDouble(amount)));
    }

    @When("I delete the record {string}")
    public void deleteRecord(String name) {
        RecordData record = context.get("record");
        Assert.assertEquals(name, record.name(), "The record to be deleted is not the one stored in the context.");
        homePage.deleteRecord(record.name(), record.categoryType(), record.amount(), record.date());
    }

    @Then("I delete the record {string} to clean up")
    public void deleteRecordToCleanUp(String name) {
        RecordData record = context.get("record");
        Assert.assertEquals(name, record.name(), "The record to be deleted is not the one stored in the context.");
        homePage.deleteRecord(record.name(), record.categoryType(), record.amount(), record.date());
    }

    @Then("The record {string} is not displayed in the list")
    public void recordIsNotDisplayed(String name) {
        RecordData record = context.get("record");
        Assert.assertFalse(homePage.isRecordDisplayedInCurrentView(record.name(), record.categoryType(), record.amount()));
    }
    //endregion

    //region Filter Records
    @Given("A record {string} for the current month exists")
    public void createRecordForCurrentMonth(String name) {
        createAndSaveRecord(name, 100.0, CategoryType.EXPENSE, "Food", RepeatOption.NOT_REPEAT, "currentMonthRecord");
    }

    @Given("A record {string} for a different month exists")
    public void createRecordForDifferentMonth(String name) {
        createAndSaveRecordWithDate(name, 500.0, CategoryType.EXPENSE, "House", LocalDate.now().minusMonths(2), RepeatOption.NOT_REPEAT, "otherMonthRecord");
    }

    @When("I filter the records by the current month")
    public void filterByCurrentMonth() {
        homePage.showRecordsPerMonth(LocalDate.now().getMonth());
    }

    @Then("The record {string} is displayed")
    public void recordIsDisplayed(String name) {
        RecordData record = context.get("currentMonthRecord");
        if (record == null) record = context.get("currentYearRecord");
        if (record == null) record = context.get("inRangeRecord");
        Assert.assertNotNull(record, "Record '" + name + "' not found in context.");
        Assert.assertTrue(homePage.isRecordDisplayedInCurrentView(record.name(), record.categoryType(), record.amount()));
    }

    @Then("The record {string} is not displayed")
    public void recordIsNotDisplayedAfterFilter(String name) {
        RecordData record = context.get("otherMonthRecord");
        if (record == null) record = context.get("otherYearRecord");
        if (record == null) record = context.get("outOfRangeRecord");
        Assert.assertNotNull(record, "Record '" + name + "' not found in context.");
        Assert.assertFalse(homePage.isRecordDisplayedInCurrentView(record.name(), record.categoryType(), record.amount()));
    }

    @Given("A record {string} for the current year exists")
    public void createRecordForCurrentYear(String name) {
        createAndSaveRecord(name, 2000.0, CategoryType.INCOME, "Salary", RepeatOption.NOT_REPEAT, "currentYearRecord");
    }

    @Given("A record {string} for a different year exists")
    public void createRecordForDifferentYear(String name) {
        createAndSaveRecordWithDate(name, 1500.0, CategoryType.INCOME, "Salary", LocalDate.now().minusYears(1), RepeatOption.NOT_REPEAT, "otherYearRecord");
    }

    @When("I filter the records by the current year")
    public void filterByCurrentYear() {
        homePage.showRecordsPerYear(Year.now());
    }

    @Given("A record {string} within the last 3 weeks exists")
    public void createRecordWithinRange(String name) {
        createAndSaveRecordWithDate(name, 75.0, CategoryType.EXPENSE, "House", LocalDate.now().minusWeeks(2), RepeatOption.NOT_REPEAT, "inRangeRecord");
    }

    @Given("A record {string} outside the last 3 weeks exists")
    public void createRecordOutsideRange(String name) {
        createAndSaveRecordWithDate(name, 30.0, CategoryType.EXPENSE, "Transport", LocalDate.now().minusMonths(2), RepeatOption.NOT_REPEAT, "outOfRangeRecord");
    }

    @When("I filter the records from 3 weeks ago to 1 week ago")
    public void filterByCustomRange() {
        homePage.showRecordPerDateRange(LocalDate.now().minusWeeks(3), LocalDate.now().minusWeeks(1));
    }
    //endregion
}
