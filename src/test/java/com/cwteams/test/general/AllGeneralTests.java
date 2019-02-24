package com.cwteams.test.general;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
   TestDAO.class,
   TestGA.class
})

public class AllGeneralTests {    
	public static void main(String[] args) {
      Result result = JUnitCore.runClasses(AllGeneralTests.class);

      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }
			
      System.out.println(result.wasSuccessful());
   }
}