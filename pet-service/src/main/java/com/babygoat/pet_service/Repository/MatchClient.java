package com.babygoat.pet_service.Repository;

import com.babygoat.pet_service.DTO.MatchDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "match-service",
        url = "${MATCH_SERVICE_URL:http://match-service:8080}")
public interface MatchClient {
    @PostMapping(value = "/api/matches/create", consumes = "application/json")
    void notifyNewMatch(@RequestHeader("X-Internal-Secret") String internalSecret, @RequestBody MatchDTO dto);
}
