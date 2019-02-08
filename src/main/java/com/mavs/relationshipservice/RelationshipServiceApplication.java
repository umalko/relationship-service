package com.mavs.relationshipservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableFeignClients
@EnableEurekaClient
@EnableNeo4jRepositories
@EnableTransactionManagement
@SpringBootApplication
@ComponentScan({"com.mavs.activity.provider.*", "com.mavs.relationshipservice.*"})
public class RelationshipServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RelationshipServiceApplication.class, args);
    }
}

