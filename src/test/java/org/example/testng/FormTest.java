package org.example.testng;

import org.example.pom.FormPom;
import org.example.utils.Driver;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Path;

public class FormTest {

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
        driver = Driver.getDriverFromEnv();
        driver.manage().window().maximize();
    }

    @Test
    public void formTest() {
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

        Assert.assertEquals(formPom.getSubmittedValue("Student Name"), FIRST_NAME + " " + LAST_NAME);
        Assert.assertEquals(formPom.getSubmittedValue("Student Email"), EMAIL);
        Assert.assertEquals(formPom.getSubmittedValue("Gender"), GENDER);
        Assert.assertEquals(formPom.getSubmittedValue("Mobile"), MOBILE);
        Assert.assertEquals(formPom.getSubmittedValue("Date of Birth"), "01 January,2000");
        Assert.assertEquals(formPom.getSubmittedValue("Subjects"), SUBJECT);
        Assert.assertEquals(formPom.getSubmittedValue("Hobbies"), HOBBY);
        Assert.assertEquals(formPom.getSubmittedValue("Picture"), PICTURE_NAME);
        Assert.assertEquals(formPom.getSubmittedValue("Address"), ADDRESS);
        Assert.assertEquals(formPom.getSubmittedValue("State and City"), STATE + " " + CITY);
    }

    @AfterMethod
    public void afterMethod() {
        if (driver != null) {
            driver.quit();
        }
    }
}
