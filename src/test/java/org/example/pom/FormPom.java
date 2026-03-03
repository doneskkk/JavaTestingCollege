package org.example.pom;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FormPom {

    private final WebDriver driver;
    private final JavascriptExecutor js;
    private final WebDriverWait wait;

    private final By formsCard = By.xpath("//*[text()='Forms']");
    private final By practiceFormMenu = By.xpath("//*[text()='Practice Form']");

    private final By firstName = By.id("firstName");
    private final By lastName = By.id("lastName");
    private final By userEmail = By.id("userEmail");
    private final By userNumber = By.id("userNumber");
    private final By dateOfBirthInput = By.id("dateOfBirthInput");
    private final By subjectsInput = By.id("subjectsInput");
    private final By uploadPicture = By.id("uploadPicture");
    private final By currentAddress = By.id("currentAddress");
    private final By stateContainer = By.id("state");
    private final By cityContainer = By.id("city");
    private final By submitButton = By.id("submit");
    private final By modalTitle = By.id("example-modal-sizes-title-lg");

    public FormPom(WebDriver driverParam) {
        this.driver = driverParam;
        this.js = (JavascriptExecutor) driverParam;
        this.wait = new WebDriverWait(driverParam, Duration.ofSeconds(15));
    }

    public void openPracticeForm() {
        wait.until(ExpectedConditions.elementToBeClickable(formsCard)).click();
        wait.until(ExpectedConditions.elementToBeClickable(practiceFormMenu)).click();
        closeAdvert();
    }

    public void setFirstName(String value) {
        type(firstName, value);
    }

    public void setLastName(String value) {
        type(lastName, value);
    }

    public void setEmail(String value) {
        type(userEmail, value);
    }

    public void setGender(String gender) {
        By genderLocator = By.xpath("//*[@id='genterWrapper']//label[text()='" + gender + "']");
        wait.until(ExpectedConditions.elementToBeClickable(genderLocator)).click();
    }

    public void setMobile(String value) {
        type(userNumber, value);
    }

    public void setDateOfBirth(int day, String month, String year) {
        wait.until(ExpectedConditions.elementToBeClickable(dateOfBirthInput)).click();
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("react-datepicker__month-select"))))
                .selectByVisibleText(month);
        new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("react-datepicker__year-select"))))
                .selectByVisibleText(year);

        By dayLocator = By.xpath("//div[contains(@class,'react-datepicker__day') and not(contains(@class,'react-datepicker__day--outside-month')) and text()='" + day + "']");
        wait.until(ExpectedConditions.elementToBeClickable(dayLocator)).click();
    }

    public void setSubject(String subject) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(subjectsInput));
        input.sendKeys(subject);
        input.sendKeys(Keys.ENTER);
    }

    public void setHobby(String hobby) {
        By hobbyLocator = By.xpath("//*[@id='hobbiesWrapper']//label[text()='" + hobby + "']");
        wait.until(ExpectedConditions.elementToBeClickable(hobbyLocator)).click();
    }

    public void uploadPicture(String absolutePath) {
        wait.until(ExpectedConditions.presenceOfElementLocated(uploadPicture)).sendKeys(absolutePath);
    }

    public void setCurrentAddress(String value) {
        type(currentAddress, value);
    }

    public void setStateAndCity(String state, String city) {
        wait.until(ExpectedConditions.elementToBeClickable(stateContainer)).click();
        WebElement stateInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("react-select-3-input")));
        stateInput.sendKeys(state);
        stateInput.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.elementToBeClickable(cityContainer)).click();
        WebElement cityInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("react-select-4-input")));
        cityInput.sendKeys(city);
        cityInput.sendKeys(Keys.ENTER);
    }

    public void submit() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", button);
        button.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
    }

    public String getSubmittedValue(String label) {
        By valueLocator = By.xpath("//td[text()='" + label + "']/following-sibling::td");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(valueLocator)).getText().trim();
    }

    public void closeAdvert() {
        try {
            js.executeScript("var elem = document.evaluate(\"//*[@id='adplus-anchor']\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;" +
                    "if (elem && elem.parentNode) { elem.parentNode.removeChild(elem); }");
        } catch (Exception ignored) {
        }
        try {
            js.executeScript("var elem = document.evaluate(\"//footer\", document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue;" +
                    "if (elem && elem.parentNode) { elem.parentNode.removeChild(elem); }");
        } catch (Exception ignored) {
        }
    }

    private void type(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        element.clear();
        element.sendKeys(value);
    }
}
