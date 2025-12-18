package com.github.emavgl.oinkoin.tests.appium;

import com.github.emavgl.oinkoin.tests.appium.utils.Constants;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class BaseTest {
    protected static AndroidDriver driver;

    public static AndroidDriver getDriver() {
        return driver;
    }

    public void setUp() {
        if (driver == null) {
            UiAutomator2Options options = new UiAutomator2Options()
                    .setAutomationName("UiAutomator2")
                    .setPlatformName(Constants.PLATFORM_NAME)
                    .setNoReset(false)
                    .setFullReset(true)
                    .amend("appium:settings[disableIdLocatorAutocompletion]", true)
                    .amend("appium:newCommandTimeout", 3600);

            // --- CI vs LOCAL CONFIGURATION STRATEGY ---
            
            // Check if 'appPath' is passed via Gradle (CI Environment)
            String ciAppPath = System.getProperty("appPath");

            if (ciAppPath != null && !ciAppPath.isEmpty()) {
                // CASE A: RUNNING ON GITHUB ACTIONS (CI)
                System.out.println("[CI] CI Environment detected.");
                System.out.println("[CI] Using injected APK path: " + ciAppPath);
                
                options.setApp(ciAppPath);
                
                // In CI, we use the device provided by the runner (e.g. Android 10).
                options.setDeviceName("Android Emulator");

            } else {
                // CASE B: RUNNING LOCALLY (IDE/Terminal)
                System.out.println("[Local] Local Environment detected.");
                System.out.println("[Local] Using Constants path: " + Constants.APP_PATH);
                
                options.setApp(Constants.APP_PATH);
                options.setPlatformVersion(Constants.PLATFORM_VERSION);
                options.setUdid(Constants.UDID);
                options.setAppPackage(Constants.APP_PACKAGE);
            }

            try {
                driver = new AndroidDriver(getAppiumServerUrl(), options);
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize Appium Driver. Check if Appium Server is running.", e);
            }
        }
    }

    private URL getAppiumServerUrl() {
        try {
            return new URL(Constants.APPIUM_SERVER_URL);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL for Appium server", e);
        }
    }

    public void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}