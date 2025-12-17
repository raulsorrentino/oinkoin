package com.github.emavgl.oinkoin.tests.appium.hooks;

import com.github.emavgl.oinkoin.tests.appium.BaseTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Hooks extends BaseTest {
    @Before
    public void setUpScenario() {
        System.out.println("Setting up scenario...");
        if (getDriver() == null) {
            setUp();
        }
    }

    @After
    public void tearDownScenario() {
        System.out.println("Tearing down scenario...");
        tearDown();
    }
}
