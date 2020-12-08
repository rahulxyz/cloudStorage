package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private static WebDriver driver;
	private static WebDriverWait wait;

	private SignupPage signupPage;
	private LoginPage loginPage;
	private HomePage homePage;

	String baseUrl;

	String signupPath = "/signup";
	String loginPath = "/login";
	String homePath = "/home";
	String logoutPath = loginPath+"?logout";

	String fname = "Rahul";
	String lname = "Gupta";
	String uname = "ra1";
	String pword = "1234";

	/*Notes*/
	String title = "My Note 1";
	String description = "Description 2";
	String updateDescription = ">>>>Updated Description 1";

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 20);
	}

	@BeforeEach
	public void beforeEach(){
		baseUrl = "http://localhost:" + this.port;
		signupPage = new SignupPage(driver);
		loginPage = new LoginPage(driver);
		homePage = new HomePage(driver);
	}

	@AfterAll
	static void afterAll() {
		driver.quit();
	}

	@Test
	@Order(1)
	public void testUnAuthAccess() {
		testHomePageNoAccessWithoutLogin();
	}

	@Test
	@Order(2)
	public void testValidUserSignupAndLogin() throws InterruptedException {
		testSignup();
		testLogin();
		testLogout();
		testHomePageNoAccessWithoutLogin();
	}

	@Test
	@Order(3)
	public void testSaveNotes() throws InterruptedException {
		testLogin();
		wait.until(ExpectedConditions.visibilityOf(homePage.getNotesTab()));
		homePage.gotoNotesTab();
		wait.until(ExpectedConditions.visibilityOf(homePage.getAddNoteButton()));
		homePage.saveNote(title,description);
		homePage.goBackToNotes();
		List<WebElement> rows = homePage.getNoteRow();
		WebElement rowAdded = rows.get(rows.size()-1).findElement(By.id("row-note-description"));
		Assertions.assertEquals(description, rowAdded.getText());
		testLogout();
	}

	@Test
	@Order(4)
	public void testEditNotes() throws InterruptedException {
		testLogin();
		wait.until(ExpectedConditions.visibilityOf(homePage.getNotesTab()));
		homePage.gotoNotesTab();
		wait.until(ExpectedConditions.visibilityOf(homePage.getAddNoteButton()));
		List<WebElement> rows = homePage.getNoteRow();
		WebElement editButton = rows.get(rows.size()-1).findElement(By.id("edit-note"));
		homePage.clickEditNote(editButton);
		homePage.editNote(updateDescription);
		homePage.goBackToNotes();
		WebElement rowAdded = rows.get(rows.size()-1).findElement(By.id("row-note-description"));
		Assertions.assertEquals(updateDescription, rowAdded.getText());
		testLogout();
	}

	@Test
	@Order(5)
	public void testDeleteNotes() throws InterruptedException {
		testLogin();
		wait.until(ExpectedConditions.visibilityOf(homePage.getNotesTab()));
		homePage.gotoNotesTab();
		wait.until(ExpectedConditions.visibilityOf(homePage.getAddNoteButton()));
		List<WebElement> rows = homePage.getNoteRow();
		WebElement deleteButton = rows.get(rows.size()-1).findElement(By.id("delete-note-button"));
		String removedDescription = rows.get(rows.size()-1).findElement(By.id("row-note-description")).getText();
		System.out.println(">>>>>>>>>>>> "+ removedDescription);
		homePage.clickDeleteNote(deleteButton);

		homePage.goBackToNotes();
		WebElement rowAdded = rows.get(rows.size()-1).findElement(By.id("row-note-description"));
		Assertions.assertNotEquals(removedDescription, rowAdded.getText());

		testLogout();
	}

	public void testSignup() {
		driver.get(baseUrl + signupPath);
		Assertions.assertEquals("Sign Up", driver.getTitle());
		signupPage.signUp(fname, lname, uname, pword);
		wait.until(ExpectedConditions.urlToBe(baseUrl + loginPath));
	}

	public void testHomePageNoAccessWithoutLogin(){
		driver.get(baseUrl + homePath);
		wait.until(ExpectedConditions.urlToBe(baseUrl + loginPath));
		Assertions.assertEquals("Login", driver.getTitle());
	}

	public void testLogin(){
		driver.get(baseUrl + loginPath);
		Assertions.assertEquals("Login", driver.getTitle());
		loginPage.login(uname, pword);
		wait.until(ExpectedConditions.urlToBe(baseUrl + homePath));
		Assertions.assertEquals("Home", driver.getTitle());
	}

	public void testLogout() {
		driver.get(baseUrl + homePath);
		wait.until(ExpectedConditions.visibilityOf(homePage.getLogoutButton()));
		homePage.logout();
		wait.until(ExpectedConditions.urlToBe(baseUrl + logoutPath));
		Assertions.assertEquals("Login", driver.getTitle());
	}

}
