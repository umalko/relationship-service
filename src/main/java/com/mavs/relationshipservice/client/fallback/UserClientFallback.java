package com.mavs.relationshipservice.client.fallback;

import com.mavs.activity.dto.ActivityUserDto;
import com.mavs.relationshipservice.client.UserClient;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public Optional<ActivityUserDto> findUserByEmail(String email) {
        return Optional.empty();
    }
}
