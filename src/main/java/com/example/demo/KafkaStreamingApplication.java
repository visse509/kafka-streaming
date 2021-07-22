package com.example.demo;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.Record;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.util.Properties;

@Configuration
public class KafkaStreamingApplication {

    @Bean
    public Properties streamConfiguration() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:29092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        properties.put(StreamsConfig.STATE_DIR_CONFIG, "/tmp");
        return properties;
    }

    @Bean
    public KafkaStreams startKafkaStreams(Properties streamConfiguration) {
        streamConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "audit-reordering");
        Topology topology = new Topology();
        topology.addSource("Source", "in-topic");
        topology.addProcessor("time-processor", () -> (Processor<String, String, String, String>) record ->  {
            System.out.println("Published on queue: " + record.value());
            System.out.println("Processed by streaming application: " + Instant.now());
        }, "Source");
        KafkaStreams kafkaStream = new KafkaStreams(topology, streamConfiguration);
        kafkaStream.start();
        return kafkaStream;
    }
}
