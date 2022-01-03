package dev.rvz.consummers;

public interface ServiceFactory<T> {
    ConsumerService<T> create() throws Exception;
}
