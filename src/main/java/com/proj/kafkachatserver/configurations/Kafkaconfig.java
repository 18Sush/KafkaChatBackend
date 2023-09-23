package com.proj.kafkachatserver.configurations;

import org.springframework.context.annotation.Configuration;

import org.springframework.kafka.annotation.EnableKafka;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.Properties;
@EnableKafka
@Configuration
public class Kafkaconfig {

}
