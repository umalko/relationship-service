package com.mavs.relationshipservice.repository;

import com.mavs.relationshipservice.model.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;

import java.util.Optional;

public interface PersonRepository extends Neo4jRepository<Person, Long> {

    Optional<Person> findByEmail(String email);
}
