package com.github.emavgl.oinkoin.tests.appium.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.Test;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.github.emavgl.oinkoin.tests.appium.hooks", "com.github.emavgl.oinkoin.tests.appium.steps"},
        plugin = {
                "pretty",
                "html:build/reports/cucumber-report.html",
                "testng:build/testng-output/cucumber.xml",
                "json:build/reports/cucumber.json"
        }
)
@Test
public class CucumberRunner extends AbstractTestNGCucumberTests {

}
