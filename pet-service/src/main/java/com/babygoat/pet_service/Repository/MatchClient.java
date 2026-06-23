package com.babygoat.pet_service.Repository;

import com.babygoat.pet_service.DTO.MatchDTO;
import com.babygoat.pet_service.Security.FeignConfig;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "match-service", url = "${MATCH_SERVICE_URL:http://match-service:8080}", configuration = FeignConfig.class)
public interface MatchClient {
    @PostMapping("/api/matches/create")
    void notifyNewMatch(@RequestBody MatchDTO dto);
}
