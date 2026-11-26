package utils;

import java.util.concurrent.atomic.AtomicInteger;

public class TestDataGenerator {

    private static final AtomicInteger COUNTER = new AtomicInteger((int) (System.currentTimeMillis() % 1000));

    public static String generateUniqueUsername() {
        return generateUniqueUsername("usr");
    }

    public static String generateUniqueUsername(String prefix) {
        String cleanPrefix = (prefix != null && !prefix.trim().isEmpty()) ? prefix.replaceAll("[^a-zA-Z]", "") : "usr";
        if (cleanPrefix.length() > 3) {
            cleanPrefix = cleanPrefix.substring(0, 3).toLowerCase();
        }
        long timePortion = (System.currentTimeMillis() / 1000) % 100000;
        int countPortion = Math.abs(COUNTER.incrementAndGet() % 1000);
        return String.format("%s%05d%03d", cleanPrefix, timePortion, countPortion);
    }

    public static String generateRandomSsn() {
        int part1 = 100 + (int) (Math.random() * 899);
        int part2 = 10 + (int) (Math.random() * 89);
        int part3 = 1000 + (int) (Math.random() * 8999);
        return part1 + "-" + part2 + "-" + part3;
    }

    public static String generateRandomPhone() {
        long number = 1000000000L + (long) (Math.random() * 8999999999L);
        return String.valueOf(number);
    }

    public static String generateRandomZip() {
        int zip = 10000 + (int) (Math.random() * 89999);
        return String.valueOf(zip);
    }
}
