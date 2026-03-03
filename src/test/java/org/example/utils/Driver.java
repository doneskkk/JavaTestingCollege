package org.example.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Driver {
    static public WebDriver getAutoLocalDriver() {
        WebDriverManager.chromedriver().setup(); // sets up ChromeDriver automatically
        return new ChromeDriver();
    }

    static public WebDriver getLocalDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\User\\Downloads\\chromedriver-win64");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        return new ChromeDriver(options);
    }

    public static WebDriver getDriverFromEnv() {
        Map<String, String> env = System.getenv();
        if ("true".equalsIgnoreCase(env.getOrDefault("USE_REMOTE_DRIVER", "false"))) {
            return getRemoteDriver();
        }
        return getAutoLocalDriver();
    }

    public static RemoteWebDriver getRemoteDriver() {
        ChromeOptions options = new ChromeOptions();
        options.setCapability("browserVersion", "128.0");
        options.setCapability("selenoid:options", new HashMap<String, Object>() {{
            /* How to add test badge */
            put("name", "Test badge...");

            /* How to set session timeout */
            put("sessionTimeout", "15m");

            /* How to set timezone */
            put("env", new ArrayList<String>() {{
                add("TZ=UTC");
            }});

            /* How to add "trash" button */
            put("labels", new HashMap<String, Object>() {{
                put("manual", "true");
            }});

            /* How to enable video recording */
            put("enableVideo", true);
            put("enableVNC", true);
            put("enableLog", true);
            put("noSandbox", true);
            put("headless", true);
        }});
        try {
            String selenoidUrl = System.getenv().getOrDefault("SELENOID_URL", "http://localhost:4444/wd/hub");
            RemoteWebDriver remoteDriver = new RemoteWebDriver(new URL(selenoidUrl), options);
            remoteDriver.setFileDetector(new LocalFileDetector());
            return remoteDriver;
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid SELENOID_URL value", e);
        }
    }
}
