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
        //Use Case 1: Login like Guest user (without registration)
        driver.get("https://naturalmilk.ecwid.com/");
        driver.manage().window().maximize();

        WebElement signIn = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[1]/td/table/tbody/tr/td/div/div/a"));
        assertEquals("Sign In", signIn.getText());
    }

    @Test
    public void _2userRegister_WithFreeEmail_ShouldBeRegister() {
        //Use Case 4: Register as a user
        registerUser(eMail);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement hello = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[1]/td/table/tbody/tr/td/div/div/span[1]"));
        String helloUser = "Hello, " + username;
        assertEquals(helloUser, hello.getText());
    }

    @Test
    public void _3userRegister_WithUsedEmail_ExpectedError() {
        //Use Case 4: Register as a user. Alternate scenario
        registerUser(eMail);

        WebDriverWait pause = new WebDriverWait(driver, 10);
        pause.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/div")));

        WebElement err = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/div"));
        assertEquals("Email already registered", err.getText());
    }

    @Test
    public void _4userLogin_ValidData_ShouldBeLogin() {
        //Use Case 2: Login like Registration user
        goToLoginData();
        passedTruePassword();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement hello = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[1]/td/table/tbody/tr/td/div/div/span[1]"));
        String helloUser = "Hello, " + username;
        assertEquals(helloUser, hello.getText());
    }

    @Test
    public void _5userLogin_FalseData_ExpectedError() {
        //Use Case 2: Login like Registration user. Alternate scenario expected error
        goToLoginData();
        passedWrongPassword();

        WebDriverWait pause = new WebDriverWait(driver, 10);
        pause.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/div")));

        WebElement err = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div"));
        assertEquals("Wrong email or password. Please try again.", err.getText());
    }

    @Test
    public void _6userLogin_FalseData_GoToPasswordResetPage() {
        //Use Case 2: Login like Registration user. Alternate scenario user receives an email with instructions about his login credentials
        goToLoginData();
        passedWrongPassword();

        WebDriverWait pause = new WebDriverWait(driver, 10);
        pause.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/div")));

        WebElement forgottenPassword = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[3]/td/table/tbody/tr[1]/td/a"));
        forgottenPassword.click();

        assertEquals("https://naturalmilk.ecwid.com/#!/~/resetPassword", driver.getCurrentUrl());
    }

    @Test
    public void _7loginUser_ChangePassword_ExpectedSucces() {
        //Use Case 3: Change password
        goToLoginData();
        passedTruePassword();

        driver.get("https://naturalmilk.ecwid.com/#!/~/accountSettings");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement oldPass = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[1]/table/tbody/tr[1]/td/div/input"));
        oldPass.clear();
        oldPass.sendKeys(password);

        WebElement newPass = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[2]/table/tbody/tr[1]/td/div/input"));
        newPass.clear();
        newPass.sendKeys("newpass");

        WebElement retypePass = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[3]/table/tbody/tr[1]/td/div/input"));
        retypePass.clear();
        retypePass.sendKeys("newpass");

        WebElement save = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/button"));
        save.click();

        WebDriverWait pause = new WebDriverWait(driver, 20);
        pause.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div.gwt-HTML.ecwid-DisappearingMessage-content")));
        WebElement succes = driver.findElement(By.cssSelector("div.gwt-HTML.ecwid-DisappearingMessage-content"));
        assertEquals("Password has been changed\nsuccessfully",succes.getText());
    }

    @Test
    public void _8loginUserChangepassword_InkorektPassword_ExpectedError(){
        //Use Case 3: Change password. Alternate scenario invalid password
        goToLoginData();
        passedTruePassword();

        driver.get("https://naturalmilk.ecwid.com/#!/~/accountSettings");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement oldPass = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[1]/table/tbody/tr[1]/td/div/input"));
        oldPass.clear();
        oldPass.sendKeys(password);

        WebElement newPass = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[2]/table/tbody/tr[1]/td/div/input"));
        newPass.clear();
        newPass.sendKeys("123");

        WebElement retypePass = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[3]/table/tbody/tr[1]/td/div/input"));
        retypePass.clear();
        retypePass.sendKeys("123");

        WebElement save = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/button"));
        save.click();

        WebDriverWait pause = new WebDriverWait(driver, 10);
        pause.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[2]/table/tbody/tr[2]/td/div/div")));
        WebElement error = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[2]/table/tbody/tr[2]/td/div/div"));
        assertEquals("Password must be at least 5 characters long", error.getText());
    }

    @Test
    public void _9loginUserChangepassword_InkorektPassword_ExpectedError(){
        //Use Case 3: Change password. Alternate scenario new password and confirm new password do not match
        goToLoginData();
        passedTruePassword();

        driver.get("https://naturalmilk.ecwid.com/#!/~/accountSettings");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement oldPass = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[1]/table/tbody/tr[1]/td/div/input"));
        oldPass.clear();
        oldPass.sendKeys(password);

        WebElement newPass = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[2]/table/tbody/tr[1]/td/div/input"));
        newPass.clear();
        newPass.sendKeys(password);

        WebElement retypePass = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[3]/table/tbody/tr[1]/td/div/input"));
        retypePass.clear();
        retypePass.sendKeys("newpass");

        WebElement save = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/button"));
        save.click();

        WebDriverWait pause = new WebDriverWait(driver, 10);
        pause.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[3]/table/tbody/tr[2]/td/div/div")));
        WebElement error = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[3]/table/tbody/tr[2]/td/div/div"));
        assertEquals("Passwords do not match", error.getText());
    }

    @Test
    public void _101loginUserChangepassword_WrongOldPassword_ExpectedError(){
        //Use Case 3: Change password. Alternate wrong old password
        goToLoginData();
        passedTruePassword();

        driver.get("https://naturalmilk.ecwid.com/#!/~/accountSettings");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        WebElement oldPass = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[1]/table/tbody/tr[1]/td/div/input"));
        oldPass.clear();
        oldPass.sendKeys("newpass");

        WebElement newPass = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[2]/table/tbody/tr[1]/td/div/input"));
        newPass.clear();
        newPass.sendKeys(password);

        WebElement retypePass = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[3]/table/tbody/tr[1]/td/div/input"));
        retypePass.clear();
        retypePass.sendKeys(password);

        WebElement save = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/button"));
        save.click();

        WebDriverWait pause = new WebDriverWait(driver, 10);
        pause.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[1]/table/tbody/tr[2]/td/div/div")));
        WebElement error = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[2]/div/table/tbody/tr[2]/td/div/div/div/div[1]/table/tbody/tr[2]/td/div/div"));
        assertEquals("Password is incorrect", error.getText());
    }

    @Test
    public void _11user_ChooseProduct_SystemDisplaysProduct(){
        //Use Case 5 related to navigate in site
        driver.get("https://naturalmilk.ecwid.com/");
        driver.manage().window().maximize();
        
        WebElement image = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/table/tbody/tr[2]/td[5]/div/span"));
        image.click();

        WebDriverWait pause = new WebDriverWait(driver, 10);
        pause.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[5]/div/table/tbody/tr[1]/td/div/table/tr[3]/td[3]/div/div/div[1]/a")));


        WebElement product = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[2]/td/div/div/div[5]/div/table/tbody/tr[1]/td/div/table/tr[3]/td[3]/div/div/div[1]/a"));
        product.click();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

        assertEquals("https://naturalmilk.ecwid.com/#!/%D0%A1%D0%A3%D0%A5-%D0%A1%D0%A3%D0%94%D0%96%D0%A3%D0%9A-%D0%9B%D0%A3%D0%9A%D0%90%D0%9D%D0%9A%D0%90-%D0%BF%D0%BE%D0%B4%D0%BA%D0%BE%D0%B2%D0%B0-%D0%BE%D0%BA%D0%BE%D0%BB%D0%BE-0-350%D0%BA%D0%B3/p/63576065/category=18483007", driver.getCurrentUrl());
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    private void passedTruePassword() {
        WebElement pass = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/div/input"));
        pass.clear();
        pass.sendKeys(password);

        WebElement sign = driver.findElement(By.xpath("/html/body/div[14]/div/div/table/tbody/tr[2]/td/div/table/tbody/tr[5]/td/table/tbody/tr/td/button"));
        sign.click();
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
