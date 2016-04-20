import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;

public class SystemAuthentication {

    private WebDriver driver;
    private static final String time = Long.toString(new Date().getTime());
    private static final String username = "username";
    private static final String password = "password";
    private static final String eMail = time + "@abv.bg";

    @Before
    public void setUp() {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public void _1userEnter_WithoutLogIn_ShouldBeUnLogin() {
        //Use Case ?1: Login like Guest user (without registration)
        driver.get("https://naturalmilk.ecwid.com/");
        driver.manage().window().maximize();

        WebElement signIn = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[1]/td/table/tbody/tr/td/div/div/a"));
        assertEquals("Sign In", signIn.getText());
    }

    @Test
    public void _2userRegister_WithFreeEmail_ShouldBeRegister() {
        //Use Case ?4: Register as a user
        registerUser(eMail);

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        WebElement hello = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[1]/td/table/tbody/tr/td/div/div/span[1]"));
        String helloUser = "Hello, " + username;
        assertEquals(helloUser, hello.getText());
    }

    @Test
    public void _3userRegister_WithUsedEmail_ExpectedError() {
        //Use Case ?4: Register as a user. Alternate scenario
        registerUser(eMail);

        WebElement err = driver.findElement(By.cssSelector("html#ecwid_html body#ecwid_body.no-touch.no-media-queries.ecwid-customer-loggedOut.ecwid-lang-en.dragdrop-dropTarget.dragdrop-boundary.ecwid-starter-site.ecwid-loaded.ecwid-ready.ecwid-no-scroll div.ecwid-popup.ecwid-FormPopup.ecwid-register-popup.ecwid-responsive-popup.ecwid-no-touch.ecwid-supports-cssanimations.ecwid.ecwid-compact-popup div.popupContent div.ecwid-popup-touchLimiter table.ecwid-popup-container tbody tr td div.ecwid-popup-content table.ecwid-popup-contentPanel tbody tr td div.ecwid-form table tbody tr td table tbody tr td div.ecwid-FormPopup-fieldWrapper.ecwid-FormPopup-fieldWrapper-space table#gwt-uid-17.ecwid-fieldEnvelope.ecwid-fieldEnvelope-error tbody tr td div div.ecwid-fieldEnvelope-label"));
        assertEquals("Email already registered", err.getText());
    }

    @Test
    public void _4userLogin_ValidData_ShouldBeLogin() {
        //Use Case ?2: Login like Registration user
        goToLoginData();

        WebElement pass = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/div/input"));
        pass.clear();
        pass.sendKeys(password);

        WebElement sign = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[5]/td/table/tbody/tr/td/button"));
        sign.click();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        WebElement hello = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[1]/td/table/tbody/tr/td/div/div/span[1]"));
        String helloUser = "Hello, " + username;
        assertEquals(helloUser, hello.getText());
    }

    @Test
    public void _5userLogin_FalseData_ExpectedError() {
        //Use Case ?2: Login like Registration user. Alternate scenario expected error
        goToLoginData();
        passedWrongPassword();

        WebDriverWait pause = new WebDriverWait(driver, 10);
        pause.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/div")));

        WebElement err = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div"));
        assertEquals("Wrong email or password. Please try again.", err.getText());
    }

    @Test
    public void _6userLogin_FalseData_GoToPasswordResetPage() {
        //Use Case ?2: Login like Registration user. Alternate scenario user receives an email with instructions about his login credentials
        goToLoginData();
        passedWrongPassword();

        WebDriverWait pause = new WebDriverWait(driver, 10);
        pause.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/div")));

        WebElement forgottenPassword = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[3]/td/table/tbody/tr[1]/td/a"));
        forgottenPassword.click();

        assertEquals("https://naturalmilk.ecwid.com/#!/~/resetPassword", driver.getCurrentUrl());
    }

    private void passedWrongPassword() {
        WebElement pass = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/div/input"));
        pass.clear();
        pass.sendKeys("wrongPassword");

        WebElement sign = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[5]/td/table/tbody/tr/td/button"));
        sign.click();
    }

    private void goToLoginData() {
        driver.get("https://naturalmilk.ecwid.com/");
        driver.manage().window().maximize();
        WebElement signIn = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[1]/td/table/tbody/tr/td/div/div/a"));
        signIn.click();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        WebElement email = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[1]/td/div/table/tbody/tr[1]/td/div/input"));
        email.clear();
        email.sendKeys(eMail);
    }

    private void registerUser(String newEmail) {
        driver.get("https://naturalmilk.ecwid.com/");
        driver.manage().window().maximize();
        WebElement signIn = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[1]/td/table/tbody/tr/td/div/div/a"));
        signIn.click();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        WebElement newAccount = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[3]/td/table/tbody/tr[3]/td/a"));
        newAccount.click();

        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        WebElement name = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[1]/td/div/table/tbody/tr[1]/td/div/input"));
        name.clear();
        name.sendKeys(username);
        WebElement email = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/div/input"));
        email.clear();
        email.sendKeys(newEmail);
        WebElement pass = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[3]/td/div/table/tbody/tr[1]/td/div/input"));
        pass.clear();
        pass.sendKeys(password);
        WebElement reg = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[5]/td/table/tbody/tr/td[1]/button"));
        reg.click();
    }

    @After
    public void tearDown() {
        driver.close();
    }
}
