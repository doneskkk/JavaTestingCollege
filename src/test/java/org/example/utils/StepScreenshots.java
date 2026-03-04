package org.example.utils;

import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StepScreenshots {
    private static final Logger log = LoggerFactory.getLogger(StepScreenshots.class);

    private StepScreenshots() {
    }

    @Step("Before step: {stepName}")
    public static void before(WebDriver driver, String stepName) {
        attach(driver, "Before - " + stepName);
    }

    @Step("After step: {stepName}")
    public static void after(WebDriver driver, String stepName) {
        attach(driver, "After - " + stepName);
    }

    @Attachment(value = "{1}", type = "image/png")
    public static byte[] attach(WebDriver driver, String name) {
        if (!(driver instanceof TakesScreenshot)) {
            log.warn("Driver does not support screenshots for attachment: {}", name);
            return new byte[0];
        }
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        } catch (Exception e) {
            log.warn("Failed to capture screenshot: {}", name, e);
            return new byte[0];
        }
    }
}
