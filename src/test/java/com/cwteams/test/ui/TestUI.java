package com.cwteams.test.ui;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;
import org.junit.Before;
import org.junit.Test;

public class TestUI {
	
	String ip, port, url;	
	
	@Before
	public void setUp(){		
		// String path = "settings" + File.separatorChar + "Settings-test.properties";
		
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
		url="http://"+ip+":"+port;
	}

	@Test
	public void site_header_is_on_home_page() {
		WebDriver browser;   

		// System.setProperty("webdriver.gecko.driver","GECKO_DRIVER_PATH");   

		browser = new FirefoxDriver();    
		browser.get(url);   

		WebElement header = browser.findElement(By.id("site-header"));   

		assertTrue((header.isDisplayed()));
		
		browser.close();
	}
}