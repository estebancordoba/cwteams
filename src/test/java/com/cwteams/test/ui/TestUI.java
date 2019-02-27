package com.cwteams.test.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestUI {
	
	private String ip, port, url;
	private WebDriver driver;
	
	@Before
	public void setUp(){
		
		String currentRelativePath = Paths.get(".").toAbsolutePath().normalize().toString();
		String path = currentRelativePath+ File.separatorChar + "src" +File.separatorChar + "test"+ File.separatorChar + "resources"+ File.separatorChar +
				"settings" + File.separatorChar + "Settings-test.properties";
						
		Properties propiedades = new Properties();
		try {
			propiedades.load(new FileInputStream(path));
		} 
		catch (FileNotFoundException e) { } 
		catch (IOException e) { }
		
		ip=propiedades.getProperty("ip");
		port=propiedades.getProperty("port");
		url="http://"+ip+":"+port+"/cwteams";
		
		driver = new FirefoxDriver();
		
		// System.setProperty("webdriver.gecko.driver","GECKO_DRIVER_PATH");
	}
	
	@After
	public void tearDown() {
		driver.close();
    }

	@Test
	public void show_login() {		  
		driver.get(url);  
		
		WebElement login = driver.findElement(By.id("pgLogin:j_idt47"));
				
		assertFalse((login.isDisplayed()));
		driver.findElement(By.id("topbar_login")).click();
		assertTrue((login.isDisplayed()));				
		driver.findElement(By.id("pgLogin:j_idt57")).click();
		assertFalse((login.isDisplayed()));
	}	
	
}