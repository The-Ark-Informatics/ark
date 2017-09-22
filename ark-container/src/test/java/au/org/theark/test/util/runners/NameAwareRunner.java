package au.org.theark.test.util.runners;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import au.org.theark.test.util.Reference;

public class NameAwareRunner extends SpringJUnit4ClassRunner {


    /**
     * Construct a new {@code SpringJUnit4ClassRunner} and initialize a
     * {@link TestContextManager} to provide Spring testing functionality to
     * standard JUnit tests.
     *
     * @param clazz the test class to be run
     * @see #createTestContextManager(Class)
     */
    public NameAwareRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }

    @Override
    protected Statement methodBlock(FrameworkMethod method) {
        Reference.currentTestName = method.getDeclaringClass().getName() + "#" + method.getName();
        return super.methodBlock(method);
    }
}
