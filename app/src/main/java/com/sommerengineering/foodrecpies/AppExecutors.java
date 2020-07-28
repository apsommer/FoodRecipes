package com.sommerengineering.foodrecpies;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecutors {

    // singleton
    private static AppExecutors instance;
    public static AppExecutors getInstance() {
        if (instance == null) {
            instance = new AppExecutors();
        }
        return instance;
    }

    // executors create a single thread or pool of threads (main or background)
    // scheduled executors allow for a network timeout
    private final ScheduledExecutorService networkIO = Executors.newScheduledThreadPool(3);
    public ScheduledExecutorService networkIO() {
        return networkIO;
    }
}
