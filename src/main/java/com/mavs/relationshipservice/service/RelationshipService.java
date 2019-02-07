package com.mavs.relationshipservice.service;

import com.mavs.relationshipservice.dto.RelationshipDto;
import com.mavs.relationshipservice.model.Person;

import java.util.Set;

public interface RelationshipService {

    void addRelationship(RelationshipDto relationshipDto);

    void removeRelationship(RelationshipDto relationshipDto);

    Set<Person> getAllRelationships(String personEmail);

}
