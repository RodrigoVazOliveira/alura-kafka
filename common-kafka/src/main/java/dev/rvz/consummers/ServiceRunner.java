package dev.rvz.consummers;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServiceRunner<T> {

    private final ConsumerProvider<T> consumerProvider;

    public ServiceRunner(ServiceFactory<T> serviceFactory) {
        this.consumerProvider = new ConsumerProvider<>(serviceFactory);
    }

    public void start(int threadCount) {
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i <= threadCount; i++) {
            executorService.submit(consumerProvider);
        }
    }
}
