package com.manyvids.parser.util;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomUtil {

    private static final Random RANDOM = new Random();

    public static int DELAY_MIN;
    public static int DELAY_MAN;
    public static int SUBSCRIPTIONS_PER_DAY_MIN;
    public static int SUBSCRIPTIONS_PER_DAY_MAX;

    public RandomUtil(@Value("${app.settings.delay.min}") final int delayMin,
                      @Value("${app.settings.delay.max}") final int delayMax,
                      @Value("${app.settings.subscriptions-per-day.min}") final int subscriptionsPerDayMin,
                      @Value("${app.settings.subscriptions-per-day.max}") final int subscriptionsPerDayMax) {
        DELAY_MIN = delayMin;
        DELAY_MAN = delayMax;
        SUBSCRIPTIONS_PER_DAY_MIN = subscriptionsPerDayMin;
        SUBSCRIPTIONS_PER_DAY_MAX = subscriptionsPerDayMax;
    }

    @SneakyThrows
    public static void getRandomDelay() {
        Thread.sleep(getRandom(DELAY_MIN, DELAY_MAN) * 1000);
    }

    private static int getRandom(final int from,
                                 final int to) {
        return RANDOM.nextInt(from, to);
    }
}
