package com.mavs.relationshipservice.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mavs.activity.dto.ActivityDto;
import com.mavs.activity.dto.ActivityRelationshipDto;
import com.mavs.activity.dto.ActivityUserDto;
import com.mavs.activity.dto.RelationshipType;
import com.mavs.activity.model.ActivityType;
import com.mavs.activity.provider.ActivityMessageQueueProvider;
import com.mavs.activity.util.ActivityUtil;
import com.mavs.relationshipservice.client.UserClient;
import com.mavs.relationshipservice.dto.RelationshipDto;
import com.mavs.relationshipservice.model.Person;
import com.mavs.relationshipservice.model.PersonType;
import com.mavs.relationshipservice.repository.PersonRepository;
import com.mavs.relationshipservice.service.RelationshipService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Transactional
public class RelationshipServiceImpl implements RelationshipService {

    private final PersonRepository personRepository;
    private final UserClient userClient;
    private final ActivityMessageQueueProvider activityMessageQueueProvider;

    public RelationshipServiceImpl(PersonRepository personRepository, UserClient userClient, ActivityMessageQueueProvider activityMessageQueueProvider) {
        this.personRepository = personRepository;
        this.userClient = userClient;
        this.activityMessageQueueProvider = activityMessageQueueProvider;
    }

    @Override
    public Set<Person> getAllRelationships(String personEmail) {
        Optional<Person> personOptional = personRepository.findByEmail(personEmail);
        return personOptional.isPresent() ? personOptional.get().getFriends() : Sets.newHashSet();
    }

    @Override
    public void addRelationship(RelationshipDto relationshipDto) {
        String personEmail = relationshipDto.getPersonEmail();
        String personRelationshipEmail = relationshipDto.getPersonRelationshipEmail();
        log.info("New relationship with {} and {}", personEmail, personRelationshipEmail);

        savePersonsIfNotExist(Lists.newArrayList(personEmail, personRelationshipEmail));
        Person person = addRelationshipToPerson(personEmail, personRelationshipEmail);
        personRepository.save(person);

        sendActivity(personEmail, personRelationshipEmail, RelationshipType.NEW, ActivityType.NEW_RELATIONSHIP);
    }

    @Override
    public void removeRelationship(RelationshipDto relationshipDto) {
        String personEmail = relationshipDto.getPersonEmail();
        String personRelationshipEmail = relationshipDto.getPersonRelationshipEmail();
        log.info("Removed relationship with {} and {}", personEmail, personRelationshipEmail);

        savePersonsIfNotExist(Lists.newArrayList(personEmail, personRelationshipEmail));
        Person person = removeRelationshipToPerson(personEmail, personRelationshipEmail);
        personRepository.save(person);
        sendActivity(personEmail, personRelationshipEmail, RelationshipType.END, ActivityType.END_RELATIONSHIP);
    }

    private void sendActivity(String personEmail, String personRelationshipEmail, RelationshipType type, ActivityType activityType) {
        ActivityRelationshipDto activityRelationshipDto = buildActivityRelationshipDto(personEmail, personRelationshipEmail, type);
        ActivityUtil.buildActivity(activityRelationshipDto, activityType).ifPresent(activity -> {
            ActivityDto activityDto = ActivityUtil.buildActivityDto(activity, activityRelationshipDto);
            activityMessageQueueProvider.produceActivity(activityDto);
        });
    }

    private Person removeRelationshipToPerson(String personEmail, String personRelationshipEmail) {
        Person person = personRepository.findByEmail(personEmail).get();
        Person friend = personRepository.findByEmail(personRelationshipEmail).get();
        removeFriend(person, friend);
        removeFriend(friend, person);
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
                Optional<ActivityUserDto> userOptional = userClient.findUserByEmail(personEmail);
                if (userOptional.isPresent()) { // checked user. Exist in the system
                    personRepository.save(new Person(userOptional.get().getEmail(), PersonType.CHECKED));
                } else { // unchecked user. Validate again later.
                    log.error("User Service is unavailable! User {} wasn't checked!", personEmail);
                    personRepository.save(new Person(personEmail, PersonType.UNCHECKED));
                }
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

    private ActivityRelationshipDto buildActivityRelationshipDto(String personEmail, String newPersonRelationshipEmail, RelationshipType type) {
        return ActivityRelationshipDto.builder()
                .personEmail(personEmail)
                .personRelationshipEmail(newPersonRelationshipEmail)
                .relationshipType(type)
                .build();
    }
}
