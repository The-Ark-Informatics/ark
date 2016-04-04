package au.org.theark;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import au.org.theark.core.testcategories.IntegrationTests;
import au.org.theark.core.testcategories.UnitTests;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/test/resources/applicationContext.xml"})
public class TestExamples {

	@Test
	@Category(UnitTests.class)
	public void testUnit() {
		System.out.println("This is a unit test");
	}
	
	@Test
	@Category(IntegrationTests.class)
	public void testIntegration() {
		System.out.println("This is an integration test");
	}
	
}
