package org.obfuscate4e.tests;

import org.obfuscate4e.tests.testcase.ChecksumHelperTest;
import org.obfuscate4e.tests.testcase.ObfuscationExclusionTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests extends TestSuite {

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Eclipse Test for Obfuscate4e");
		
		suite.addTest(ProjectDependingTests.suite());
		suite.addTestSuite(ObfuscationExclusionTest.class);
		suite.addTestSuite(ChecksumHelperTest.class);
		
		return suite;
	}

}
