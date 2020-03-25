package com.bsc.currency.paymentracker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PaymentTrackerApplicationTest {

    public static final String ERROR_MESSAGE = "Error: incorrect input format. " +
            "It should be in currency code (3 upper letters) and currency amount divided by whitespace!!!";

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    final String okfile = "src/test/resources/testokfile.txt";
    final String badfile = "src/test/resources/badfile.txt";

    final String[] oneSecondPeriod = new String[] {"500"};

    @Before
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @After
    public void restoreSystemInputOutput() throws IOException {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFileWrongName() {
        PaymentTrackerApplication.main(new String[]{"! =", "100"});
    }

    //@Test
    public void testFileWrongFirstLineFile() throws InterruptedException {
        setUpOutput();
        waitAndExit();
        runPaymentTracker(new String[]{badfile, "500"});
        PaymentTrackerApplication.main(new String[]{badfile, "500"});
        assertEquals(ERROR_MESSAGE, getOutput().trim());
    }

    @Test
    public void testoutput1() {
        provideInput("ajajaj");
        PaymentTrackerApplication.main(new String[]{"500"});
        provideExit();
        assertTrue(getOutput().trim().startsWith(ERROR_MESSAGE));
    }

    //@Test
    public void testoutput2() throws InterruptedException {
        provideInput("USD 10\n");
        PaymentTrackerApplication.main(new String[]{"300"});
        waitAndExit();
    }

    @Test
    public void testoutput3() {
        provideInput("USD 1a0");
        PaymentTrackerApplication.main(new String[]{"500"});
        provideExit();
        assertTrue(getOutput().trim().startsWith(ERROR_MESSAGE));
    }

    //@Test
    public void testoutput4() throws InterruptedException {
        provideInput("USD 10 5");
        PaymentTrackerApplication.main(new String[]{"300"});
        provideExit();
        assertTrue(getOutput().trim().startsWith(ERROR_MESSAGE));
    }

    //@Test
    public void testoutputOK() throws InterruptedException, IOException {
        restoreSystemInputOutput();
        setUpOutput();
        provideInput("USD 10");
        PaymentTrackerApplication.main(new String[]{"300"});
        //runPaymentTracker(new String[]{"300"});
        waitAndExit();
        assertEquals("USD 10", getOutput().trim());
    }

    private void provideInput(String data) {
        ByteArrayInputStream stream = null;
        try {
            stream = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        testIn = stream;
        System.setIn(testIn);
    }

    private void runPaymentTracker(String[] args) throws InterruptedException {
        Thread t = new Thread(() -> PaymentTrackerApplication.main(args));
        t.start();
        t.join();
    }

    private void provideExit() {
        provideInput("exit");
    }

    private void waitAndExit() {
        try {
            TimeUnit.MILLISECONDS.sleep(1180);
            provideExit();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getOutput() {
        final String out;
        try {
            out = testOut.toString(StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        systemOut.println(out);
        return testOut.toString();
    }

}
