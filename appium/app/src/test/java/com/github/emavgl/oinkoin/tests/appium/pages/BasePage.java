package com.github.emavgl.oinkoin.tests.appium.pages;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collections;

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

    /**
     * CI/CD WORKAROUND:
     * Performs a "blind" tap at the center of the screen to force the rendering
     * of the first Flutter frame on slow emulators (e.g., GitHub Actions).
     * This unlocks the Semantics Tree which would otherwise result empty due to
     * a race condition in the Flutter embedding on software-rendered environments.
     */
    protected void wakeUpFlutter() {
        System.out.println("[BasePage] Attempting 'Wake Up' tap to trigger Flutter rendering...");
        try {
            Dimension size = driver.manage().window().getSize();
            int x = size.width / 2;
            int y = size.height / 2;

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            
            tap.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), x, y));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(tap));
            
            // Short pause to allow the CPU to process the input and render the frame
            Thread.sleep(1500);
        } catch (Exception e) {
            // Do not fail the test if this workaround fails
            System.err.println("[BasePage] Wake up tap warning: " + e.getMessage());
        }
    }
}
