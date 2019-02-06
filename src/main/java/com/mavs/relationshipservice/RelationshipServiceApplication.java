package com.mavs.relationshipservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;

@EnableFeignClients
@EnableEurekaClient
@EnableNeo4jRepositories
@SpringBootApplication
public class RelationshipServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RelationshipServiceApplication.class, args);
    }

}

