package dev.rvz.services.helpers;

import dev.rvz.services.ConsumerFunction;
import dev.rvz.services.KafkaService;

import java.util.Map;
import java.util.regex.Pattern;

public class KafkaServiceBuilder<T> {
    private String groupId;
    private String topic;
    private ConsumerFunction<T> parse;
    private Map<String, String> properties;
    private Pattern patternTopic;

    public KafkaServiceBuilder setGroupId(String groupId) {
        this.groupId = groupId;
        return this;
    }

    public KafkaServiceBuilder setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public KafkaServiceBuilder setParse(ConsumerFunction<T> parse) {
        this.parse = parse;
        return this;
    }

    public KafkaServiceBuilder setProperties(Map<String, String> properties) {
        this.properties = properties;
        return this;
    }

    public KafkaServiceBuilder setPatternTopic(Pattern patternTopic) {
        this.patternTopic = patternTopic;
        return this;
    }

    public KafkaService<T> build() {
        if (this.patternTopic == null) {
            return new KafkaService<>(groupId, topic, parse, properties);
        }

        return new KafkaService<>(groupId, patternTopic, parse, properties);
    }
}
