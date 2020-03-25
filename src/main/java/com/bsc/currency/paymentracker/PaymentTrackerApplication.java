package com.bsc.currency.paymentracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
* Application homework Demo which manages Payments and track them
*/
public class PaymentTrackerApplication {

    private static final String ERROR_MESSAGE = "Error: incorrect input format. " +
            "It should be in currency code (3 upper letters) and currency amount divided by whitespace!!! InputLine: ";

    private static final String CURRENCY_PATTERN = "[A-Z]{3}";
    private static final String EXIT_PATTERN = "exit";

    private static final long DEFAULT_PERIOD = new Long(60000); //60 seconds


    public static void main(String[] args) {

        final ConcurrentMap<String, BigDecimal> currencyMap = new ConcurrentHashMap<>(200);

        final Timer timer = new Timer();
        Long period = DEFAULT_PERIOD;
        String fileName = null;

        if(args.length == 1) {
            if (isLong(args[0])) {
                period = Long.parseLong(args[0]);
            } else {
                fileName = args[0];
            }
        }
        if (args.length == 2) {
            fileName = args[0];
            if (isLong(args[1])) {
                period = Long.parseLong(args[1]);
            }
        }

        final TimerTask printTask = preparePrintTask(currencyMap);
        timer.schedule(printTask, 0, period);

        if (fileName != null) {
            try (Scanner sc = prepareFileScanner(fileName)) {
                doReadScannerToMap(sc, currencyMap);
            }
        }

        try (Scanner sc = new Scanner(System.in)) {
            doReadScannerToMap(sc, currencyMap);
        }

        printTask.cancel();
        timer.cancel();
    }

    /**
     * Reads scanner to concurentMap
     * 
     * @param sc          scanner to read from
     * @param currencyMap ConcurrentMap to write to
     */
    private static void doReadScannerToMap(final Scanner sc, final ConcurrentMap<String, BigDecimal> currencyMap) {
        while (sc.hasNextLine()) {
            if (sc.hasNext(EXIT_PATTERN)) {
                break;
            }
            final String currencyCode = sc.hasNext(CURRENCY_PATTERN) ? sc.next(CURRENCY_PATTERN) : null;
            final BigDecimal currencyValue = sc.hasNextBigDecimal() ? sc.nextBigDecimal() : null;
            if (currencyCode != null && currencyValue != null) {
                currencyMap.merge(currencyCode, currencyValue, BigDecimal::add);
            } else {
                System.out.println(ERROR_MESSAGE + (sc.hasNext() ? sc.next() : ""));
            }
        }
    }

    /**
     * prepare task for scheduling regularly print results from the given concurent hash map
     *
     * @param currencyMap ConcurrentMap to write to
     */
    private static TimerTask preparePrintTask(final ConcurrentMap<String, BigDecimal> currencyMap) {
        Objects.requireNonNull(currencyMap);
        final TimerTask printTask = new TimerTask() {
            @Override
            public void run() {
                currencyMap.forEach((key, value) -> {
                    if (!BigDecimal.ZERO.equals(value)) {
                        System.out.println(key + " " + value);
                    }
                });
            }
        };
        return printTask;
    }

    /**
     * Creates Scanner over the given file, throws RuntimeException if File isnt correct or not exist
     *
     * @param filePath path to file
     * @return Scanner created from file
     */
    private static Scanner prepareFileScanner(final String filePath) {
        try {
            return new Scanner(new File(filePath), StandardCharsets.UTF_8.toString());
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("first command line argument has to be valid path to valid and openable file", e);
        }
    }

    private static boolean isLong(final String arg) {
        try {
            Long.parseLong(arg);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}

