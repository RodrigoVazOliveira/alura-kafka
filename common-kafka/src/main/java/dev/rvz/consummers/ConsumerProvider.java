package dev.rvz.consummers;

import dev.rvz.services.KafkaService;

import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

public class ConsumerProvider<T> implements Callable<Void> {

    private final ServiceFactory<T> serviceFactory;

    public ConsumerProvider(ServiceFactory<T> serviceFactory) {
        this.serviceFactory = serviceFactory;
    }

    public Void call() throws ExecutionException, InterruptedException {
        ConsumerService<T> myService = serviceFactory.create();
        KafkaService kafkaService = new KafkaService<>(myService.getGroupId(), myService.getTopic(), myService::parse, new HashMap<>());
        kafkaService.run();;

        return null;
    }
}
