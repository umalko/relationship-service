package com.mavs.relationshipservice.client;

import com.mavs.activity.dto.ActivityUserDto;
import com.mavs.relationshipservice.client.fallback.UserClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "auth-service", fallback = UserClientFallback.class)
public interface UserClient {

    @GetMapping("/api/v1/users/email/{email}")
    Optional<ActivityUserDto> findUserByEmail(@PathVariable("email") String email);
}