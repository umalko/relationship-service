package com.mavs.relationshipservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@NodeEntity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    private String email;

    @Relationship(type = "FRIEND", direction = Relationship.UNDIRECTED)
    private Set<Person> friends;

    public Person(String email) {
        this.email = email;
    }

    public String toString() {

        return this.email + "'s friends => "
                + Optional.ofNullable(this.friends).orElse(
                Collections.emptySet()).stream()
                .map(Person::getEmail)
                .collect(Collectors.toList());
    }
}
