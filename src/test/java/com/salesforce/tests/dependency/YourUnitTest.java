package com.salesforce.tests.dependency;

import org.junit.Test;

import java.io.IOException;

/**
 * Place holder for your unit tests
 */
public class YourUnitTest extends BaseTest {

    @Test
    public void testFakeDepend() throws IOException {
        String[] input = {
                "DEPEND TELNET\n"
        };

        String expectedOutput = "DEPEND TELNET\n" +
                "DEPEND should atleast have 2 modules\n";

        runTest(expectedOutput, input);
    }

    @Test
    public void testFakeInstall() throws IOException {
        String[] input = {
                "INSTALL\n"
        };

        String expectedOutput = "INSTALL\n" +
                "INSTALL should atleast have 1 module\n";

        runTest(expectedOutput, input);
    }

    @Test
    public void testFakeRemove() throws IOException {
        String[] input = {
                "REMOVE\n"
        };

        String expectedOutput = "REMOVE\n" +
                "REMOVE should atleast have 1 module\n";

        runTest(expectedOutput, input);
    }
}
