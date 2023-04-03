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

        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "C:/Selenium/chromedriver.exe");
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            driver = new ChromeDriver(options);
        } else if (browser.equalsIgnoreCase("edge")) {
            System.setProperty("webdriver.edge.driver", "C:\\Selenium\\msedgedriver.exe");
            EdgeOptions option = new EdgeOptions();
            option.setCapability("ms:inPrivate", true);
            driver = new EdgeDriver(option);
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
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        String uniqueUsername = date + username + time;

        if (uniqueUsername.contains("testExistingUser") || email.contains("thisemailexists")) {
            inputInfo(username, email, password);
            //WebElement user = driver.findElement(By.id("new_username"));
            //user.sendKeys(username);
            //WebElement mail = driver.findElement(By.id("email"));
            //mail.sendKeys(email + "@testing.com");
        } else {
            inputInfo(uniqueUsername.replace(".", "").replace(":", "").replace("-", ""), email, password);
            // WebElement user = driver.findElement(By.id("new_username"));
            // user.sendKeys(uniqueUsername.replace(".", "").replace(":", "").replace("-", ""));
            // WebElement mail = driver.findElement(By.id("email"));
            // mail.sendKeys(email + "@testing.com");
        }
    }

    private void inputInfo(String username, String email, String password) {
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

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("create-account-enabled")));
        WebElement button = driver.findElement(By.id("create-account-enabled"));
        action.moveToElement(button).perform();
        button.click();

    }
   /* @Then("i should be {string} and get {string}")
    public void iShouldBeAndGet(String registered, String message) {
        String expectedTitle;
        String expected = message;
        String captcha;

        if (registered.equalsIgnoreCase("yes")) {
            expectedTitle = "Success | Mailchimp";
            wait.until(ExpectedConditions.titleContains(expectedTitle));
            successText = driver.findElement(By.xpath("//*[@id=\"signup-success\"]/div/div[1]/section/div/h1"));

            if (successText.isDisplayed()) {
                assertTrue(successText.getText().contains(message));
                assertEquals(message, successText.getText());

            }
        }
        else if (registered.equalsIgnoreCase("no")) {
            expectedTitle = "Signup | Mailchimp";
            wait.until(ExpectedConditions.titleContains(expectedTitle));
            errorMessage = driver.findElement(By.xpath("//*[@id=\"signup-form\"]/fieldset/div[2]/div/span/text()[1] | //*[@id=\"signup-form\"]/fieldset/div[1]/div/span"));
            error = driver.findElement(By.cssSelector("#signup-form > fieldset > div:nth-child(2) > div > span, #signup-form > fieldset > div:nth-child(2) > div > span"));

            if (errorMessage.isDisplayed() || (error.isDisplayed()) ) {
                assertTrue(errorMessage.getText().contains(message));
                assertEquals(expected, errorMessage.getText());
                assertEquals(message, errorMessage.getText(), error.getText());
            }
            else
                System.out.println("blä");
        }


        //if (!registered.equalsIgnoreCase("yes") && !registered.equalsIgnoreCase("no")) {  //*[@id="signup-form"]/fieldset/div[2]/div/span/text()[1]   //*[@id="signup-form"]/fieldset/div[2]/div/span/text()[2]
          //  System.out.println("captcha located");        #signup-form > fieldset > div:nth-child(2) > div > span   //*[@id="signup-form"]/fieldset/div[1]/div/span  //*[@id="signup-form"]/fieldset/div[2]/div/span

    }*/

    @Then("i should be {string} and get {string}")
    public void iShouldBeAndGet(String registered, String message) {
        String expectedTitle;
        WebElement errorMessage, yesMessage;

        if (registered.equalsIgnoreCase("yes")) {
            expectedTitle = "Success | Mailchimp";
            wait.until(ExpectedConditions.titleContains(expectedTitle));

            // errorMessage = driver.findElement(By.xpath("//*[@id=\"signup-form\"]/fieldset/div[2]/div/span"));
            yesMessage = driver.findElement(By.cssSelector("#signup-success > div > div.content.line.section > section > div > h1"));

            if (yesMessage.isDisplayed()) {
                assertEquals(expectedTitle, driver.getTitle());
                assertTrue(yesMessage.getText().contains(message));
                System.out.println("Actual: " + yesMessage.getText());
                System.out.println("Expected: " + message);
            }

        } else if (registered.equalsIgnoreCase("no")) {
            expectedTitle = "Signup | Mailchimp";
            wait.until(ExpectedConditions.titleContains(expectedTitle));
            //wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#av-flash-errors > ul > li, #signup-form > fieldset > div.line.login-field.margin-bottom--lv2 > div > div:nth-child(1) > div > label, #slot-preShell > div.c-langSelectorHeader.flex.flex-direction--column.flex-grow-1 > div:nth-child(1) > a > img")));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".invalid-error")));

            errorMessage = driver.findElement(By.cssSelector(".invalid-error"));
            //errorMessage = driver.findElement(By.cssSelector("#signup-form > fieldset > div:nth-child(2) > div > span, #signup-form > fieldset > div:nth-child(1) > div > span"));
            //errorMessage = driver.findElement(By.xpath("//*[@id=\"signup-form\"]/fieldset/div[1]/div/span | //*[@id=\"signup-form\"]/fieldset/div[2]/div/span"));
            if (errorMessage.getText().contains(message))
                System.out.println("Actual: " + errorMessage.getText());
            System.out.println("Expected: " + message);
            assertTrue(errorMessage.isDisplayed());
            //assertEquals(expectedTitle, driver.getTitle());
            assertThat(errorMessage.getText(), containsString(message));
            //assertEquals(message, errorMessage.getText());
            System.out.println(message + " " + errorMessage.getText());

        } else
            System.out.println("A wild captcha appeared! Maybe try catching it with a pokeball?");

    }


/*
    @Then("i should be {string} and get {string}")
    public void iShouldBeAndGet(String registered, String message) {
        String actualMessage;
        String expected;
        if (registered.equalsIgnoreCase("yes")) {
            wait.until(ExpectedConditions.titleContains("Success | Mailchimp"));
            expected = "Success | Mailchimp";
            WebElement success = driver.findElement(By.cssSelector(".margin-right--lv4 > a:nth-child(1), div.float--left:nth-child(3) > p:nth-child(2) > a:nth-child(1), #resend-email-link"));

            if (success.isDisplayed()) {
                assertEquals(expected, driver.getTitle());
                System.out.println(message);
            } else {
                expected = "recaptcha challenge expires in two minutes";
                wait.until(ExpectedConditions.titleContains("recaptcha challenge expires in two minutes"));
                // If the success element is not displayed, it means the captcha was shown  #recaptcha-token html
                //WebElement captcha = driver.findElement(By.cssSelector("#recaptcha-token, #rc-imageselect > div.rc-footer > div.rc-controls > div.rc-challenge-help > a, #recaptcha-help-button"));  body > div:nth-child(16) > div:nth-child(2) > iframe
                //WebElement captcha = driver.findElement(By.id("recaptcha-verify-button"));  head title reCAPTCHA
                //WebElement captcha = driver.findElement(By.xpath("/html/body/script/text()"));
                //assertTrue(captcha.isDisplayed());
                assertEquals(expected, driver.getTitle());
            }
        } else if (registered.equalsIgnoreCase("no")) {
            errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#signup-form > fieldset > div:nth-child(1) > div > span, #signup-form > fieldset > div:nth-child(2) > div > span")));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("padding--lv3 !padding-top-bottom--lv0")));
            expected = "Signup | Mailchimp";

            WebElement noSuccess = driver.findElement(By.cssSelector("#marketing_newsletter, .c-headingSignUp > span:nth-child(1), .c-recaptchaDisclaimer > span:nth-child(1), .invalid-error"));

            actualMessage = errorMessage.getText();
            assertTrue(noSuccess.isDisplayed());
            assertEquals(message, actualMessage);
        } else {

            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#rc-imageselect > div.rc-footer > div.rc-controls > div.rc-challenge-help > a")));
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#recaptcha-reload-button, #recaptcha-audio-button, #recaptcha-help-button, #recaptcha-verify-button")));
        }
    }
*/


/*
    @Then("i should be {string}")
    public void iShouldBe(String registered) {
        //wait.until(ExpectedConditions.titleContains("Success | Mailchimp"));

        String errorMessage;
        String expected;
        if (registered.equalsIgnoreCase("yes")) {
            wait.until(ExpectedConditions.titleContains("Success | Mailchimp"));
            expected = "Success | Mailchimp";
            WebElement success = driver.findElement(By.cssSelector(".margin-right--lv4 > a:nth-child(1), div.float--left:nth-child(3) > p:nth-child(2) > a:nth-child(1), #resend-email-link"));

            if (success.isDisplayed()) {
                assertEquals(expected, driver.getTitle());
                System.out.println("jippi");
            } else {
                expected = "recaptcha challenge expires in two minutes";
                wait.until(ExpectedConditions.titleContains("recaptcha challenge expires in two minutes"));
                // If the success element is not displayed, it means the captcha was shown  #recaptcha-token html
                //WebElement captcha = driver.findElement(By.cssSelector("#recaptcha-token, #rc-imageselect > div.rc-footer > div.rc-controls > div.rc-challenge-help > a, #recaptcha-help-button"));  body > div:nth-child(16) > div:nth-child(2) > iframe
                //WebElement captcha = driver.findElement(By.id("recaptcha-verify-button"));  head title reCAPTCHA
                //WebElement captcha = driver.findElement(By.xpath("/html/body/script/text()"));
                //assertTrue(captcha.isDisplayed());
                assertEquals(expected, driver.getTitle());
            }
        } else if (registered.equalsIgnoreCase("no")) {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.className("padding--lv3 !padding-top-bottom--lv0")));
            expected = "Signup | Mailchimp";

            WebElement noSuccess = driver.findElement(By.cssSelector("#marketing_newsletter, .c-headingSignUp > span:nth-child(1), .c-recaptchaDisclaimer > span:nth-child(1), .invalid-error"));

            assertTrue(noSuccess.isDisplayed());
            assertEquals(expected, driver.getTitle());
        } else {

            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#rc-imageselect > div.rc-footer > div.rc-controls > div.rc-challenge-help > a")));
            wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#recaptcha-reload-button, #recaptcha-audio-button, #recaptcha-help-button, #recaptcha-verify-button")));
        }
    }*/

      /*  wait.until(ExpectedConditions.titleContains("Success | Mailchimp"));
        String expected;

        if (registered.equalsIgnoreCase("yes")) {
            wait.until(ExpectedConditions.titleContains("Success | Mailchimp"));
            expected = "Success | Mailchimp";
            //css-path till 3 element
            WebElement success = driver.findElement(By.cssSelector(".margin-right--lv4 > a:nth-child(1), div.float--left:nth-child(3) > p:nth-child(2) > a:nth-child(1), #resend-email-link"));

            assertTrue(success.isDisplayed());
            assertEquals(expected, driver.getTitle());

            //WebElement success = driver.findElement(By.cssSelector(".\\!margin-bottom--lv3"));
            //assertEquals(expected, "Check your email");
            //driver.getTitle().equals("Success | Mailchimp");

        } else if (registered.equalsIgnoreCase("no")) {
            wait.until(ExpectedConditions.titleContains("Signup | Mailchimp"));
            expected = "Signup | Mailchimp";

            WebElement noSuccess = driver.findElement(By.cssSelector("#marketing_newsletter, .c-headingSignUp > span:nth-child(1), .c-recaptchaDisclaimer > span:nth-child(1)"));

            assertTrue(noSuccess.isDisplayed());
            assertEquals(expected, driver.getTitle());    <button class="rc-button-default goog-inline-block" title="" value="" id="recaptcha-verify-button" tabindex="0">Verify</button>
        }
        //wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("#recaptcha-reload-button, #recaptcha-audio-button, #recaptcha-help-button, #recaptcha-verify-button")));
    }*/

    @After
    public void tearDown() {
        driver.close();
        driver.quit();
    }
}



