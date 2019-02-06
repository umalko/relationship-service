package com.mavs.relationshipservice.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mavs.relationshipservice.client.UserClient;
import com.mavs.relationshipservice.dto.UserDto;
import com.mavs.relationshipservice.model.Person;
import com.mavs.relationshipservice.repository.PersonRepository;
import com.mavs.relationshipservice.service.RelationshipService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RelationshipServiceImpl implements RelationshipService {

    private final PersonRepository personRepository;

    private final UserClient userClient;

    public RelationshipServiceImpl(PersonRepository personRepository, UserClient userClient) {
        this.personRepository = personRepository;
        this.userClient = userClient;
    }

    @Override
    public void addRelationship(String personEmail, String newPersonRelationshipEmail) {
        savePersonsIfNotExist(Lists.newArrayList(personEmail, newPersonRelationshipEmail));
        Person person = addRelationshipToPerson(personEmail, newPersonRelationshipEmail);
        personRepository.save(person);
    }

    @Override
    public void removeRelationship(String personEmail, String personRelationshipEmail) {
        savePersonsIfNotExist(Lists.newArrayList(personEmail, personRelationshipEmail));
        Person person = removeRelationshipToPerson(personEmail, personRelationshipEmail);
        personRepository.save(person);
    }

    @Override
    public Set<Person> getAllRelationships(String personEmail) {
        Optional<Person> personOptional = personRepository.findByEmail(personEmail);
        return personOptional.isPresent() ? personOptional.get().getFriends() : Sets.newHashSet();
    }

    private Person removeRelationshipToPerson(String personEmail, String personRelationshipEmail) {
        Person person = personRepository.findByEmail(personEmail).get();
        Person newFriend = personRepository.findByEmail(personRelationshipEmail).get();
        removeFriend(person, newFriend);
        return person;
    }

    private Person addRelationshipToPerson(String personEmail, String newPersonRelationshipEmail) {
        Person person = personRepository.findByEmail(personEmail).get();
        Person newFriend = personRepository.findByEmail(newPersonRelationshipEmail).get();
        addFriend(person, newFriend);
        return person;
    }

    private void savePersonsIfNotExist(List<String> personsEmails) {
        personsEmails.forEach(personEmail -> {
            Optional<Person> personOptional = personRepository.findByEmail(personEmail);
            if (!personOptional.isPresent()) {
                UserDto userDto = userClient.findUserByEmail(personEmail);
                personRepository.save(new Person(userDto.getEmail()));
            }
        });
    }

    private void addFriend(Person person, Person newFriend) {
        if (person.getFriends() == null) {
            person.setFriends(Sets.newHashSet());
        }
        person.getFriends().add(newFriend);
    }

    private void removeFriend(Person person, Person friend) {
        if (person.getFriends() == null) {
            person.setFriends(Sets.newHashSet());
        }
        person.getFriends().remove(friend);
    }
}
