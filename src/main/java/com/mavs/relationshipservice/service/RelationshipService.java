package com.mavs.relationshipservice.service;

import com.mavs.relationshipservice.model.Person;

import java.util.Set;

public interface RelationshipService {

    void addRelationship(String personEmail, String newPersonRelationshipEmail);

    void removeRelationship(String personEmail, String personRelationshipEmail);

    Set<Person> getAllRelationships(String personEmail);

}
