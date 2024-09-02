package com.manyvids.parser.util;

import java.util.concurrent.Callable;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

public class WaitConditionUtil {

    public static final Integer DEFAULT_WAIT_TIME_IN_SEC = 30;

    public static void waitUntil(final Callable<Boolean> callable) {
        waitUntil(callable,
                  DEFAULT_WAIT_TIME_IN_SEC);
    }

    public static void waitUntil(final Callable<Boolean> callable,
                                    final int atMost) {
        await().atMost(atMost, SECONDS)
            .pollInterval(1, SECONDS)
            .until(callable);
    }
}
