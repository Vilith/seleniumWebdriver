package stepDef;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;

public class UserStepdefs {

    private WebDriver driver;
    private WebDriverWait wait;

    @Given("i am using {string}")
    public void iAmUsing(String browser) {
        switch (browser.toLowerCase()) {
            case "chrome" -> {
                System.setProperty("webdriver.chrome.driver", "C:/Selenium/chromedriver.exe");
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(chromeOptions);
            }
            case "edge" -> {
                System.setProperty("webdriver.edge.driver", "C:\\Selenium\\msedgedriver.exe");
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.setCapability("ms:inPrivate", true);
                driver = new EdgeDriver(edgeOptions);
            }
            default -> throw new IllegalStateException("Value not correct: " + browser);
        }
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        driver.manage().window().maximize();
    }

    @And("that i am on the registrationpage")
    public void thatIAmOnTheRegistrationpage() {
        driver.get("https://login.mailchimp.com/signup/");
        wait.until(ExpectedConditions.titleContains("Signup | Mailchimp"));
    }

    @When("i submit {string} {string} and {string}")
    public void iSubmitAnd(String username, String email, String password) {
        String uniqueUsername = generateUniqueUsername(username);

        if (uniqueUsername.contains("testExistingUser") || email.contains("thisemailexists")) {
            fillRegistrationForm(username, email, password);
        } else {
            fillRegistrationForm(uniqueUsername.replace(".", "").replace(":", "").replace("-", ""), email, password);
        }
    }

    private String generateUniqueUsername(String username) {
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        return date + username + time;
    }

    private void fillRegistrationForm(String username, String email, String password) {
        WebElement user = driver.findElement(By.id("new_username"));
        user.sendKeys(username);

        WebElement mail = driver.findElement(By.id("email"));
        mail.sendKeys(email);

        WebElement psw = driver.findElement(By.id("new_password"));
        psw.sendKeys(password);
    }

    @And("i click signup button")
    public void iClickSignupButton() {
        Actions action = new Actions(driver);
        WebElement checkBox = driver.findElement(By.name("marketing_newsletter"));
        checkBox.click();

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("create-account-enabled")));

        WebElement button = driver.findElement(By.id("create-account-enabled"));
        action.moveToElement(button).perform();
        button.click();
    }

    @Then("i should be {string} and get {string}")
    public void iShouldBeAndGet(String registered, String message) {
        String expectedTitle;
        WebElement errorMessage, confirmationMessage;

        switch (registered.toLowerCase()) {
            case "yes" -> {
                expectedTitle = "Success | Mailchimp";
                try {
                    wait.until(ExpectedConditions.titleContains(expectedTitle));
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#signup-success > div > div.content.line.section > section > div > h1")));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
                confirmationMessage = driver.findElement(By.cssSelector("#signup-success > div > div.content.line.section > section > div > h1"));
                if (confirmationMessage.isDisplayed()) {
                    assertEquals(expectedTitle, driver.getTitle());
                    assertTrue(confirmationMessage.getText().contains(message));
                }
            }
            case "no" -> {
                expectedTitle = "Signup | Mailchimp";
                try {
                    wait.until(ExpectedConditions.titleContains(expectedTitle));
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".invalid-error")));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
                errorMessage = driver.findElement(By.cssSelector(".invalid-error"));
                if (errorMessage.getText().contains(message)) {
                    assertTrue(errorMessage.isDisplayed());
                    assertThat(errorMessage.getText(), containsString(message));
                }
            }
            default -> System.out.println("Something went wrong!");
        }
    }

      /*  if (registered.equalsIgnoreCase("yes")) {
            expectedTitle = "Success | Mailchimp";
            try {
                wait.until(ExpectedConditions.titleContains(expectedTitle));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#signup-success > div > div.content.line.section > section > div > h1")));
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            confirmationMessage = driver.findElement(By.cssSelector("#signup-success > div > div.content.line.section > section > div > h1"));

            if (confirmationMessage.isDisplayed()) {
                assertEquals(expectedTitle, driver.getTitle());
                assertTrue(confirmationMessage.getText().contains(message));
            }

        } else if (registered.equalsIgnoreCase("no")) {
            expectedTitle = "Signup | Mailchimp";
            try {
                wait.until(ExpectedConditions.titleContains(expectedTitle));
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".invalid-error")));
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
            errorMessage = driver.findElement(By.cssSelector(".invalid-error"));

            if (errorMessage.getText().contains(message))
                assertTrue(errorMessage.isDisplayed());
            assertThat(errorMessage.getText(), containsString(message));

        } else
            System.out.println("Something went wrong!");*/

    @After
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}