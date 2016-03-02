/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark;

import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.util.tester.WicketTester;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import au.org.theark.web.pages.login.LoginPage;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/applicationContext.xml"})
public class TestLoginPage extends TestCase {
		
	private transient static Logger log = LoggerFactory.getLogger(TestLoginPage.class);

	private WicketTester tester;
	
	private static FirefoxDriver driver;
	private WebElement element;

	@BeforeClass
	public static void openBrowser(){
		driver = new FirefoxDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	} 

	@Before
	public void setup() {
		tester = new WicketTester();
		tester.startPage(LoginPage.class);
		driver.get("http://192.168.33.10:8080/ark");	
	}

	@Test
	public void testRenderLoginPage() {		
		tester.assertRenderedPage(LoginPage.class);
	}

	@Test
	public void testLoginFieldsRendered() {
		tester.assertComponent("loginForm:userName", TextField.class);
		tester.assertComponent("loginForm:password", PasswordTextField.class);
	}

	@Test
	public void testValidLogin() {
		log.info("Starting test " + new Object(){}.getClass().getEnclosingMethod().getName());
		driver.findElement(By.name("userName")).sendKeys("arksuperuser@ark.org.au");
		driver.findElement(By.name("password")).sendKeys("Password_1");
		driver.findElement(By.name("signInButton")).click();
		//If the Logout link appears, then we have successfully logged in.
		try {
			element = driver.findElement(WicketBy.wicketPath("ajaxLogoutLink"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertNotNull(element);
		log.info("Ending test " + new Object(){}.getClass().getEnclosingMethod().getName());
	}
	
	@Test
	public void testInvalidLogin() {
		log.info("Starting test " + new Object(){}.getClass().getEnclosingMethod().getName());
		driver.findElement(By.name("userName")).sendKeys("arksuperuser@ark.org.au");
		driver.findElement(By.name("password")).sendKeys("Incorrext Password");
		driver.findElement(By.name("signInButton")).click();
		
		//If the "Invalid username and password" warning comes up, then we didn't log in.
		element = driver.findElement(By.className("feedbackPanelERROR"));
		assertNotNull(element);
		assertEquals("Invalid username and/or password.", element.getText());
		log.info("Ending test " + new Object(){}.getClass().getEnclosingMethod().getName());
	}

	@AfterClass
	public static void closeBrowser(){
		driver.quit();
	}
}