package com.mavs.relationshipservice.client;

import com.mavs.relationshipservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface UserClient {

    @GetMapping("/api/v1/users/email/{email}")
    UserDto findUserByEmail(@PathVariable("email") String email);
}