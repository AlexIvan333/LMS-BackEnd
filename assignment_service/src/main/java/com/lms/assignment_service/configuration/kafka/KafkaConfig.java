package com.lms.assignment_service.configuration.kafka;

import com.lms.shared.events.ResourceExistsResponseEvent;
import com.lms.shared.events.UserExistsResponseEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    private static final String BOOTSTRAP_SERVERS = "host.docker.internal:9092";

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // === User Validation ===
    @Bean
    public ConsumerFactory<String, UserExistsResponseEvent> userExistsConsumerFactory() {
        JsonDeserializer<UserExistsResponseEvent> deserializer = new JsonDeserializer<>(UserExistsResponseEvent.class);
        return new DefaultKafkaConsumerFactory<>(
                commonConsumerProps(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, UserExistsResponseEvent> userExistsListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, UserExistsResponseEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(userExistsConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, UserExistsResponseEvent> userRepliesContainer() {
        var container = userExistsListenerFactory().createContainer("user-validation-response");
        container.getContainerProperties().setGroupId("assignment-service-reply-user");
        return container;
    }

    @Bean
    public ReplyingKafkaTemplate<String, Object, UserExistsResponseEvent> userReplyingKafkaTemplate() {
        return new ReplyingKafkaTemplate<>(producerFactory(), userRepliesContainer());
    }

    // === Resource Validation ===
    @Bean
    public ConsumerFactory<String, ResourceExistsResponseEvent> resourceExistsConsumerFactory() {
        JsonDeserializer<ResourceExistsResponseEvent> deserializer = new JsonDeserializer<>(ResourceExistsResponseEvent.class);
        return new DefaultKafkaConsumerFactory<>(
                commonConsumerProps(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ResourceExistsResponseEvent> resourceExistsListenerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ResourceExistsResponseEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(resourceExistsConsumerFactory());
        return factory;
    }

    @Bean
    public ConcurrentMessageListenerContainer<String, ResourceExistsResponseEvent> resourceRepliesContainer() {
        var container = resourceExistsListenerFactory().createContainer("resource-validation-response");
        container.getContainerProperties().setGroupId("assignment-service-reply-resource");
        return container;
    }

    @Bean
    public ReplyingKafkaTemplate<String, Object, ResourceExistsResponseEvent> resourceReplyingKafkaTemplate() {
        return new ReplyingKafkaTemplate<>(producerFactory(), resourceRepliesContainer());
    }

    private Map<String, Object> commonConsumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "assignment-service");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }
}
