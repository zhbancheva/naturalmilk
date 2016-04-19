import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertEquals;

public class SystemAuthentication {

    private WebDriver driver;

    @Before
    public  void  setUp(){
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @Test
    public  void guestLogin(){
        //1.Login like Guest user (without registration)
        driver.get("https://naturalmilk.ecwid.com/");
        WebElement signIn = driver.findElement(By.xpath("/html/body/div[3]/div[1]/div/section/div/div/div/div/div/table/tbody/tr[1]/td/table/tbody/tr/td/div/div/a"));
        assertEquals("Sign In", signIn.getText());
    }

    @After
    public  void  tearDown(){

    }
}
