package org.example.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StepScreenshots {
    private static final Logger log = LoggerFactory.getLogger(StepScreenshots.class);

    private StepScreenshots() {
    }

    public static void before(WebDriver driver, String stepName) {
        Allure.step("Before step: " + stepName, () -> attach(driver, "Before - " + stepName));
    }

    public static void after(WebDriver driver, String stepName) {
        Allure.step("After step: " + stepName, () -> attach(driver, "After - " + stepName));
    }

    public static void attach(WebDriver driver, String name) {
        if (!(driver instanceof TakesScreenshot)) {
            log.warn("Driver does not support screenshots for attachment: {}", name);
            return;
        }
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(name, "image/png", new java.io.ByteArrayInputStream(screenshot), ".png");
        } catch (Exception e) {
            log.warn("Failed to capture screenshot: {}", name, e);
        }
    }
}
