package com.cwteams.test.ui;


import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
   TestUI.class,
})

public class AllUITests {    
	public static void main(String[] args) {
      Result result = JUnitCore.runClasses(AllUITests.class);

      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
			
      System.out.println(result.wasSuccessful());
   }
}