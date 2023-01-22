import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/*
Testirati na stranici https://vue-demo.daniel-avellaneda.com login stranicu.

Test 1: Verifikovati da se u url-u stranice javlja ruta "/login". Verifikovati da atribut type u polju za unos email ima vrednost "email" i za password da ima atribut type "password.

Test 2: Koristeci Faker uneti nasumicno generisan email i password i verifikovati da se pojavljuje poruka "User does not exist".

Test 3: Verifikovati da kad se unese admin@admin.com (sto je dobar email) i pogresan password (generisan faker-om), da se pojavljuje poruka "Wrong password"

Koristiti TestNG i dodajte before i after class metode.

Domaci se kači na github.
 */

public class Testing {

    private WebDriver driver;
    private Faker faker;

    @BeforeClass
    public void beforeClass() {
        System.setProperty("webdriver.chrome.driver.", "C:\\chromedriver\\chromedriver.exe");

        //driver.manage().window().maximize(); - iz nekog razloga testovi bivaju ignorisani ako se maksimizira

        driver = new ChromeDriver();
        faker = new Faker();
    }

    @BeforeMethod
    public void beforeMethod() {
        driver.get("https://vue-demo.daniel-avellaneda.com/login");
    }

    @Test
    public void test1() {

        String expectedResult = "https://vue-demo.daniel-avellaneda.com/login";
        String urlElement = driver.getCurrentUrl();
        Assert.assertEquals(urlElement, expectedResult);

        String expectedEmailType = "email";
        WebElement email = driver.findElement(By.id("email"));
        String actualEmailType = email.getAttribute("type");
        Assert.assertEquals(actualEmailType, expectedEmailType);

        String expectedPasswordType = "password";
        WebElement password = driver.findElement(By.id("password"));
        String actualPasswordType = password.getAttribute("type");
        Assert.assertEquals(actualPasswordType, expectedPasswordType);
    }

    @Test
    public void test2() {

        WebElement email = driver.findElement(By.id("email"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/main/div/div[2]/div/div/div[3]/span/form/div/div[3]/button"));

        email.sendKeys(faker.internet().emailAddress());
        password.sendKeys(faker.internet().password());
        loginButton.click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String expectedMessage = "User does not exists";
        WebElement userNoExistMessage = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/main/div/div[2]/div/div/div[4]/div/div/div/div/div[1]/ul/li"));
        String actualMessage = userNoExistMessage.getText();
        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @Test
    public void test3() {

        WebElement email = driver.findElement(By.id("email"));
        WebElement password = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/main/div/div[2]/div/div/div[3]/span/form/div/div[3]/button"));

        email.sendKeys("admin@admin.com");
        password.sendKeys(faker.internet().password());
        loginButton.click();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String expectedMessage = "Wrong password";
        WebElement wrongPasswordMessage = driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/main/div/div[2]/div/div/div[4]/div/div/div/div/div[1]/ul/li"));
        String actualMessage = wrongPasswordMessage.getText();
        Assert.assertEquals(actualMessage, expectedMessage);
    }

    @AfterClass
    public void AfterClass() {
        driver.quit();
    }
}

