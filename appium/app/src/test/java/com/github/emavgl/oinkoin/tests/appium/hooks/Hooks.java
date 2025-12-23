package com.github.emavgl.oinkoin.tests.appium.hooks;

import com.github.emavgl.oinkoin.tests.appium.BaseTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hooks extends BaseTest {
    @Before
    public void setUpScenario() {
        System.out.println("Setting up scenario...");
        if (getDriver() == null) {
            setUp();
        }
    }

    @After
    public void tearDownScenario(Scenario scenario) {
        // --- Screenshot on Failure ---
        if (scenario.isFailed() && BaseTest.getDriver() != null) {
            try {
                System.out.println("[HOOKS] Scenario failed! Taking screenshot...");

                final byte[] screenshot = BaseTest.getDriver().getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot on Failure");

                System.out.println("[HOOKS] Page Source on Failure:");
                System.out.println(BaseTest.getDriver().getPageSource());

            } catch (Exception e) {
                System.err.println("[HOOKS] Failed to take screenshot: " + e.getMessage());
            }
        }

        new BaseTest().tearDown();
    }
}
