/**
 * 
 */
package org.obfuscate4e.tests.testcase;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.obfuscate4e.GeneralObfuscate4eException;
import org.obfuscate4e.ObfuscationExclusion;

/**
 * @author fwo
 * 
 */
public class ObfuscationExclusionTest extends TestCase {

	private static final String testPath = "test.path.path.path";
	private static final String testDescription = "hello test";

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}
	
	/**
	 * Test method for
	 * {@link org.obfuscate4e.ObfuscationExclusion#createSimpleClassExclusion(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCreateSimpleClassExclusion() {

		ObfuscationExclusion myTestExclusion = null;
		try {
			myTestExclusion = ObfuscationExclusion.createSimpleClassExclusion(
					testPath, testDescription);
		} catch (GeneralObfuscate4eException e) {
			fail(e.getMessage());
		}

		assertNotNull(myTestExclusion);
		assertEquals(ObfuscationExclusion.CLASS, myTestExclusion.getType());
		assertEquals(testPath, myTestExclusion.getPath());
		assertEquals(testDescription, myTestExclusion.getDescription());
		assertFalse(myTestExclusion.isFlagRelating());
		assertEquals(ObfuscationExclusion.NO_RELATION, myTestExclusion
				.getRelation());

		assertFalse(myTestExclusion.isGenerated());

		myTestExclusion.setGenerated(true);
		assertTrue(myTestExclusion.isGenerated());
	}

	@Test
	public void testCreateSimpleClassExclusionWithNullPath() {

		try {
			ObfuscationExclusion.createSimpleClassExclusion(null,
					testDescription);
			fail("path==null did not throw exception");
		} catch (Exception e) {
			// everything is O.K.
		}
	}

	/**
	 * Test method for
	 * {@link org.obfuscate4e.ObfuscationExclusion#createSimpleClassExclusion(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCreateSimpleClassExclusionWithNullDescription() {

		ObfuscationExclusion myTestExclusion = null;
		try {
			myTestExclusion = ObfuscationExclusion.createSimpleClassExclusion(
					testPath, null);
		} catch (GeneralObfuscate4eException e) {
			fail(e.getMessage());
		}

		assertNotNull(myTestExclusion);
		assertEquals(ObfuscationExclusion.CLASS, myTestExclusion.getType());
		assertEquals(testPath, myTestExclusion.getPath());
		assertEquals("", myTestExclusion.getDescription());
		assertFalse(myTestExclusion.isFlagRelating());
		assertEquals(ObfuscationExclusion.NO_RELATION, myTestExclusion
				.getRelation());

		assertFalse(myTestExclusion.isGenerated());

		myTestExclusion.setGenerated(true);
		assertTrue(myTestExclusion.isGenerated());
	}

	/**
	 * Test method for
	 * {@link org.obfuscate4e.ObfuscationExclusion#createClassInheritanceExclusion(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCreateClassInheritanceExclusion() {
		ObfuscationExclusion myTestExclusion = null;
		try {
			myTestExclusion = ObfuscationExclusion
					.createClassInheritanceExclusion(testPath, testDescription);
		} catch (GeneralObfuscate4eException e) {
			fail(e.getMessage());
		}

		assertNotNull(myTestExclusion);
		assertEquals(ObfuscationExclusion.CLASS, myTestExclusion.getType());
		assertEquals(testPath, myTestExclusion.getPath());
		assertEquals(testDescription, myTestExclusion.getDescription());

		assertTrue(myTestExclusion.isFlagRelating());
		assertEquals(ObfuscationExclusion.EXTENDS, myTestExclusion
				.getRelation());

		assertFalse(myTestExclusion.isGenerated());

		myTestExclusion.setGenerated(true);
		assertTrue(myTestExclusion.isGenerated());

	}
	
	/**
	 * Test method for
	 * {@link org.obfuscate4e.ObfuscationExclusion#createClassInheritanceExclusion(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCreateClassInheritanceExclusionWithNullDescription() {
		ObfuscationExclusion myTestExclusion = null;
		try {
			myTestExclusion = ObfuscationExclusion
					.createClassInheritanceExclusion(testPath, null);
		} catch (GeneralObfuscate4eException e) {
			fail(e.getMessage());
		}

		assertNotNull(myTestExclusion);
		assertEquals(ObfuscationExclusion.CLASS, myTestExclusion.getType());
		assertEquals(testPath, myTestExclusion.getPath());
		assertEquals("", myTestExclusion.getDescription());

		assertTrue(myTestExclusion.isFlagRelating());
		assertEquals(ObfuscationExclusion.EXTENDS, myTestExclusion
				.getRelation());

		assertFalse(myTestExclusion.isGenerated());

		myTestExclusion.setGenerated(true);
		assertTrue(myTestExclusion.isGenerated());

	}
	
	@Test
	public void testCreateClassInheritanceExclusionWithNullPath() {

		try {
			ObfuscationExclusion.createClassInheritanceExclusion(null,
					testDescription);
			fail("path==null did not throw exception");
		} catch (Exception e) {
			// everything is O.K.
		}
	}


	/**
	 * Test method for
	 * {@link org.obfuscate4e.ObfuscationExclusion#createInterfaceImplementationExclusion(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCreateInterfaceImplementationExclusion() {
		ObfuscationExclusion myTestExclusion = null;
		try {
			myTestExclusion = ObfuscationExclusion
					.createInterfaceImplementationExclusion(testPath,
							testDescription);
		} catch (GeneralObfuscate4eException e) {
			fail(e.getMessage());
		}

		assertNotNull(myTestExclusion);
		assertEquals(ObfuscationExclusion.INTERFACE, myTestExclusion.getType());
		assertEquals(testPath, myTestExclusion.getPath());
		assertEquals(testDescription, myTestExclusion.getDescription());

		assertTrue(myTestExclusion.isFlagRelating());
		assertEquals(ObfuscationExclusion.IMPLEMENTS, myTestExclusion
				.getRelation());

		assertFalse(myTestExclusion.isGenerated());

		myTestExclusion.setGenerated(true);
		assertTrue(myTestExclusion.isGenerated());
	}
	
	/**
	 * Test method for
	 * {@link org.obfuscate4e.ObfuscationExclusion#createInterfaceImplementationExclusion(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCreateInterfaceImplementationExclusionWithNullDescription() {
		ObfuscationExclusion myTestExclusion = null;
		try {
			myTestExclusion = ObfuscationExclusion
					.createInterfaceImplementationExclusion(testPath,
							null);
		} catch (GeneralObfuscate4eException e) {
			fail(e.getMessage());
		}

		assertNotNull(myTestExclusion);
		assertEquals(ObfuscationExclusion.INTERFACE, myTestExclusion.getType());
		assertEquals(testPath, myTestExclusion.getPath());
		assertEquals("", myTestExclusion.getDescription());

		assertTrue(myTestExclusion.isFlagRelating());
		assertEquals(ObfuscationExclusion.IMPLEMENTS, myTestExclusion
				.getRelation());

		assertFalse(myTestExclusion.isGenerated());

		myTestExclusion.setGenerated(true);
		assertTrue(myTestExclusion.isGenerated());
	}
	
	@Test
	public void testCreateInterfaceImplementationExclusionWithNullPath() {

		try {
			ObfuscationExclusion.createInterfaceImplementationExclusion(null,
					testDescription);
			fail("path==null did not throw exception");
		} catch (Exception e) {
			// everything is O.K.
		}
	}


	/**
	 * Test method for
	 * {@link org.obfuscate4e.ObfuscationExclusion#createPackageExclusion(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCreatePackageExclusion() {
		ObfuscationExclusion myTestExclusion = null;
		try {
			myTestExclusion = ObfuscationExclusion
					.createPackageExclusion(testPath, testDescription);
		} catch (GeneralObfuscate4eException e) {
			fail(e.getMessage());
		}

		assertNotNull(myTestExclusion);
		assertEquals(ObfuscationExclusion.PACKAGE, myTestExclusion.getType());
		assertEquals(testPath, myTestExclusion.getPath());
		assertEquals(testDescription, myTestExclusion.getDescription());

		assertFalse(myTestExclusion.isFlagRelating());
		assertEquals(ObfuscationExclusion.NO_RELATION, myTestExclusion
				.getRelation());

		assertFalse(myTestExclusion.isGenerated());

		myTestExclusion.setGenerated(true);
		assertTrue(myTestExclusion.isGenerated());
	}
	
	/**
	 * Test method for
	 * {@link org.obfuscate4e.ObfuscationExclusion#createPackageExclusion(java.lang.String, java.lang.String)}
	 * .
	 */
	@Test
	public void testCreatePackageExclusionWithNullDescription() {
		ObfuscationExclusion myTestExclusion = null;
		try {
			myTestExclusion = ObfuscationExclusion
					.createPackageExclusion(testPath, null);
		} catch (GeneralObfuscate4eException e) {
			fail(e.getMessage());
		}

		assertNotNull(myTestExclusion);
		assertEquals(ObfuscationExclusion.PACKAGE, myTestExclusion.getType());
		assertEquals(testPath, myTestExclusion.getPath());
		assertEquals("", myTestExclusion.getDescription());

		assertFalse(myTestExclusion.isFlagRelating());
		assertEquals(ObfuscationExclusion.NO_RELATION, myTestExclusion
				.getRelation());

		assertFalse(myTestExclusion.isGenerated());

		myTestExclusion.setGenerated(true);
		assertTrue(myTestExclusion.isGenerated());
	}
	
	@Test
	public void testCreatePackageExclusionWithNullPath() {

		try {
			ObfuscationExclusion.createPackageExclusion(null,
					testDescription);
			fail("path==null did not throw exception");
		} catch (Exception e) {
			// everything is O.K.
		}
	}



	/**
	 * Test method for
	 * {@link org.obfuscate4e.ObfuscationExclusion#isGenerated()}.
	 */
	@Test
	public void testGenerated() {
		ObfuscationExclusion myTestExclusion = null;
		try {
			myTestExclusion = ObfuscationExclusion
					.createSimpleClassExclusion(testPath, testDescription);
		} catch (GeneralObfuscate4eException e) {
			fail(e.getMessage());
		}

		assertFalse(myTestExclusion.isGenerated());
		myTestExclusion.setGenerated(true);
		assertTrue(myTestExclusion.isGenerated());
		myTestExclusion.setGenerated(false);
		assertFalse(myTestExclusion.isGenerated());

	}

	/**
	 * Test method for {@link org.obfuscate4e.ObfuscationExclusion#getPath()}.
	 */
	@Test
	public void testGetPath() {
		// no operation; already tested in factory method test cases
	}

	/**
	 * Test method for
	 * {@link org.obfuscate4e.ObfuscationExclusion#getDescription()}.
	 */
	@Test
	public void testGetDescription() {
		// no operation; already tested in factory method test cases
	}

	/**
	 * Test method for {@link org.obfuscate4e.ObfuscationExclusion#getType()}.
	 */
	@Test
	public void testGetType() {
		// no operation; already tested in factory method test cases
	}

	/**
	 * Test method for
	 * {@link org.obfuscate4e.ObfuscationExclusion#getRelation()}.
	 */
	@Test
	public void testGetRelation() {
		// no operation; already tested in factory method test cases
	}

	/**
	 * Test method for
	 * {@link org.obfuscate4e.ObfuscationExclusion#isFlagRelating()}.
	 */
	@Test
	public void testIsFlagRelating() {
		// no operation; already tested in factory method test cases
	}

}
