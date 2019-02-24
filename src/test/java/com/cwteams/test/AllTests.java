package com.cwteams.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;

import com.cwteams.test.general.AllGeneralTests;
import com.cwteams.test.ui.AllUITests;

@RunWith(Suite.class)

@Suite.SuiteClasses({
   AllUITests.class,
   AllGeneralTests.class,
})

public class AllTests {    
	public static void main(String[] args) {
      Result result = JUnitCore.runClasses(AllTests.class);

      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
			
      System.out.println(result.wasSuccessful());
   }
}