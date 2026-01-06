package com.github.emavgl.oinkoin.tests.appium.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {

    protected final AppiumDriver driver;
    protected final WebDriverWait wait;

    @FindBy(id = "home-tab")
    protected WebElement homeTab;
    @FindBy(id = "home-tab-selected")
    protected WebElement homeTabSelected;
    @FindBy(id = "categories-tab")
    protected WebElement categoriesTab;
    @FindBy(id = "categories-tab-selected")
    protected WebElement categoriesTabSelected;
    @FindBy(id = "settings-tab")
    protected WebElement settingsTab;
    @FindBy(id = "settings-tab-selected")
    protected WebElement settingsTabSelected;

    public BasePage(AppiumDriver driver) {
        this.driver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(180));
    }

    public boolean isDisplayed(WebElement webElement) {
        try {
            return webElement.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public void openHomeTab() {
        if (isDisplayed(homeTab))
            homeTab.click();
        else
            homeTabSelected.click();
    }

    public void openCategoriesTab() {
        if (isDisplayed(categoriesTab))
            categoriesTab.click();
        else
            categoriesTabSelected.click();
    }

    public void openSettingsTab() {
        if (isDisplayed(settingsTab))
            settingsTab.click();
        else
            settingsTabSelected.click();
    }

    protected void wakeUpFlutter() {
        System.out.println("[BasePage] Attempting 'Wake Up' tap via ADB...");
        try {
            // Nexus 5X resolution is 1080x1920. Center is approx 540 960.
            // We use hardcoded coordinates to be independent of the driver's window size calculation.
            String cmd = "adb shell input tap 540 960";
            
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
            
            System.out.println("[BasePage] ADB Tap command executed.");
            
            // "Short" pause to allow the CPU to process the input and render the frame
            Thread.sleep(600000);
        } catch (Exception e) {
            System.err.println("[BasePage] ADB Wake up failed: " + e.getMessage());
        }
    }
}
