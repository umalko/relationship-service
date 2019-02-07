package com.mavs.relationshipservice.controller;

import com.mavs.relationshipservice.dto.RelationshipDto;
import com.mavs.relationshipservice.model.Person;
import com.mavs.relationshipservice.service.RelationshipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/v1/relationships")
public class RelationshipController {

    private final RelationshipService relationshipService;

    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @GetMapping("/{email}")
    public Set<String> getAllRelationships(@PathVariable("email") String email) {
        return relationshipService.getAllRelationships(email)
                .stream().map(Person::getEmail).collect(Collectors.toSet());
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void addRelationship(@RequestBody RelationshipDto relationshipDto) {
        relationshipService.addRelationship(relationshipDto);
    }

    @DeleteMapping
    public void removeRelationship(@RequestBody RelationshipDto relationshipDto) {
        relationshipService.removeRelationship(relationshipDto);
    }
}
