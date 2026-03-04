package org.example.testng;

import org.example.pom.FormPom;
import org.example.utils.Driver;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Path;

public class FormTest {
    private static final Logger log = LoggerFactory.getLogger(FormTest.class);

    static public WebDriver driver;
    static public String URL = "https://demoqa.com/";

    static public String FIRST_NAME = "Chiril";
    static public String LAST_NAME = "Dones";
    static public String EMAIL = "chirildones@gmail.com";
    static public String GENDER = "Male";
    static public String MOBILE = "9911122233";
    static public int BIRTH_DAY = 1;
    static public String BIRTH_MONTH = "January";
    static public String BIRTH_YEAR = "2000";
    static public String SUBJECT = "Maths";
    static public String HOBBY = "Sports";
    static public String ADDRESS = "Chisinau, Test street 10";
    static public String STATE = "NCR";
    static public String CITY = "Delhi";

    static public String PICTURE_NAME = "test-picture.txt";

    @BeforeMethod
    public void beforeMethod() {
        log.info("Starting test setup");
        driver = Driver.getDriverFromEnv();
        driver.manage().window().maximize();
        log.info("Driver initialized and window maximized");
    }

    @Test
    public void formTest() {
        log.info("Opening URL: {}", URL);
        driver.get(URL);
        FormPom formPom = new FormPom(driver);

        formPom.openPracticeForm();
        formPom.setFirstName(FIRST_NAME);
        formPom.setLastName(LAST_NAME);
        formPom.setEmail(EMAIL);
        formPom.setGender(GENDER);
        formPom.setMobile(MOBILE);
        formPom.setDateOfBirth(BIRTH_DAY, BIRTH_MONTH, BIRTH_YEAR);
        formPom.setSubject(SUBJECT);
        formPom.setHobby(HOBBY);

        String picturePath = Path.of("src", "test", "resources", "test-data", PICTURE_NAME)
                .toAbsolutePath()
                .toString();
        formPom.uploadPicture(picturePath);

        formPom.setCurrentAddress(ADDRESS);
        formPom.setStateAndCity(STATE, CITY);
        formPom.submit();

        assertSubmittedValue(formPom, "Student Name", FIRST_NAME + " " + LAST_NAME);
        assertSubmittedValue(formPom, "Student Email", EMAIL);
        assertSubmittedValue(formPom, "Gender", GENDER);
        assertSubmittedValue(formPom, "Mobile", MOBILE);
        assertSubmittedValue(formPom, "Date of Birth", "01 January,2000");
        assertSubmittedValue(formPom, "Subjects", SUBJECT);
        assertSubmittedValue(formPom, "Hobbies", HOBBY);
        assertSubmittedValue(formPom, "Picture", PICTURE_NAME);
        assertSubmittedValue(formPom, "Address", ADDRESS);
        assertSubmittedValue(formPom, "State and City", STATE + " " + CITY);
        log.info("All form assertions passed");
    }

    @AfterMethod
    public void afterMethod() {
        if (driver != null) {
            log.info("Closing driver");
            driver.quit();
            log.info("Driver closed");
        }
    }

    private void assertSubmittedValue(FormPom formPom, String field, String expected) {
        String actual = formPom.getSubmittedValue(field);
        log.info("Asserting [{}]: expected='{}', actual='{}'", field, expected, actual);
        Assert.assertEquals(actual, expected, "Mismatch for field: " + field);
    }
}
