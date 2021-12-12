package com.mygdx.game.chess.test;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public final class Runner {

    public static void main(final String[] args) {
        final Result result = JUnitCore.runClasses(TestSuite.class);
        System.out.println("Ran " + result.getRunCount() + " unit tests.");
        if (result.wasSuccessful()) {
            System.out.println("All tests passed.");
        } else {
            System.out.println(result.getFailureCount() + " test(s) failed.\nBelow are the test(s) that failed");
            result.getFailures().forEach(failure -> System.out.println(failure.getTrace()));
        }
    }
}