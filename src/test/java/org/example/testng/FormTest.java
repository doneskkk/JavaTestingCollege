package org.example.testng;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import io.qameta.allure.Step;
import io.qameta.allure.testng.AllureTestNg;
import org.example.pom.FormPom;
import org.example.utils.Driver;
import org.example.utils.StepScreenshots;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.nio.file.Path;

@Listeners({AllureTestNg.class})
@Epic("DemoQA")
@Feature("Practice Form")
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
    @Story("Fill and validate all fields")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Fills all fields in Practice Form, submits and verifies each value in the result modal.")
    public void formTest() {
        log.info("Opening URL: {}", URL);
        StepScreenshots.before(driver, "Open base URL");
        driver.get(URL);
        StepScreenshots.after(driver, "Open base URL");
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
        Allure.addAttachment("Test Data Summary", "text/plain",
                "Name: " + FIRST_NAME + " " + LAST_NAME + "\n" +
                        "Email: " + EMAIL + "\n" +
                        "Mobile: " + MOBILE + "\n" +
                        "Date of birth: 01 January,2000\n" +
                        "Subject: " + SUBJECT + "\n" +
                        "Hobby: " + HOBBY + "\n" +
                        "Picture: " + PICTURE_NAME + "\n" +
                        "Address: " + ADDRESS + "\n" +
                        "State and City: " + STATE + " " + CITY);
    }

    @AfterMethod
    public void afterMethod() {
        if (driver != null) {
            log.info("Closing driver");
            driver.quit();
            log.info("Driver closed");
        }
    }

    @Step("Verify submitted field {field}")
    private void assertSubmittedValue(FormPom formPom, String field, String expected) {
        StepScreenshots.before(driver, "Assert field: " + field);
        String actual = formPom.getSubmittedValue(field);
        log.info("Asserting [{}]: expected='{}', actual='{}'", field, expected, actual);
        Assert.assertEquals(actual, expected, "Mismatch for field: " + field);
        StepScreenshots.after(driver, "Assert field: " + field);
    }
}
